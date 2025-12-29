package org.toxsoft.skf.journals;

import static org.toxsoft.skf.alarms.lib.ISkAlarmConstants.*;
import static org.toxsoft.skf.journals.ISkJournalsConstants.*;
import static org.toxsoft.skf.journals.e4.uiparts.engine.ISkResources.*;

import org.eclipse.e4.core.contexts.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.quant.*;
import org.toxsoft.core.tslib.gw.gwid.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.skf.alarms.lib.*;
import org.toxsoft.skf.journals.e4.uiparts.devel.*;
import org.toxsoft.uskat.classes.*;
import org.toxsoft.uskat.core.api.*;
import org.toxsoft.uskat.core.api.cmdserv.*;
import org.toxsoft.uskat.core.api.evserv.*;
import org.toxsoft.uskat.core.api.objserv.*;
import org.toxsoft.uskat.core.api.sysdescr.*;
import org.toxsoft.uskat.core.api.sysdescr.dto.*;
import org.toxsoft.uskat.core.api.users.*;
import org.toxsoft.uskat.core.connection.*;
import org.toxsoft.uskat.core.devapi.*;
import org.toxsoft.uskat.core.gui.conn.*;
import org.toxsoft.uskat.core.impl.*;

/**
 * The library quant.
 *
 * @author max
 */
