package org.toxsoft.skf.journals.e4.uiparts.main;

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
import org.toxsoft.core.tsgui.m5.gui.panels.*;
import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.core.tsgui.panels.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.time.*;
import org.toxsoft.core.tslib.bricks.time.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.gw.skid.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.logs.impl.*;
import org.toxsoft.skf.journals.e4.uiparts.*;
import org.toxsoft.skf.journals.e4.uiparts.engine.*;
import org.toxsoft.skf.journals.e4.uiparts.engine.IJournalParamsPanel.*;
import org.toxsoft.uskat.core.api.cmdserv.*;
import org.toxsoft.uskat.core.api.sysdescr.*;
import org.toxsoft.uskat.core.api.sysdescr.dto.*;
import org.toxsoft.uskat.core.api.users.*;
import org.toxsoft.uskat.core.connection.*;
import org.toxsoft.uskat.core.gui.conn.*;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.type.*;

/**
 * Панель просмотра журнала команд
 *
 * @author dima
 */
public class CommandsJournalPanel
    extends TsPanel
    implements IGenericChangeListener {

  /**
   * формат для отображения метки времени
   */
  private static final String timestampFormatString = "dd.MM.yy HH:mm:ss"; //$NON-NLS-1$

  private static final DateFormat timestampFormat = new SimpleDateFormat( timestampFormatString );

  /**
   * Максимальное количество отображаемых команд в панели
   */
  private static final int COMMAND_PANEL_COUNT_LIMIT = 8192;

  IM5CollectionPanel<IDtoCompletedCommand> panel = null;

  JournalParamsPanel paramsPanel = null;

  private CommandQueryEngine                           queryEngine;
  private M5DefaultItemsProvider<IDtoCompletedCommand> commandProvider;

  /**
   * @param aParent родительская панель
   * @param aContext контекст
   * @throws TsException - ошибка создания
   */
  public CommandsJournalPanel( Composite aParent, ITsGuiContext aContext )
      throws TsException {
    super( aParent, aContext );

    setLayout( new BorderLayout() );

    IM5Model<IDtoCompletedCommand> userModel = m5().getModel( CommandM5Model.MODEL_ID, IDtoCompletedCommand.class );
    // EventM5LifecycleManager lm =
    // (EventM5LifecycleManager)userModel.getLifecycleManager( windowContext().get( IEventService.class ) );
    commandProvider = new M5DefaultItemsProvider<>();
    panel = userModel.panelCreator().createCollViewerPanel( aContext, commandProvider );
    panel.createControl( this ).setLayoutData( BorderLayout.CENTER );

    queryEngine = new CommandQueryEngine( aContext );

    JournalsLibUtils.loadClassesTreeModel( aContext, COMMANDS_FILTER_CLASSES_TREE_MODEL_LIB );

    paramsPanel = new JournalParamsPanel( aContext );
    paramsPanel.addListener( this );

    JournalsLibUtils.loadObjectsTreeModel( aContext, COMMANDS_FILTER_OBJECTS_TREE_MODEL_LIB );

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
    TsIllegalStateRtException.checkNull( d, "No domain in context" ); //$NON-NLS-1$
    return d;
  }

  @Override
  public void onGenericChangeEvent( Object aSource ) {
    IJournalParamsPanel journalPanel = (IJournalParamsPanel)aSource;
    if( journalPanel.currentAction() == ECurrentAction.QUERY_ALL ) {
      queryAllCommands();
    }
    if( journalPanel.currentAction() == ECurrentAction.QUERY_SELECTED ) {
      querySelectedCommands();
    }
    if( journalPanel.currentAction() == ECurrentAction.PRINT ) {
      printCommands();
    }
  }

  @SuppressWarnings( { "boxing", "nls" } )
  private void queryAllCommands() {
    Display display = getShell().getDisplay();
    ITimeInterval interval = paramsPanel.interval();
    try {
      IList<IDtoCompletedCommand> queryCommands = queryEngine.query( interval, allCommandsParams() );
      LoggerUtils.defaultLogger().info( "queryAllCommands(): commandProvider.setItems( commands ). commands count = %d",
          queryCommands.size() );
      if( queryCommands.size() > COMMAND_PANEL_COUNT_LIMIT ) {
        IList<IDtoCompletedCommand> newCommands = queryCommands.fetch( 0, COMMAND_PANEL_COUNT_LIMIT );
        ITimeInterval newInterval = new TimeInterval( newCommands.first().timestamp(), newCommands.last().timestamp() );
        String warn = String.format( STR_COMMAND_LIMIT, queryCommands.size(), COMMAND_PANEL_COUNT_LIMIT, newInterval );
        LoggerUtils.defaultLogger().warning( warn );
        TsDialogUtils.warn( getShell(), warn );
        queryCommands = newCommands;
      }
      IList<IDtoCompletedCommand> commands = queryCommands;
      display.syncExec( () -> commandProvider.items().setAll( commands ) );
      display.syncExec( () -> panel.refresh() );
    }
    catch( Throwable e ) {
      String error = (e.getCause() != null ? e.getCause().getMessage() : e.getMessage());
      TsDialogUtils.error( getShell(), error );
      LoggerUtils.defaultLogger().error( e );
    }
  }

  @SuppressWarnings( { "nls", "boxing" } )
  private void querySelectedCommands() {
    Display display = getShell().getDisplay();
    ITimeInterval interval = paramsPanel.interval();
    IList<ConcerningEventsItem> selectedEvents = ((ConcerningEventsParams)paramsPanel.selectedParams()).eventItems();
    try {
      ConcerningEventsParams selEvents = new ConcerningEventsParams();
      for( ConcerningEventsItem item : selectedEvents ) {
        if( item.eventIds().isEmpty() || item.strids().isEmpty() ) {
          continue;
        }
        selEvents.addItem( item );
      }
      IList<IDtoCompletedCommand> queryCommands = queryEngine.query( interval, selEvents );
      LoggerUtils.defaultLogger().info(
          "querySelectedCommands(): commandProvider.setItems( commands ). commands count = %d", queryCommands.size() );
      if( queryCommands.size() > COMMAND_PANEL_COUNT_LIMIT ) {
        IList<IDtoCompletedCommand> newCommands = queryCommands.fetch( 0, COMMAND_PANEL_COUNT_LIMIT );
        ITimeInterval newInterval = new TimeInterval( newCommands.first().timestamp(), newCommands.last().timestamp() );
        String warn = String.format( STR_COMMAND_LIMIT, queryCommands.size(), COMMAND_PANEL_COUNT_LIMIT, newInterval );
        LoggerUtils.defaultLogger().warning( warn );
        TsDialogUtils.warn( getShell(), warn );
        queryCommands = newCommands;
      }
      IList<IDtoCompletedCommand> commands = queryCommands;
      display.syncExec( () -> commandProvider.items().setAll( commands ) );
      display.syncExec( () -> panel.refresh() );
    }
    catch( Throwable e ) {
      String error = (e.getCause() != null ? e.getCause().getMessage() : e.getMessage());
      TsDialogUtils.error( getShell(), error );
      LoggerUtils.defaultLogger().error( e );
    }
  }

  /**
   * @return параметры запроса на все события
   */
  IConcerningEventsParams allCommandsParams() {
    ConcerningEventsParams retVal = new ConcerningEventsParams();
    for( ISkClassInfo classInfo : listNeededClasses() ) {
      // dima 10.11.25 оставляем только классы у которых нет наследников
      if( classInfo.listSubclasses( false, false ).isEmpty() ) {
        IStringListEdit cmdsIds = new StringArrayList();

        for( IDtoCmdInfo cmd : classInfo.cmds().list() ) {
          cmdsIds.add( cmd.id() );
        }

        ConcerningEventsItem evItem = new ConcerningEventsItem( classInfo.id(), cmdsIds, IStringList.EMPTY );
        retVal.addItem( evItem );
      }
    }

    return retVal;
  }

  @SuppressWarnings( "unchecked" )
  protected IStridablesList<ISkClassInfo> listNeededClasses() {
    ILibClassInfoesTreeModel<IDtoCmdInfo> classModel =
        (ILibClassInfoesTreeModel<IDtoCmdInfo>)tsContext().get( FILTER_CLASSES_TREE_MODEL_LIB );
    return classModel.getRootClasses();
  }

  private void printCommands() {
    try {
      ISkConnectionSupplier connectionSup = eclipseContext().get( ISkConnectionSupplier.class );
      ISkConnection connection = connectionSup.defConn();
      CommandM5Model printCommandsModel = new CommandM5Model( connection, tsContext(), true );

      m5().initTemporaryModel( printCommandsModel );

      ITsGuiContext printContext = new TsGuiContext( tsContext() );

      long startTime = paramsPanel.interval().startTime();
      long endTime = paramsPanel.interval().endTime();

      String title = String.format( PRINT_COMMAND_LIST_TITLE_FORMAT, timestampFormat.format( new Date( startTime ) ),
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
          ReportGenerator.generateJasperPrint( printContext, printCommandsModel, commandProvider );
      JasperReportDialog.showPrint( printContext, jasperPrint );
    }
    catch( Exception ex ) {
      LoggerUtils.errorLogger().error( ex );
      TsDialogUtils.error( getShell(), ex );
    }

  }
}
