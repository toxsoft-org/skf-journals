package org.toxsoft.skf.journals.e4.uiparts.engine;

import static org.toxsoft.skf.journals.e4.uiparts.engine.ISkResources.*;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.widgets.Shell;
import org.toxsoft.core.tsgui.dialogs.TsDialogUtils;
import org.toxsoft.core.tslib.av.impl.AvUtils;
import org.toxsoft.core.tslib.av.opset.IOptionSetEdit;
import org.toxsoft.core.tslib.av.opset.impl.OptionSet;
import org.toxsoft.core.tslib.av.opset.impl.OptionSetUtils;
import org.toxsoft.core.tslib.bricks.ctx.ITsContext;
import org.toxsoft.core.tslib.bricks.time.*;
import org.toxsoft.core.tslib.bricks.time.impl.QueryInterval;
import org.toxsoft.core.tslib.bricks.time.impl.TimedList;
import org.toxsoft.core.tslib.coll.IList;
import org.toxsoft.core.tslib.coll.IListEdit;
import org.toxsoft.core.tslib.coll.impl.ElemArrayList;
import org.toxsoft.core.tslib.gw.gwid.Gwid;
import org.toxsoft.core.tslib.gw.gwid.GwidList;
import org.toxsoft.core.tslib.utils.errors.TsIllegalStateRtException;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;
import org.toxsoft.core.tslib.utils.logs.impl.LoggerUtils;
import org.toxsoft.uskat.core.ISkCoreApi;
import org.toxsoft.uskat.core.api.cmdserv.IDtoCompletedCommand;
import org.toxsoft.uskat.core.api.hqserv.*;
import org.toxsoft.uskat.core.gui.conn.ISkConnectionSupplier;
import org.toxsoft.uskat.core.gui.glib.query.ISkQueryCancelProducer;
import org.toxsoft.uskat.core.gui.glib.query.SkQueryUtils;
import org.toxsoft.uskat.core.utils.SkTimedListUtils;

/**
 * Реализация движка {@link IQueryEngine}.
 *
 * @author mvk
 */
public class CommandQueryEngine
    implements IQueryEngine<IDtoCompletedCommand> {

  /**
   * Таймаут (мсек) запроса команд. < 0: бесконечно
   */
  private static final long EVENT_QUERY_TIMEOUT = -1;

  private final Shell      shell;
  private final ISkCoreApi coreApi;

  /**
   * Просто конструктор.
   *
   * @param aContext {@link ITsContext} контекст
   * @throws TsNullArgumentRtException аргумент = null
   */
  public CommandQueryEngine( ITsContext aContext ) {
    TsNullArgumentRtException.checkNull( aContext );
    shell = aContext.get( Shell.class );
    coreApi = aContext.get( ISkConnectionSupplier.class ).defConn().coreApi();
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса IEventQueryEngine
  //

  @Override
  public IList<IDtoCompletedCommand> query( ITimeInterval aInterval, IJournalQueryFilter aParams,
      IProgressMonitor aMonitor, ISkQueryCancelProducer aCancelProducer ) {
    TsNullArgumentRtException.checkNulls( aInterval, aParams, aMonitor, aCancelProducer );
    if( aParams.items().isEmpty() ) {
      return IList.EMPTY;
    }
    GwidList gwids = new GwidList();
    for( int i = 0, count = aParams.items().size(); i < count; i++ ) {
      ConcerningEventsItem item = (ConcerningEventsItem)aParams.items().get( i );
      gwids.addAll( item.gwids( false, coreApi ) );
    }
    LoggerUtils.defaultLogger().info( "CommandQueryEngine.query(...): gwids size = %d", gwids.size() );
    for( Gwid gwid : gwids ) {
      LoggerUtils.defaultLogger().info( "CommandQueryEngine.query(...): gwid = %s", gwid.asString() );
    }

    // Параметры запроса
    IOptionSetEdit options = new OptionSet( OptionSetUtils.createOpSet( //
        ISkHistoryQueryServiceConstants.OP_SK_MAX_EXECUTION_TIME, AvUtils.avInt( EVENT_QUERY_TIMEOUT ) //
    ) );
    // Формирование запроса
    ISkQueryRawHistory query = coreApi.hqService().createHistoricQuery( options );
    try {
      // Подготовка запроса
      query.prepare( gwids );
      // Настройка обработки результатов запроса
      TimedList<IDtoCompletedCommand> retValue = new TimedList<>();
      query.genericChangeEventer().addListener( aSource -> {
        ISkQueryRawHistory q = (ISkQueryRawHistory)aSource;
        if( q.state() == ESkQueryState.READY ) {
          for( Gwid gwid : query.listGwids() ) {
            retValue.addAll( query.get( gwid ) );
          }
        }
        if( q.state() == ESkQueryState.FAILED ) {
          String stateMessage = q.stateMessage();
          TsDialogUtils.error( shell, ERR_QUERY_CMDS_FAILED, stateMessage );
        }
      } );
      // Интервал запроса
      IQueryInterval interval =
          new QueryInterval( EQueryIntervalType.CSCE, aInterval.startTime(), aInterval.endTime() );
      // Выполение запроса
      SkQueryUtils.execQueryWithProgress( query, interval, aMonitor, aCancelProducer );
      // Обработка результата
      if( query.state() == ESkQueryState.READY ) {
        int eventQtty = 0;
        IListEdit<ITimedList<IDtoCompletedCommand>> paramLists = new ElemArrayList<>();
        for( Gwid gwid : query.listGwids() ) {
          ITimedList<IDtoCompletedCommand> cmds = query.get( gwid );
          paramLists.add( cmds );
          eventQtty += cmds.size();
        }
        aMonitor.setTaskName( String.format( MSG_PREPARE_CMDS_VIEW, Integer.valueOf( eventQtty ) ) );
        return SkTimedListUtils.uniteTimeporalLists( paramLists );
      }
      if( query.state() == ESkQueryState.FAILED ) {
        throw new TsIllegalStateRtException( ERR_QUERY_CMDS_FAILED, query.stateMessage() );
      }
      // Пустой результат (ошибка)
      return new TimedList<>();
    }
    finally {
      query.close();
    }
  }
}
