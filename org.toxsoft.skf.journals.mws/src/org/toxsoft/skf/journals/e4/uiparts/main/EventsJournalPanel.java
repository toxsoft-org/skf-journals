package org.toxsoft.skf.journals.e4.uiparts.main;

import static org.toxsoft.core.tsgui.m5.gui.mpc.IMultiPaneComponentConstants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.skf.journals.e4.uiparts.ISkJournalsHardConstants.*;
import static org.toxsoft.skf.journals.e4.uiparts.main.ISkResources.*;

import java.text.*;
import java.util.*;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.jasperreports.gui.main.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.dialogs.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.gui.mpc.impl.*;
import org.toxsoft.core.tsgui.m5.gui.panels.*;
import org.toxsoft.core.tsgui.m5.gui.panels.impl.*;
import org.toxsoft.core.tsgui.m5.gui.viewers.impl.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.panels.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.time.*;
import org.toxsoft.core.tslib.bricks.time.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.coll.notifier.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.gw.skid.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.logs.impl.*;
import org.toxsoft.skf.journals.e4.uiparts.*;
import org.toxsoft.skf.journals.e4.uiparts.engine.*;
import org.toxsoft.skf.journals.e4.uiparts.engine.IJournalParamsPanel.*;
import org.toxsoft.uskat.core.api.evserv.*;
import org.toxsoft.uskat.core.api.sysdescr.*;
import org.toxsoft.uskat.core.api.sysdescr.dto.*;
import org.toxsoft.uskat.core.api.users.*;
import org.toxsoft.uskat.core.connection.*;
import org.toxsoft.uskat.core.gui.conn.*;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.type.*;

/**
 * Панель журнала событий.
 *
 * @author max
 * @author dima
 */
