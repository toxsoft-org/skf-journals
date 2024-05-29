package org.toxsoft.skf.journals.e4.uiparts.engine;

import static org.toxsoft.skf.journals.e4.uiparts.engine.ISkResources.*;

import org.eclipse.swt.widgets.Shell;
import org.toxsoft.core.tslib.av.opset.IOptionSetEdit;
import org.toxsoft.core.tslib.bricks.ctx.ITsContext;
import org.toxsoft.core.tslib.bricks.threadexec.ITsThreadExecutor;
import org.toxsoft.core.tslib.bricks.time.*;
import org.toxsoft.core.tslib.bricks.time.impl.QueryInterval;
import org.toxsoft.core.tslib.bricks.time.impl.TimeUtils;
import org.toxsoft.core.tslib.coll.IList;
import org.toxsoft.core.tslib.coll.IListEdit;
import org.toxsoft.core.tslib.coll.impl.ElemArrayList;
import org.toxsoft.core.tslib.gw.gwid.Gwid;
import org.toxsoft.core.tslib.gw.gwid.GwidList;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.logs.impl.LoggerUtils;
import org.toxsoft.uskat.core.ISkCoreApi;
import org.toxsoft.uskat.core.api.cmdserv.IDtoCompletedCommand;
import org.toxsoft.uskat.core.api.hqserv.ESkQueryState;
import org.toxsoft.uskat.core.api.hqserv.ISkQueryRawHistory;
import org.toxsoft.uskat.core.gui.conn.ISkConnectionSupplier;
import org.toxsoft.uskat.core.gui.glib.query.SkAbstractQueryDialog;
import org.toxsoft.uskat.core.impl.SkThreadExecutorService;

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
  private static final long CMD_QUERY_TIMEOUT = -1;

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
  public IList<IDtoCompletedCommand> query( ITimeInterval aInterval, IJournalQueryFilter aParams ) {
    TsNullArgumentRtException.checkNulls( aInterval, aParams );
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

    // Исполнитель запросов в одном потоке
    ITsThreadExecutor threadExecutor = SkThreadExecutorService.getExecutor( coreApi );
    // Настройка обработки результатов запроса
    IListEdit<ITimedList<IDtoCompletedCommand>> queryResults = new ElemArrayList<>();
    // Создание диалога прогресса выполнения запроса
    SkAbstractQueryDialog<ISkQueryRawHistory> dialog =
        new SkAbstractQueryDialog<>( shell, MSG_QUERIENG_CMDS, CMD_QUERY_TIMEOUT, threadExecutor ) {

          @Override
          protected ISkQueryRawHistory doCreateQuery( IOptionSetEdit aOptions ) {
            return coreApi.hqService().createHistoricQuery( aOptions );
          }

          @Override
          protected void doPrepareQuery( ISkQueryRawHistory aQuery ) {
            // Подготовка запроса
            aQuery.prepare( gwids );
            aQuery.genericChangeEventer().addListener( aSource -> {
              ISkQueryRawHistory q = (ISkQueryRawHistory)aSource;
              switch( q.state() ) {
                case PREPARED:
                  getProgressMonitor().setTaskName( String.format( MSG_PREPARE_CMDS_QUERY ) );
                  break;
                case READY:
                  int cmdQtty = 0;
                  for( Gwid gwid : aQuery.listGwids() ) {
                    ITimedList<IDtoCompletedCommand> cmds = aQuery.get( gwid );
                    queryResults.add( cmds );
                    cmdQtty += cmds.size();
                  }
                  getProgressMonitor()
                      .setTaskName( String.format( MSG_PREPARE_CMDS_VIEW, Integer.valueOf( cmdQtty ) ) );
                  break;
                case FAILED:
                  break;
                case EXECUTING:
                case CLOSED:
                case UNPREPARED:
                  break;
                default:
                  throw new TsNotAllEnumsUsedRtException();
              }
            } );
          }
        };
    // Запуск выполнения запроса
    dialog.executeQuery( new QueryInterval( EQueryIntervalType.CSCE, aInterval.startTime(), aInterval.endTime() ) );

    if( dialog.query().state() == ESkQueryState.FAILED ) {
      throw new TsIllegalStateRtException( ERR_QUERY_CMDS_FAILED, dialog.query().stateMessage() );
    }
    // Результат
    return TimeUtils.uniteTimeporaLists( queryResults );
  }
}
