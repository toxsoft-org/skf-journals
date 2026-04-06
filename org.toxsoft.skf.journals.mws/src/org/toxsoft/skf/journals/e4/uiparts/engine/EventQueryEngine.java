package org.toxsoft.skf.journals.e4.uiparts.engine;

import static org.toxsoft.skf.journals.e4.uiparts.engine.ISkResources.*;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.ctx.*;
import org.toxsoft.core.tslib.bricks.threadexec.*;
import org.toxsoft.core.tslib.bricks.time.*;
import org.toxsoft.core.tslib.bricks.time.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.gw.gwid.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.logs.impl.*;
import org.toxsoft.uskat.core.*;
import org.toxsoft.uskat.core.api.evserv.*;
import org.toxsoft.uskat.core.api.hqserv.*;
import org.toxsoft.uskat.core.gui.conn.*;
import org.toxsoft.uskat.core.gui.glib.query.*;
import org.toxsoft.uskat.core.impl.*;

/**
 * Реализация движка {@link IQueryEngine} для событий.
 *
 * @author mvk
 */
public class EventQueryEngine
    implements IQueryEngine<SkEvent> {

  /**
   * Таймаут (мсек) запроса событий. < 0: бесконечно
   */
  private static final long EVENT_QUERY_TIMEOUT = -1;

  private final Shell      shell;
  private final ISkCoreApi coreApi;

  /**
   * Конструктор.
   *
   * @param aContext {@link ITsContext} контекст
   * @throws TsNullArgumentRtException аргумент = null
   */
  public EventQueryEngine( ITsContext aContext ) {
    TsNullArgumentRtException.checkNull( aContext );
    shell = aContext.get( Shell.class );
    coreApi = aContext.get( ISkConnectionSupplier.class ).defConn().coreApi();
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса IEventQueryEngine
  //
  @Override
  public IList<SkEvent> query( ITimeInterval aInterval, IJournalQueryFilter aParams ) {
    TsNullArgumentRtException.checkNulls( aInterval, aParams );
    if( aParams.items().isEmpty() ) {
      return IList.EMPTY;
    }
    GwidList gwids = new GwidList();
    for( int i = 0, count = aParams.items().size(); i < count; i++ ) {
      ConcerningEventsItem item = (ConcerningEventsItem)aParams.items().get( i );
      gwids.addAll( item.gwids( true, coreApi ) );
    }
    LoggerUtils.info( "EventQueryEngine.query(...): gwids size = %d", gwids.size() );
    for( Gwid gwid : gwids ) {
      LoggerUtils.info( "EventQueryEngine.query(...): gwid = %s", gwid.asString() );
    }

    // Исполнитель запросов в одном потоке
    ITsThreadExecutor threadExecutor = SkThreadExecutorService.getExecutor( coreApi );
    // Настройка обработки результатов запроса
    IListEdit<ITimedList<SkEvent>> queryResults = new ElemArrayList<>();
    // Создание диалога прогресса выполнения запроса
    SkAbstractQueryDialog<ISkQueryRawHistory> dialog =
        new SkAbstractQueryDialog<>( shell, MSG_QUERIENG_CMDS, EVENT_QUERY_TIMEOUT, threadExecutor ) {

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
                  getProgressMonitor().setTaskName( String.format( MSG_PREPARE_EVENTS_QUERY ) );
                  break;
                case READY:
                  int cmdQtty = 0;
                  for( Gwid gwid : aQuery.listGwids() ) {
                    ITimedList<SkEvent> cmds = aQuery.get( gwid );
                    queryResults.add( cmds );
                    cmdQtty += cmds.size();
                  }
                  getProgressMonitor()
                      .setTaskName( String.format( MSG_PREPARE_EVENTS_VIEW, Integer.valueOf( cmdQtty ) ) );
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
      throw new TsIllegalStateRtException( ERR_QUERY_EVENTS_FAILED, dialog.query().stateMessage() );
    }
    // Результат
    return TimeUtils.uniteTimeporaLists( queryResults );
  }
}
