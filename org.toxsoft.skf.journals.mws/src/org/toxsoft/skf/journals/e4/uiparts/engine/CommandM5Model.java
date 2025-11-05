package org.toxsoft.skf.journals.e4.uiparts.engine;

import static org.toxsoft.core.tsgui.m5.IM5Constants.*;
import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.skf.journals.e4.uiparts.engine.ISkResources.*;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.core.tsgui.valed.api.*;
import org.toxsoft.core.tsgui.valed.controls.basic.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.skf.journals.e4.uiparts.devel.*;
import org.toxsoft.uskat.core.api.cmdserv.*;
import org.toxsoft.uskat.core.api.objserv.*;
import org.toxsoft.uskat.core.api.sysdescr.*;
import org.toxsoft.uskat.core.api.sysdescr.dto.*;
import org.toxsoft.uskat.core.connection.*;

/**
 * Модель отображения команд.
 *
 * @author max
 * @author dima
 */
public class CommandM5Model
    extends M5Model<IDtoCompletedCommand> {

  /**
   * Регистр форматов отображения команд
   */
  private ISkModJournalCommandFormattersRegistry formatterRegistry;

  /**
   * Соединение с сервером
   */
  private ISkConnection conn;

  /**
   * Контекст приложения
   */
  private ITsGuiContext context;

  private static final String VIS_NAME_FORMAT  = "%s [%s]";               //$NON-NLS-1$
  private static final String VIS_DESCR_FORMAT = " %s \n Gwid: %s \n %s"; //$NON-NLS-1$

  private static final String AUTHOR_FORMAT = "%s [%s]"; //$NON-NLS-1$

  private static final String EXECUTER_FORMAT = "%s [%s]"; //$NON-NLS-1$

  /**
   * Идентификатор модели.
   */
  public static final String MODEL_ID = "ts.sk.journal.ISkCommand"; //$NON-NLS-1$

  /**
   * Model for print.
   */
  public static final String PRINT_MODEL_ID = MODEL_ID + ".print"; //$NON-NLS-1$

  /**
   * Идентификатор поля {@link #VIS_NAME}.
   */
  public static final String FID_VIS_NAME = "VisName"; //$NON-NLS-1$

  /**
   * Идентификатор поля {@link #TIME}.
   */
  private static final String FID_TIME = "ts.Time"; //$NON-NLS-1$

  /**
   * Идентификатор поля {@link #AUTHOR}.
   */
  private static final String FID_AUTHOR = "ts.Author"; //$NON-NLS-1$

  /**
   * Идентификатор поля {@link #EXECUTER}.
   */
  private static final String FID_EXECUTER = "ts.Executer"; //$NON-NLS-1$

  /**
   * Наименование команды.
   */
  public final M5AttributeFieldDef<IDtoCompletedCommand> VIS_NAME = new M5AttributeFieldDef<>( FID_VIS_NAME, STRING ) {

    @Override
    protected void doInit() {
      setNameAndDescription( VIS_NAME_COL_NAME, VIS_NAME_COL_DESCR );
      setDefaultValue( IAtomicValue.NULL );
      setFlags( M5FF_COLUMN | M5FF_READ_ONLY );

    }

    @Override
    protected IAtomicValue doGetFieldValue( IDtoCompletedCommand aEntity ) {
      ISkObject srcObj = conn.coreApi().objService().find( aEntity.cmd().cmdGwid().skid() );
      ISkModJournalCommandFormatter formatter =
          formatterRegistry.find( srcObj.classId(), aEntity.cmd().cmdGwid().propId() );
      // dima 30.10.25 if there is no formatter for that command, then process it by itself
      if( formatter == null ) {
        return getDfltVisName( aEntity );
      }

      return avStr( formatter.formatVisualName( aEntity, context ) );
    }

    private IAtomicValue getDfltVisName( IDtoCompletedCommand aEntity ) {
      // Получаем объект команды
      ISkObject skObject = conn.coreApi().objService().find( aEntity.cmd().cmdGwid().skid() );
      // Получаем его класс
      ISkClassInfo skClass = conn.coreApi().sysdescr().findClassInfo( skObject.classId() );
      // Описание команды
      IDtoCmdInfo cmdInfo = skClass.cmds().list().findByKey( aEntity.cmd().cmdGwid().propId() );

      return avStr( String.format( VIS_NAME_FORMAT, cmdInfo.nmName(), aEntity.cmd().cmdGwid().strid() ) );
    }
  };

  /**
   * Описание команды.
   */
  public final M5AttributeFieldDef<IDtoCompletedCommand> VIS_DESCRIPTION =
      new M5AttributeFieldDef<>( FID_DESCRIPTION, STRING ) {

        @Override
        protected void doInit() {
          // display in details panel, no need name & description
          setNameAndDescription( DESCRIPTION_STR, TsLibUtils.EMPTY_STRING );
          ValedStringText.OPDEF_IS_MULTI_LINE.setValue( params(), AV_TRUE );
          params().setInt( IValedControlConstants.OPDEF_VERTICAL_SPAN, 4 );
          setDefaultValue( IAtomicValue.NULL );
          setFlags( M5FF_DETAIL | M5FF_READ_ONLY );

        }

        @Override
        protected IAtomicValue doGetFieldValue( IDtoCompletedCommand aEntity ) {
          // Получаем объект команды
          ISkObject skObject = conn.coreApi().objService().find( aEntity.cmd().cmdGwid().skid() );
          // Получаем его класс
          ISkClassInfo skClass = conn.coreApi().sysdescr().findClassInfo( skObject.classId() );
          // Описание команды
          IDtoCmdInfo cmdInfo = skClass.cmds().list().findByKey( aEntity.cmd().cmdGwid().propId() );
          // old version
          // return avStr(
          // String.format( VIS_DESCR_FORMAT, cmdInfo.description(), aEntity.cmd().argValues().toString() ) );
          // new version
          return avStr( String.format( VIS_DESCR_FORMAT, cmdInfo.description(),
              aEntity.cmd().cmdGwid().canonicalString(), argsOptSet2ReadableString( aEntity.cmd().argValues() ) ) );
        }
      };

  @SuppressWarnings( "nls" )
  private static String argsOptSet2ReadableString( IOptionSet aCmdArgs ) {
    if( aCmdArgs.isEmpty() ) {
      return COMMAND_HAS_NO_ARGUMENTS;
    }
    StringBuilder sb = new StringBuilder( COMMAND_ARGUMENTS );
    for( String argKey : aCmdArgs.keys() ) {
      String argValue = aCmdArgs.findByKey( argKey ).toString();
      sb.append( " • " + argKey + " = " + argValue + "\n" );
    }
    return sb.toString();
  }

  /**
   * Время команды
   */
  public final M5AttributeFieldDef<IDtoCompletedCommand> TIME = new M5AttributeFieldDef<>( FID_TIME, TIMESTAMP ) {

    @Override
    protected void doInit() {
      setNameAndDescription( TIME_COL_NAME, TIME_COL_DESCR );
      setDefaultValue( IAtomicValue.NULL );
      setFlags( M5FF_COLUMN | M5FF_READ_ONLY );
    }

    @Override
    protected IAtomicValue doGetFieldValue( IDtoCompletedCommand aEntity ) {
      Long date = Long.valueOf( aEntity.timestamp() );
      return AvUtils.avStr( String.format( "%tF      %tT.%tL", date, date, date ) ); //$NON-NLS-1$
      // return avTimestamp( aEntity.timestamp() );
    }
  };

  /**
   * Автор команды
   */
  public final M5AttributeFieldDef<IDtoCompletedCommand> AUTHOR = new M5AttributeFieldDef<>( FID_AUTHOR, STRING ) {

    @Override
    protected void doInit() {
      setNameAndDescription( AUTHOR_COL_NAME, AUTHOR_COL_DESCR );
      setDefaultValue( IAtomicValue.NULL );
      setFlags( M5FF_COLUMN | M5FF_READ_ONLY );
    }

    @Override
    protected IAtomicValue doGetFieldValue( IDtoCompletedCommand aEntity ) {
      ISkObject srcObj = conn.coreApi().objService().find( aEntity.cmd().authorSkid() );

      return avStr( String.format( AUTHOR_FORMAT, srcObj.readableName(), srcObj.strid() ) );
    }
  };

  /**
   * Исполнитель команды
   */
  public final M5AttributeFieldDef<IDtoCompletedCommand> EXECUTER = new M5AttributeFieldDef<>( FID_EXECUTER, STRING ) {

    @Override
    protected void doInit() {
      setNameAndDescription( CMD_EXEC_COL_NAME, CMD_EXEC_COL_DESCR );
      setDefaultValue( IAtomicValue.NULL );
      setFlags( M5FF_COLUMN | M5FF_READ_ONLY );
    }

    @Override
    protected IAtomicValue doGetFieldValue( IDtoCompletedCommand aEntity ) {
      ISkObject srcObj = conn.coreApi().objService().find( aEntity.cmd().cmdGwid().skid() );
      ISkModJournalCommandFormatter formatter =
          formatterRegistry.find( srcObj.classId(), aEntity.cmd().cmdGwid().propId() );
      // dima 30.10.25 if there is no formatter for that command, then process it by itself
      if( formatter == null ) {
        return getDfltExecuterText( aEntity );
      }

      return avStr( formatter.formatExecuterText( aEntity, context ) );
    }

    private IAtomicValue getDfltExecuterText( IDtoCompletedCommand aEntity ) {
      ISkObject srcObj = conn.coreApi().objService().find( aEntity.cmd().cmdGwid().skid() );
      return avStr( String.format( EXECUTER_FORMAT, srcObj.readableName(), srcObj.strid() ) );
    }
  };

  /**
   * Конструктор.
   *
   * @param aConn - соединение с сервером.
   * @param aModelContext - app context
   * @param aForPrint - attribute signs the model for prints (if true).
   */
  public CommandM5Model( ISkConnection aConn, ITsGuiContext aModelContext, boolean aForPrint ) {
    super( aForPrint ? PRINT_MODEL_ID : MODEL_ID, IDtoCompletedCommand.class );
    conn = aConn;
    context = aModelContext;
    formatterRegistry = context.get( ISkModJournalCommandFormattersRegistry.class );

    setNameAndDescription( CMDS_LIST_TABLE_NAME, CMDS_LIST_TABLE_DESCR );
    IListEdit<IM5FieldDef<IDtoCompletedCommand, ?>> fDefs = new ElemArrayList<>();
    fDefs.add( TIME );
    fDefs.add( EXECUTER );
    fDefs.add( VIS_NAME );
    fDefs.add( AUTHOR );
    fDefs.add( VIS_DESCRIPTION );

    addFieldDefs( fDefs );
  }
}