public class EventsJournalPanel
    extends TsPanel
    implements IGenericChangeListener {

  /**
   * формат для отображения метки времени
   */
  private static final String timestampFormatString = "dd.MM.yy HH:mm:ss"; //$NON-NLS-1$

  private static final DateFormat timestampFormat = new SimpleDateFormat( timestampFormatString );

  private static final String ERR_MSG_NO_DOMAIN_IN_CONTEXT = "No domain in context"; //$NON-NLS-1$

  /**
   * Таймаут (мсек) запроса событий. < 0: бесконечно
   */
  private static final long EVENT_QUERY_TIMEOUT = -1;

  /**
   * Максимальное количество отображаемых событий в панели
   */
  private static final int EVENT_PANEL_COUNT_LIMIT = 8192;

  private EventQueryEngine queryEngine;

  // private M5DefaultItemsProvider<SkEvent> eventProvider;

  private InternalItemsProvider eventProvider;

  private IM5CollectionPanel<SkEvent> panel;

  private JournalParamsPanel paramsPanel;

  private IM5Model<SkEvent> eventsModel;

  /**
   * Конструктор.
   *
   * @param aParent - родительский компонент.
   * @param aContext - контекст.
   * @throws TsException - ошибка при формировании панели журнала событий.
   */
  public EventsJournalPanel( Composite aParent, ITsGuiContext aContext )
      throws TsException {
    super( aParent, aContext );
    init( aContext, EVENTS_FILTER_CLASSES_TREE_MODEL_LIB );
  }

  /**
   * Конструктор.
   *
   * @param aParent - родительский компонент.
   * @param aContext - контекст.
   * @param aFilteredClassesModelId отфильтрованный список классов
   * @throws TsException - ошибка при формировании панели журнала событий.
   */
  public EventsJournalPanel( Composite aParent, ITsGuiContext aContext, IDataDef aFilteredClassesModelId )
      throws TsException {
    super( aParent, aContext );
    init( aContext, aFilteredClassesModelId );
  }

  private void init( ITsGuiContext aContext, IDataDef aEventsFilterClassesTreeModelLib )
      throws TsException {
    ISkConnection connection = aContext.get( ISkConnectionSupplier.class ).defConn();

    if( !m5().models().hasKey( EventM5Model.MODEL_ID ) ) {
      m5().addModel( new EventM5Model( connection, aContext ) );
    }

    setLayout( new BorderLayout() );

    eventsModel = m5().getModel( EventM5Model.MODEL_ID, SkEvent.class );

    eventProvider = new InternalItemsProvider();

    OPDEF_IS_FILTER_PANE.setValue( aContext.params(), AV_TRUE );
    OPDEF_IS_TOOLBAR_NAME.setValue( aContext.params(), AV_TRUE );

    M5TreeViewer<SkEvent> treeViewer = new M5EventsTreeViewer( aContext, eventsModel );

    MultiPaneComponentModown<SkEvent> eventComponent = new MultiPaneComponentModown<>( treeViewer );

    eventComponent.setItemProvider( eventProvider );

    panel = new M5CollectionPanelMpcModownWrapper<>( eventComponent, true );

    // panel = eventsModel.panelCreator().createCollViewerPanel( aContext, eventProvider );
    panel.createControl( this ).setLayoutData( BorderLayout.CENTER );

    queryEngine = new EventQueryEngine( aContext );

    JournalsLibUtils.loadClassesTreeModel( aContext, aEventsFilterClassesTreeModelLib );

    paramsPanel = new JournalParamsPanel( aContext );

    paramsPanel.addListener( this );

    JournalsLibUtils.loadObjectsTreeModel( aContext, EVENTS_FILTER_OBJECTS_TREE_MODEL_LIB );

    paramsPanel.setAppContext( aContext.eclipseContext() );

    // панель инструментов сверху
    paramsPanel.createControl( this );
    paramsPanel.getControl().setLayoutData( BorderLayout.NORTH );
    // настройка взаимодействия между компонентами
    // paramsPanel.addGenericChangeListener( queryStartListener );
    // eventProvider.items().addAll( lm.doListEntities() );

    panel.refresh();
  }

  /**
   * Возвращает домен моделирования сущностей приложения.
   *
   * @return {@link IM5Domain} - домен моделирования сущностей приложения
   */
  @Override
  public IM5Domain m5() {
    IM5Domain d = eclipseContext().get( IM5Domain.class );
    TsIllegalStateRtException.checkNull( d, ERR_MSG_NO_DOMAIN_IN_CONTEXT );
    return d;
  }

  /**
   * @return параметры запроса на все события
   */
  IConcerningEventsParams allEventsParams() {
    ConcerningEventsParams retVal = new ConcerningEventsParams();
    for( ISkClassInfo classInfo : listNeededClasses() ) {

      IStringListEdit eventsIds = new StringArrayList();

      for( IDtoEventInfo event : classInfo.events().list() ) {
        eventsIds.add( event.id() );
      }

      ConcerningEventsItem evItem = new ConcerningEventsItem( classInfo.id(), eventsIds, IStringList.EMPTY );
      retVal.addItem( evItem );
    }
    return retVal;
  }

  protected IStridablesList<ISkClassInfo> listNeededClasses() {
    @SuppressWarnings( "unchecked" )
    ILibClassInfoesTreeModel<IDtoEventInfo> classModel =
        (ILibClassInfoesTreeModel<IDtoEventInfo>)tsContext().get( FILTER_CLASSES_TREE_MODEL_LIB );
    return classModel.getRootClasses();
  }

  @Override
  public void onGenericChangeEvent( Object aSource ) {

    IJournalParamsPanel journalPanel = (IJournalParamsPanel)aSource;
    if( journalPanel.currentAction() == ECurrentAction.QUERY_ALL ) {
      queryAllEvents();
    }
    if( journalPanel.currentAction() == ECurrentAction.QUERY_SELECTED ) {
      querySelectedEvents();
    }
    if( journalPanel.currentAction() == ECurrentAction.PRINT ) {
      printEvents();
    }
    // if( journalPanel.currentAction() == ECurrentAction.EXPORT_EXCEL ) {
    // try {
    // new ProgressMonitorDialog( getShell() ).run( true, true, new XlsExportRunner() );
    // }
    // catch( Exception e ) {
    // LoggerUtils.errorLogger().error( e );
    // TsDialogUtils.error( getShell(), e );
    // }
    // }

    // if(journalPanel.currentAction() == ECurrentAction.SEARCH_IN_LIST){
    // String searchStr = paramsPanel.searchString();
    // IList<IDisplayableEvent> events = eventsProvider.list();
    // IListEdit<IDisplayableEvent> searchedEvents = new ElemArrayList<>();
    // for(IDisplayableEvent event: events){
    // String longDescr = event.longDescription();
    // if(longDescr.contains(searchStr)){
    // searchedEvents.add(event);
    // }
    // }
    // eventsProvider.setList(searchedEvents);
    // }

  }

  private void printEvents() {
    try {
      ISkConnectionSupplier connectionSup = eclipseContext().get( ISkConnectionSupplier.class );
      ISkConnection connection = connectionSup.defConn();
      EventM5Model printEventsModel = new EventM5Model( connection, tsContext(), true );

      m5().initTemporaryModel( printEventsModel );

      ITsGuiContext printContext = new TsGuiContext( tsContext() );

      long startTime = paramsPanel.interval().startTime();
      long endTime = paramsPanel.interval().endTime();

      String title = String.format( PRINT_EVENT_LIST_TITLE_FORMAT, timestampFormat.format( new Date( startTime ) ),
          timestampFormat.format( new Date( endTime ) ) );

      IJasperReportConstants.REPORT_TITLE_M5_ID.setValue( printContext.params(), AvUtils.avStr( title ) );

      // выясняем текущего пользователя

      Skid currUser = connection.coreApi().getCurrentUserInfo().userSkid();
      ISkUser user = connection.coreApi().userService().getUser( currUser.strid() );
      String userName = user.nmName().trim().length() > 0 ? user.nmName() : user.login();

      IJasperReportConstants.LEFT_BOTTOM_STR_M5_ID.setValue( printContext.params(),
          AvUtils.avStr( AUTHOR_STR + userName ) );
      IJasperReportConstants.RIGHT_BOTTOM_STR_M5_ID.setValue( printContext.params(),
          AvUtils.avStr( DATE_STR + timestampFormat.format( new Date() ) ) );

      printContext.params().setStr( IJasperReportConstants.REPORT_DATA_HORIZONTAL_TEXT_ALIGN_ID,
          HorizontalTextAlignEnum.LEFT.getName() );

      final JasperPrint jasperPrint =
          ReportGenerator.generateJasperPrint( printContext, printEventsModel, eventProvider );
      JasperReportDialog.showPrint( printContext, jasperPrint );
    }
    catch( Exception ex ) {
      LoggerUtils.errorLogger().error( ex );
      TsDialogUtils.error( getShell(), ex );
    }

  }

  @SuppressWarnings( { "boxing", "nls" } )
  private void queryAllEvents() {
    Display display = getShell().getDisplay();
    ITimeInterval interval = paramsPanel.interval();
    try {
      LoggerUtils.defaultLogger().info( "queryAllEvents(): queryEngine.query( interval, allEventsParams() )" );
      IList<SkEvent> queryEvents = queryEngine.query( interval, allEventsParams() );
      LoggerUtils.defaultLogger().info( "queryAllEvents(): eventProvider.setItems( events ). events count = %d",
          queryEvents.size() );
      if( queryEvents.size() > EVENT_PANEL_COUNT_LIMIT ) {
        IList<SkEvent> newEvents = queryEvents.fetch( 0, EVENT_PANEL_COUNT_LIMIT );
        ITimeInterval newInterval = new TimeInterval( newEvents.first().timestamp(), newEvents.last().timestamp() );
        String warn = String.format( STR_EVENTS_LIMIT, queryEvents.size(), EVENT_PANEL_COUNT_LIMIT, newInterval );
        LoggerUtils.defaultLogger().warning( warn );
        TsDialogUtils.warn( getShell(), warn );
        queryEvents = newEvents;
      }
      IList<SkEvent> events = queryEvents;
      display.syncExec( () -> eventProvider.setItems( events ) );
      LoggerUtils.defaultLogger().info( "queryAllEvents(): panel.refresh()" );
      display.syncExec( () -> panel.refresh() );
    }
    catch( Throwable e ) {
      String error = (e.getCause() != null ? e.getCause().getMessage() : e.getMessage());
      TsDialogUtils.error( getShell(), error );
      LoggerUtils.defaultLogger().error( e );
    }
  }

  @SuppressWarnings( { "nls", "boxing" } )
  private void querySelectedEvents() {
    Display display = getShell().getDisplay();
    ITimeInterval interval = paramsPanel.interval();
    try {
      IList<ConcerningEventsItem> selectedEvents = ((ConcerningEventsParams)paramsPanel.selectedParams()).eventItems();
      ConcerningEventsParams selEvents = new ConcerningEventsParams();
      for( ConcerningEventsItem item : selectedEvents ) {
        if( item.eventIds().isEmpty() || item.strids().isEmpty() ) {
          continue;
        }
        selEvents.addItem( item );
      }
      LoggerUtils.defaultLogger().info( "querySelectedEvents(): queryEngine.query( interval, selEvents )" );
      IList<SkEvent> queryEvents = queryEngine.query( interval, selEvents );
      LoggerUtils.defaultLogger().info( "queryAllEvents(): eventProvider.setItems( events ). events count = %d",
          queryEvents.size() );
      if( queryEvents.size() > EVENT_PANEL_COUNT_LIMIT ) {
        IList<SkEvent> newEvents = queryEvents.fetch( 0, EVENT_PANEL_COUNT_LIMIT );
        ITimeInterval newInterval = new TimeInterval( newEvents.first().timestamp(), newEvents.last().timestamp() );
        String warn = String.format( STR_EVENTS_LIMIT, queryEvents.size(), EVENT_PANEL_COUNT_LIMIT, newInterval );
        LoggerUtils.defaultLogger().warning( warn );
        TsDialogUtils.warn( getShell(), warn );
        queryEvents = newEvents;
      }
      IList<SkEvent> events = queryEvents;
      display.syncExec( () -> eventProvider.setItems( events ) );
      LoggerUtils.defaultLogger().info( "querySelectedEvents(): panel.refresh()" );
      display.syncExec( () -> panel.refresh() );
    }
    catch( Throwable e ) {
      String error = (e.getCause() != null ? e.getCause().getMessage() : e.getMessage());
      TsDialogUtils.error( getShell(), error );
      LoggerUtils.defaultLogger().error( e );
    }
  }

  /**
   * Default items provider.
   *
   * @author hazard157
   */
  static class InternalItemsProvider
      implements IM5ItemsProvider<SkEvent> {

    private IList<SkEvent> items = new ElemArrayList<>();

    @Override
    public IGenericChangeEventer genericChangeEventer() {
      return NoneGenericChangeEventer.INSTANCE;
    }

    @Override
    public IList<SkEvent> listItems() {
      return items;
    }

    void setItems( IList<SkEvent> aItems ) {
      items = aItems;
    }

    // @Override
    // public IListReorderer<SkEvent> reorderer() {
    // return doGetItemsReorderer();
    // }

  }

  /**
   * Попытка простой реализации (без нотификации) для ускорения отображения
   *
   * @author max
   * @param <T> - класс элемента коллекции
   */
  static class SimpleNotifierListEdit<T>
      extends NotifierListEditWrapper<T> {

    /**
     * По умолчанию
     */
    private static final long serialVersionUID = 1L;

    /**
     * Список, который "оборачивается" настоящим классом.
     */
    private final IListEdit<T> source;

    public SimpleNotifierListEdit( IListEdit<T> aSource ) {
      super( aSource );
      source = aSource;
    }

    @Override
    public T set( int aIndex, T aElem ) {
      return source.set( aIndex, aElem );
    }

    @Override
    public void insert( int aIndex, T aElem ) {
      source.insert( aIndex, aElem );
    }

    @Override
    public int add( T aElem ) {
      return source.add( aElem );
    }

    // @Override
    // public void fireItemByIndexChangeEvent( int aIndex ) {
    // LoggerUtils.defaultLogger().info( "fireItemByIndexChangeEvent" );
    // super.fireItemByIndexChangeEvent( aIndex );
    // }
    //
    // @Override
    // public void fireItemByRefChangeEvent( Object aItem ) {
    // LoggerUtils.defaultLogger().info( "fireItemByRefChangeEvent" );
    // super.fireItemByRefChangeEvent( aItem );
    // }
    //
    // @Override
    // protected void fireChangedEvent( ECrudOp aOp, Object aItem ) {
    // LoggerUtils.defaultLogger().info( "fireChangedEvent" );
    // super.fireChangedEvent( aOp, aItem );
    // }
    //
    // @Override
    // public void fireBatchChangeEvent() {
    // LoggerUtils.defaultLogger().info( "fireBatchChangeEvent" );
    // super.fireBatchChangeEvent();
    // }

  }

  /**
   * Класс дереве - создан для доступа к protected конструктору для передачи в него простой реализации INotifierListEdit
   */
  static class M5EventsTreeViewer
      extends M5TreeViewer<SkEvent> {

    M5EventsTreeViewer( ITsGuiContext aContext, IM5Model<SkEvent> aObjModel ) {
      super( aContext, aObjModel, new SimpleNotifierListEdit<>( new ElemArrayList<SkEvent>() ), false );
    }

  }
}