public class QuantJournalsMws
    extends AbstractQuant
    implements ISkCoreExternalHandler {

  /**
   * Constructor.
   */
  public QuantJournalsMws() {
    super( QuantJournalsMws.class.getSimpleName() );
    SkCoreUtils.registerCoreApiHandler( this );
  }

  @Override
  protected void doInitApp( IEclipseContext aAppContext ) {
    ISkModJournalEventFormattersRegistry eventFormattersRegistry =
        aAppContext.containsKey( ISkModJournalEventFormattersRegistry.class )
            ? aAppContext.get( ISkModJournalEventFormattersRegistry.class )
            : new DefaultMwsModJournalEventFormattersRegistry();

    aAppContext.set( ISkModJournalEventFormattersRegistry.class, eventFormattersRegistry );

    if( !aAppContext.containsKey( ISkModJournalCommandFormattersRegistry.class ) ) {
      aAppContext.set( ISkModJournalCommandFormattersRegistry.class,
          new DefaultMwsModJournalCommandFormattersRegistry() );
    }

    AlarmJournalEventFormatter alarmJournalEventFormatter = new AlarmJournalEventFormatter();
    Gwid evGwid = Gwid.createEvent( CLSID_ALARM, EVID_ACKNOWLEDGE );
    eventFormattersRegistry.registerFomatter( evGwid, alarmJournalEventFormatter );

    // получаем регистр форматеров команд и регистрируем самые распространенные команды
    ISkModJournalCommandFormattersRegistry cmdFormatterRegistry =
        aAppContext.get( ISkModJournalCommandFormattersRegistry.class );
    AlarmJournalCmdFormatter alarmJournalCmdFormatter = new AlarmJournalCmdFormatter();
    Gwid cmdGwid = Gwid.createCmd( CLSID_ALARM, CMDID_ACKNOWLEDGE );
    cmdFormatterRegistry.registerFomatter( cmdGwid, alarmJournalCmdFormatter );

    // Обработка событий подключение/отключение к серверу
    SessionEventFormatter sessionEventFormatter = new SessionEventFormatter();
    evGwid = Gwid.createEvent( ISkServer.CLASS_ID, ISkServer.EVID_SESSION_CREATED );
    eventFormattersRegistry.registerFomatter( evGwid, sessionEventFormatter );
    evGwid = Gwid.createEvent( ISkServer.CLASS_ID, ISkServer.EVID_SESSION_RESTORED );
    eventFormattersRegistry.registerFomatter( evGwid, sessionEventFormatter );
    evGwid = Gwid.createEvent( ISkServer.CLASS_ID, ISkServer.EVID_SESSION_BREAKED );
    eventFormattersRegistry.registerFomatter( evGwid, sessionEventFormatter );
    evGwid = Gwid.createEvent( ISkServer.CLASS_ID, ISkServer.EVID_SESSION_CLOSED );
    eventFormattersRegistry.registerFomatter( evGwid, sessionEventFormatter );
    evGwid = Gwid.createEvent( ISkServer.CLASS_ID, ISkServer.EVID_LOGIN_FAILED );
    eventFormattersRegistry.registerFomatter( evGwid, sessionEventFormatter );

  }

  @Override
  protected void doInitWin( IEclipseContext aWinContext ) {
    // nop
  }

  @Override
  public void processSkCoreInitialization( IDevCoreApi aCoreApi ) {
    // register abilities
    aCoreApi.userService().abilityManager().defineKind( ABKIND_JOURNALS );
    aCoreApi.userService().abilityManager().defineAbility( ABILITY_ACCESS_JOURNALS );
    aCoreApi.userService().abilityManager().defineAbility( ABILITY_JOURNALS_EVENTS );
    aCoreApi.userService().abilityManager().defineAbility( ABILITY_JOURNALS_COMMANDS );
    aCoreApi.userService().abilityManager().defineAbility( ABILITY_JOURNALS_ALARMS );
  }

  /**
   * Класс осуществляющий форматирование команды подтверждения алармов
   *
   * @author dima
   */
  static class AlarmJournalCmdFormatter
      implements ISkModJournalCommandFormatter {

    @Override
    public String formatExecuterText( IDtoCompletedCommand aEntity, ITsGuiContext aContext ) {
      ISkConnectionSupplier conSupp = aContext.get( ISkConnectionSupplier.class );
      ISkConnection conn = conSupp.defConn();
      ISkAlarm alarmObj = (ISkAlarm)conn.coreApi().objService().find( aEntity.cmd().cmdGwid().skid() );
      return String.format( VALIDATION_FORMAT_STR, alarmObj.severity().nmName(), alarmObj.readableName(),
          alarmObj.strid() );
    }

    @Override
    public String formatVisualName( IDtoCompletedCommand aEntity, ITsGuiContext aContext ) {
      // не нужно тут дублирование автора, он рядом в колонке автор
      // ISkConnectionSupplier conSupp = aContext.get( ISkConnectionSupplier.class );
      // ISkConnection conn = conSupp.defConn();

      // формируем короткое описание команды
      StringBuilder sb = new StringBuilder();
      // // получаем автора и отображаем его нормальное имя
      // Gwid authorGwid = aEntity.cmd().argValues().getValobj( CMDARGID_ACK_AUTHOR_GWID );
      // ISkUser author = conn.coreApi().userService().findUser( authorGwid.strid() );
      String authorComment = aEntity.cmd().argValues().getStr( EVPRMID_ACK_COMMNET );
      sb.append( STR_ACKNOWLEDGE_COMMENT );
      sb.append( ": " ); //$NON-NLS-1$
      sb.append( authorComment );
      return sb.toString();
    }
  }

  /**
   * Класс осуществляющий форматирование события алармов
   *
   * @author dima
   */
  static class AlarmJournalEventFormatter
      implements ISkModJournalEventFormatter {

    private static IDtoEventInfo getEvInfo( ISkConnection aConn, SkEvent aEvent ) {
      // Получаем его класс
      ISkClassInfo skClass = aConn.coreApi().sysdescr().findClassInfo( aEvent.eventGwid().classId() );
      // Описание события
      IDtoEventInfo evInfo = skClass.events().list().findByKey( aEvent.eventGwid().propId() );
      return evInfo;
    }

    /**
     * Формирует краткий однострочный текст для вывода в строке таблицы.
     *
     * @param aEvent {@link SkEvent} - отображаемое событие
     * @param aContext {@link ITsGuiContext} - контекст приложения
     * @return String - сформированный текст, не бывает <code>null</code>
     */
    @Override
    public String formatShortText( SkEvent aEvent, ITsGuiContext aContext ) {
      ISkConnectionSupplier conSupp = aContext.get( ISkConnectionSupplier.class );
      ISkConnection conn = conSupp.defConn();

      // формируем короткое описание события
      StringBuilder sb = new StringBuilder();
      // получаем автора и отображаем его нормальное имя
      Gwid userGwid = aEvent.paramValues().getValobj( EVPRMID_ACK_AUTHOR );
      ISkUser user = conn.coreApi().userService().findUser( userGwid.strid() );
      String userComment = aEvent.paramValues().getStr( EVPRMID_ACK_COMMNET );
      sb.append( user.nmName() );
      sb.append( " : " ); //$NON-NLS-1$
      sb.append( userComment );
      return sb.toString();
    }

    /**
     * Формирует многострочный текст для вывода в поле детального просмотра таблицы.
     *
     * @param aEvent {@link SkEvent} - отображаемое событие
     * @param aContext {@link ITsGuiContext} - контекст приложения
     * @return String - сформированный текст, не бывает <code>null</code>
     */
    @SuppressWarnings( "nls" )
    @Override
    public String formatLongText( SkEvent aEvent, ITsGuiContext aContext ) {
      // формируем короткое описание события
      StringBuilder sb = new StringBuilder();

      ISkConnectionSupplier conSupp = aContext.get( ISkConnectionSupplier.class );
      ISkConnection conn = conSupp.defConn();
      // получаем автора и отображаем его нормальное имя
      Gwid userGwid = aEvent.paramValues().getValobj( EVPRMID_ACK_AUTHOR );
      ISkUser user = conn.coreApi().userService().findUser( userGwid.strid() );
      String userComment = aEvent.paramValues().getStr( EVPRMID_ACK_COMMNET );
      IDtoEventInfo evInfo = getEvInfo( conn, aEvent );
      // Получаем объект параметра
      ISkObject attrObject = conn.coreApi().objService().find( aEvent.eventGwid().skid() );

      // изменилось значение параметра состояния резервирования
      sb.append( String.format( EV_NAME_OBJ_PARAMS_FMT_STR, evInfo.nmName(), attrObject.description() ) );
      sb.append( " • " + STR_ACKNOWLEDGE_AUTHOR + " : " );
      sb.append( user.nmName() );
      sb.append( "\n" );
      sb.append( " • " + STR_ACKNOWLEDGE_COMMENT + " : " );
      sb.append( userComment );
      return sb.toString();
    }
  }

  /**
   * Класс осуществляющий форматирование событий создания/отключения сессии
   *
   * @author dima
   */
  static class SessionEventFormatter
      implements ISkModJournalEventFormatter {

    @Override
    public String formatShortText( SkEvent aEvent, ITsGuiContext aContext ) {
      ISkConnectionSupplier conSupp = aContext.get( ISkConnectionSupplier.class );
      ISkConnection conn = conSupp.defConn();

      // описание пользователя
      String login = aEvent.paramValues().findByKey( ISkServer.EVPID_LOGIN ).asString();
      // по логину ищем пользователя
      ISkUser userObj = conn.coreApi().userService().findUser( login );
      // IP address
      String ipAddress = aEvent.paramValues().findByKey( ISkServer.EVPID_IP ).asString();
      // сессия
      String sessionStr = TsLibUtils.EMPTY_STRING;
      if( aEvent.paramValues().hasKey( ISkServer.EVPID_SESSION_ID ) ) {
        sessionStr = aEvent.paramValues().findByKey( ISkServer.EVPID_SESSION_ID ).asValobj().toString();
      }
      // формируем описание события
      StringBuilder sb = new StringBuilder();
      sb.append( String.format( USER_IP_FMT_STR, userObj.nmName(), ipAddress ) );
      return sb.toString();
    }

    @Override
    public String formatLongText( SkEvent aEvent, ITsGuiContext aContext ) {
      // формируем короткое описание события
      StringBuilder sb = new StringBuilder();

      ISkConnectionSupplier conSupp = aContext.get( ISkConnectionSupplier.class );
      ISkConnection conn = conSupp.defConn();
      IDtoEventInfo evInfo = getEvInfo( conn, aEvent );
      // описание пользователя
      String login = aEvent.paramValues().findByKey( ISkServer.EVPID_LOGIN ).asString();
      // по логину ищем пользователя
      ISkUser userObj = conn.coreApi().userService().findUser( login );
      // IP address
      String ipAddress = aEvent.paramValues().findByKey( ISkServer.EVPID_IP ).asString();
      // сессия
      String sessionStr = TsLibUtils.EMPTY_STRING;
      if( aEvent.paramValues().hasKey( ISkServer.EVPID_SESSION_ID ) ) {
        sessionStr = aEvent.paramValues().findByKey( ISkServer.EVPID_SESSION_ID ).asValobj().toString();
      }

      // формируем описание события
      sb.append( String.format( EV_NAME_OBJ_PARAMS_FMT_STR, evInfo.nmName(), evInfo.description() ) );
      sb.append( " • " + STR_SESSION_USER + " : " );
      sb.append( userObj.nmName() );
      sb.append( "\n" );
      sb.append( " • " + STR_IP_ADDRESS + " : " );
      sb.append( ipAddress );
      if( sessionStr.length() > 0 ) {
        sb.append( " • " + STR_SESSION + " : " );
        sb.append( sessionStr );
      }
      return sb.toString();

    }

    private static IDtoEventInfo getEvInfo( ISkConnection aConn, SkEvent aEvent ) {
      // Получаем его класс
      ISkClassInfo skClass = aConn.coreApi().sysdescr().findClassInfo( aEvent.eventGwid().classId() );
      // Описание события
      IDtoEventInfo evInfo = skClass.events().list().findByKey( aEvent.eventGwid().propId() );
      return evInfo;
    }

  }

}
