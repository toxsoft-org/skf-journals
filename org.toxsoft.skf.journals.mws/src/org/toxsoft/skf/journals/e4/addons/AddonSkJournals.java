package org.toxsoft.skf.journals.e4.addons;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.toxsoft.core.tsgui.bricks.ctx.ITsGuiContext;
import org.toxsoft.core.tsgui.mws.bases.MwsAbstractAddon;
import org.toxsoft.core.tslib.av.EAtomicType;
import org.toxsoft.core.tslib.av.IAtomicValue;
import org.toxsoft.core.tslib.av.opset.IOptionSet;
import org.toxsoft.core.tslib.gw.gwid.Gwid;
import org.toxsoft.core.tslib.utils.TsLibUtils;
import org.toxsoft.core.tslib.utils.logs.impl.LoggerUtils;
import org.toxsoft.skf.journals.Activator;
import org.toxsoft.skf.journals.ISkJournalsConstants;
import org.toxsoft.skf.journals.e4.uiparts.devel.*;
import org.toxsoft.uskat.core.api.evserv.SkEvent;
import org.toxsoft.uskat.core.api.objserv.ISkObject;
import org.toxsoft.uskat.core.api.sysdescr.ISkClassInfo;
import org.toxsoft.uskat.core.api.sysdescr.dto.IDtoEventInfo;
import org.toxsoft.uskat.core.connection.ISkConnection;
import org.toxsoft.uskat.core.gui.conn.ISkConnectionSupplier;

/**
 * Plugin adoon.
 *
 * @author hazard157
 */
public class AddonSkJournals
    extends MwsAbstractAddon {

  // ------------------------------------------------------------------------------------------------
  /**
   * Класс: система
   */
  private static final String CLS_SYSTEM = "sk.System";

  /**
   * Событие: потеря связи
   */
  private static final String EVENT_LOGIN_FAILED = "LoginFailed";

  /**
   * Событие: создана сессия
   */
  private static final String EVENT_SESSION_CREATED = "SessionCreated";

  /**
   * Событие: сессия закрыта
   */
  private static final String EVENT_SESSION_CLOSED = "SessionClosed";

  /**
   * Событие: потеря связи
   */
  private static final String EVENT_SESSION_BREAKED = "SessionBreaked";

  /**
   * Событие: восстановление связи
   */
  private static final String EVENT_SESSION_RESTORED = "SessionRestored";

  /**
   * Событие: изменение системного описания
   */
  private static final String EVENT_SESSION_CHANGED = "SysdescrChanged";

  /**
   * Параметр события: значение было
   */
  private static final String EVPARAM_OLD_VAL = "oldVal";

  /**
   * Параметр события: значение стало
   */
  private static final String EVPARAM_NEW_VAL = "newVal";

  /**
   * Параметр события: булевое текущее значение
   */
  private static final String EVPARAM_ON_VAL = "on";

  private static final String ERROR_FORMING_MESSAGE_STR = "Error forming message. Couldnt find proper param of event";

  /**
   * Constructor.
   */
  public AddonSkJournals() {
    super( Activator.PLUGIN_ID );
  }

  // ------------------------------------------------------------------------------------
  // MwsAbstractAddon
  //

  @Override
  protected void initApp( IEclipseContext aAppContext ) {
    ISkModJournalEventFormattersRegistry eventFormattersRegistry =
        aAppContext.containsKey( ISkModJournalEventFormattersRegistry.class )
            ? aAppContext.get( ISkModJournalEventFormattersRegistry.class )
            : new DefaultMwsModJournalEventFormattersRegistry();

    aAppContext.set( ISkModJournalEventFormattersRegistry.class, eventFormattersRegistry );

    SessionChangeJournalEventFormatter sessionChangeJournalEventFormatter = new SessionChangeJournalEventFormatter();

    Gwid evGwid = Gwid.createEvent( CLS_SYSTEM, EVENT_SESSION_CREATED );
    eventFormattersRegistry.registerFomatter( evGwid, sessionChangeJournalEventFormatter );
    evGwid = Gwid.createEvent( CLS_SYSTEM, EVENT_SESSION_CREATED );
    eventFormattersRegistry.registerFomatter( evGwid, sessionChangeJournalEventFormatter );
    evGwid = Gwid.createEvent( CLS_SYSTEM, EVENT_SESSION_CLOSED );
    eventFormattersRegistry.registerFomatter( evGwid, sessionChangeJournalEventFormatter );
    evGwid = Gwid.createEvent( CLS_SYSTEM, EVENT_SESSION_BREAKED );
    eventFormattersRegistry.registerFomatter( evGwid, sessionChangeJournalEventFormatter );
    evGwid = Gwid.createEvent( CLS_SYSTEM, EVENT_SESSION_RESTORED );
    eventFormattersRegistry.registerFomatter( evGwid, sessionChangeJournalEventFormatter );
    evGwid = Gwid.createEvent( CLS_SYSTEM, EVENT_LOGIN_FAILED );
    eventFormattersRegistry.registerFomatter( evGwid, sessionChangeJournalEventFormatter );

    // Далее сгенерено автоматически из файла описания ods
    ValueChangeJournalEventFormatter valueChangeJournalEventFormatter = new ValueChangeJournalEventFormatter();

    try {

      evGwid = Gwid.createEvent( "mcc.AnalogInput", "evtChannelAddress" );
      eventFormattersRegistry.registerFomatter( evGwid, valueChangeJournalEventFormatter );
      evGwid = Gwid.createEvent( "mcc.AnalogInput", "evtFilterConst" );
      eventFormattersRegistry.registerFomatter( evGwid, valueChangeJournalEventFormatter );
      evGwid = Gwid.createEvent( "mcc.AnalogInput", "evtX0" );
      eventFormattersRegistry.registerFomatter( evGwid, valueChangeJournalEventFormatter );
      evGwid = Gwid.createEvent( "mcc.AnalogInput", "evtY0" );
      eventFormattersRegistry.registerFomatter( evGwid, valueChangeJournalEventFormatter );
      evGwid = Gwid.createEvent( "mcc.AnalogInput", "evtX1" );
      eventFormattersRegistry.registerFomatter( evGwid, valueChangeJournalEventFormatter );
      evGwid = Gwid.createEvent( "mcc.AnalogInput", "evtY1" );
      eventFormattersRegistry.registerFomatter( evGwid, valueChangeJournalEventFormatter );
      evGwid = Gwid.createEvent( "mcc.AnalogInput", "evtSetPoint1" );
      eventFormattersRegistry.registerFomatter( evGwid, valueChangeJournalEventFormatter );
      evGwid = Gwid.createEvent( "mcc.AnalogInput", "evtSetPoint2" );
      eventFormattersRegistry.registerFomatter( evGwid, valueChangeJournalEventFormatter );
      evGwid = Gwid.createEvent( "mcc.AnalogInput", "evtSetPoint3" );
      eventFormattersRegistry.registerFomatter( evGwid, valueChangeJournalEventFormatter );
      evGwid = Gwid.createEvent( "mcc.AnalogInput", "evtSetPoint4" );
      eventFormattersRegistry.registerFomatter( evGwid, valueChangeJournalEventFormatter );
      evGwid = Gwid.createEvent( "mcc.AnalogInput", "evtSetPoint4generation" );
      eventFormattersRegistry.registerFomatter( evGwid,
          new TriggeredJournalEventFormatter( "установлено", "сброшено" ) );
      evGwid = Gwid.createEvent( "mcc.AnalogInput", "evtSetPoint4indication" );
      eventFormattersRegistry.registerFomatter( evGwid,
          new TriggeredJournalEventFormatter( "установлено", "сброшено" ) );
      evGwid = Gwid.createEvent( "mcc.AnalogInput", "evtSetPoint3generation" );
      eventFormattersRegistry.registerFomatter( evGwid,
          new TriggeredJournalEventFormatter( "установлено", "сброшено" ) );
      evGwid = Gwid.createEvent( "mcc.AnalogInput", "evtSetPoint3indication" );
      eventFormattersRegistry.registerFomatter( evGwid,
          new TriggeredJournalEventFormatter( "установлено", "сброшено" ) );
      evGwid = Gwid.createEvent( "mcc.AnalogInput", "evtSetPoint2generation" );
      eventFormattersRegistry.registerFomatter( evGwid,
          new TriggeredJournalEventFormatter( "установлено", "сброшено" ) );
      evGwid = Gwid.createEvent( "mcc.AnalogInput", "evtSetPoint2indication" );
      eventFormattersRegistry.registerFomatter( evGwid,
          new TriggeredJournalEventFormatter( "установлено", "сброшено" ) );
      evGwid = Gwid.createEvent( "mcc.AnalogInput", "evtSetPoint1generation" );
      eventFormattersRegistry.registerFomatter( evGwid,
          new TriggeredJournalEventFormatter( "установлено", "сброшено" ) );
      evGwid = Gwid.createEvent( "mcc.AnalogInput", "evtSetPoint1indication" );
      eventFormattersRegistry.registerFomatter( evGwid,
          new TriggeredJournalEventFormatter( "установлено", "сброшено" ) );
      evGwid = Gwid.createEvent( "mcc.AnalogInput", "evtEventTime" );
      eventFormattersRegistry.registerFomatter( evGwid, valueChangeJournalEventFormatter );
      evGwid = Gwid.createEvent( "mcc.AnalogInput", "evtAlarmMinGeneration" );
      eventFormattersRegistry.registerFomatter( evGwid, new TriggeredJournalEventFormatter( "", true ) );
      evGwid = Gwid.createEvent( "mcc.AnalogInput", "evtAlarmMinIndication" );
      eventFormattersRegistry.registerFomatter( evGwid, new TriggeredJournalEventFormatter( "", true ) );
      evGwid = Gwid.createEvent( "mcc.AnalogInput", "evtWarningMinGeneration" );
      eventFormattersRegistry.registerFomatter( evGwid, new TriggeredJournalEventFormatter( "", true ) );
      evGwid = Gwid.createEvent( "mcc.AnalogInput", "evtWarningMinIndication" );
      eventFormattersRegistry.registerFomatter( evGwid, new TriggeredJournalEventFormatter( "", true ) );
      evGwid = Gwid.createEvent( "mcc.AnalogInput", "evtWarningMaxGeneration" );
      eventFormattersRegistry.registerFomatter( evGwid, new TriggeredJournalEventFormatter( "", true ) );
      evGwid = Gwid.createEvent( "mcc.AnalogInput", "evtWarningMaxIndication" );
      eventFormattersRegistry.registerFomatter( evGwid, new TriggeredJournalEventFormatter( "", true ) );
      evGwid = Gwid.createEvent( "mcc.AnalogInput", "evtAlarmMaxGeneration" );
      eventFormattersRegistry.registerFomatter( evGwid, new TriggeredJournalEventFormatter( "", true ) );
      evGwid = Gwid.createEvent( "mcc.AnalogInput", "evtAlarmMaxIndication" );
      eventFormattersRegistry.registerFomatter( evGwid, new TriggeredJournalEventFormatter( "", true ) );
      evGwid = Gwid.createEvent( "mcc.AnalogInput", "evtCalibrationWarning" );
      eventFormattersRegistry.registerFomatter( evGwid, new TriggeredJournalEventFormatter( "", true ) );
      evGwid = Gwid.createEvent( "mcc.AnalogInput", "evtCalibrationError" );
      eventFormattersRegistry.registerFomatter( evGwid, new TriggeredJournalEventFormatter( "", true ) );
      evGwid = Gwid.createEvent( "mcc.AnalogInput", "evtImitation" );
      eventFormattersRegistry.registerFomatter( evGwid,
          new TriggeredJournalEventFormatter( "установлено", "сброшено" ) );
      evGwid = Gwid.createEvent( "mcc.AnalogInput", "evtEnblAlarmTreat" );
      eventFormattersRegistry.registerFomatter( evGwid,
          new TriggeredJournalEventFormatter( "установлено", "сброшено" ) );
      evGwid = Gwid.createEvent( "mcc.IrreversibleEngine", "evtAwpStart" );
      eventFormattersRegistry.registerFomatter( evGwid, new TriggeredJournalEventFormatter( "", true ) );
      evGwid = Gwid.createEvent( "mcc.IrreversibleEngine", "evtAwpStop" );
      eventFormattersRegistry.registerFomatter( evGwid, new TriggeredJournalEventFormatter( "", true ) );
      evGwid = Gwid.createEvent( "mcc.IrreversibleEngine", "evtPanelStart" );
      eventFormattersRegistry.registerFomatter( evGwid, new TriggeredJournalEventFormatter( "", true ) );
      evGwid = Gwid.createEvent( "mcc.IrreversibleEngine", "evtPanelStop" );
      eventFormattersRegistry.registerFomatter( evGwid, new TriggeredJournalEventFormatter( "", true ) );
      evGwid = Gwid.createEvent( "mcc.IrreversibleEngine", "evtLocalStart" );
      eventFormattersRegistry.registerFomatter( evGwid, new TriggeredJournalEventFormatter( "", true ) );
      evGwid = Gwid.createEvent( "mcc.IrreversibleEngine", "evtLocalStop" );
      eventFormattersRegistry.registerFomatter( evGwid, new TriggeredJournalEventFormatter( "", true ) );
      evGwid = Gwid.createEvent( "mcc.IrreversibleEngine", "evtControllerStart" );
      eventFormattersRegistry.registerFomatter( evGwid, new TriggeredJournalEventFormatter( "", true ) );
      evGwid = Gwid.createEvent( "mcc.IrreversibleEngine", "evtControllerStop" );
      eventFormattersRegistry.registerFomatter( evGwid, new TriggeredJournalEventFormatter( "", true ) );
      evGwid = Gwid.createEvent( "mcc.IrreversibleEngine", "evtOn" );
      eventFormattersRegistry.registerFomatter( evGwid, new TriggeredJournalEventFormatter( "включен", "отключен" ) );
      evGwid = Gwid.createEvent( "mcc.IrreversibleEngine", "evtPwr" );
      eventFormattersRegistry.registerFomatter( evGwid, new TriggeredJournalEventFormatter( "в норме", "отсуствует" ) );
      evGwid = Gwid.createEvent( "mcc.IrreversibleEngine", "evtAuxTime" );
      eventFormattersRegistry.registerFomatter( evGwid, valueChangeJournalEventFormatter );
      evGwid = Gwid.createEvent( "mcc.IrreversibleEngine", "evtImitation" );
      eventFormattersRegistry.registerFomatter( evGwid,
          new TriggeredJournalEventFormatter( "установлено", "сброшено" ) );
      evGwid = Gwid.createEvent( "mcc.IrreversibleEngine", "evtEnabled" );
      eventFormattersRegistry.registerFomatter( evGwid,
          new TriggeredJournalEventFormatter( "установлено", "сброшено" ) );
      evGwid = Gwid.createEvent( "mcc.IrreversibleEngine", "evtReady" );
      eventFormattersRegistry.registerFomatter( evGwid,
          new TriggeredJournalEventFormatter( "готов", "нет готовности" ) );
      evGwid = Gwid.createEvent( "mcc.IrreversibleEngine", "evtSwitchOnFailure" );
      eventFormattersRegistry.registerFomatter( evGwid, new TriggeredJournalEventFormatter( "", true ) );
      evGwid = Gwid.createEvent( "mcc.IrreversibleEngine", "evtSwitchOffFailure" );
      eventFormattersRegistry.registerFomatter( evGwid, new TriggeredJournalEventFormatter( "", true ) );
      evGwid = Gwid.createEvent( "mcc.IrreversibleEngine", "evtHourMeterMin" );
      eventFormattersRegistry.registerFomatter( evGwid, new TriggeredJournalEventFormatter( "Сброс счетчика", false ) );
      evGwid = Gwid.createEvent( "mcc.MainSwitch", "evtAwpStart" );
      eventFormattersRegistry.registerFomatter( evGwid, new TriggeredJournalEventFormatter( "", true ) );
      evGwid = Gwid.createEvent( "mcc.MainSwitch", "evtAwpStop" );
      eventFormattersRegistry.registerFomatter( evGwid, new TriggeredJournalEventFormatter( "", true ) );
      evGwid = Gwid.createEvent( "mcc.MainSwitch", "evtPanelStart" );
      eventFormattersRegistry.registerFomatter( evGwid, new TriggeredJournalEventFormatter( "", true ) );
      evGwid = Gwid.createEvent( "mcc.MainSwitch", "evtPanelStop" );
      eventFormattersRegistry.registerFomatter( evGwid, new TriggeredJournalEventFormatter( "", true ) );
      evGwid = Gwid.createEvent( "mcc.MainSwitch", "evtControllerStart" );
      eventFormattersRegistry.registerFomatter( evGwid, new TriggeredJournalEventFormatter( "", true ) );
      evGwid = Gwid.createEvent( "mcc.MainSwitch", "evtControllerStop" );
      eventFormattersRegistry.registerFomatter( evGwid, new TriggeredJournalEventFormatter( "", true ) );
      evGwid = Gwid.createEvent( "mcc.MainSwitch", "evtAuxOn" );
      eventFormattersRegistry.registerFomatter( evGwid, new TriggeredJournalEventFormatter( "", true ) );
      evGwid = Gwid.createEvent( "mcc.MainSwitch", "evtAuxOff" );
      eventFormattersRegistry.registerFomatter( evGwid, new TriggeredJournalEventFormatter( "", true ) );
      evGwid = Gwid.createEvent( "mcc.MainSwitch", "evtEmergencyStop" );
      eventFormattersRegistry.registerFomatter( evGwid, new TriggeredJournalEventFormatter( "", true ) );
      evGwid = Gwid.createEvent( "mcc.MainSwitch", "evtLocalStart" );
      eventFormattersRegistry.registerFomatter( evGwid, new TriggeredJournalEventFormatter( "", true ) );
      evGwid = Gwid.createEvent( "mcc.MainSwitch", "evtLocalStop" );
      eventFormattersRegistry.registerFomatter( evGwid, new TriggeredJournalEventFormatter( "", true ) );
      evGwid = Gwid.createEvent( "mcc.MainSwitch", "evtAuxTimeOn" );
      eventFormattersRegistry.registerFomatter( evGwid, valueChangeJournalEventFormatter );
      evGwid = Gwid.createEvent( "mcc.MainSwitch", "evtAuxTimeOff" );
      eventFormattersRegistry.registerFomatter( evGwid, valueChangeJournalEventFormatter );
      evGwid = Gwid.createEvent( "mcc.MainSwitch", "evtPowerControl" );
      eventFormattersRegistry.registerFomatter( evGwid, new TriggeredJournalEventFormatter( "в норме", "отсуствует" ) );
      evGwid = Gwid.createEvent( "mcc.MainSwitch", "evtImitation" );
      eventFormattersRegistry.registerFomatter( evGwid,
          new TriggeredJournalEventFormatter( "установлено", "сброшено" ) );
      evGwid = Gwid.createEvent( "mcc.MainSwitch", "evtMainSwitchAlarm" );
      eventFormattersRegistry.registerFomatter( evGwid, new TriggeredJournalEventFormatter( "", true ) );
      evGwid = Gwid.createEvent( "mcc.MainSwitch", "evtSwitchOnFailure" );
      eventFormattersRegistry.registerFomatter( evGwid, new TriggeredJournalEventFormatter( "", true ) );
      evGwid = Gwid.createEvent( "mcc.MainSwitch", "evtSwitchOffFailure" );
      eventFormattersRegistry.registerFomatter( evGwid, new TriggeredJournalEventFormatter( "", true ) );
      evGwid = Gwid.createEvent( "mcc.MainSwitch", "evtHourMeterMin" );
      eventFormattersRegistry.registerFomatter( evGwid, new TriggeredJournalEventFormatter( "Сброс счетчика", false ) );
      evGwid = Gwid.createEvent( "mcc.ReversibleEngine", "evtAwpOpenStart" );
      eventFormattersRegistry.registerFomatter( evGwid,
          new TriggeredJournalEventFormatter( "Пуск с АРМ на открытие", true ) );
      evGwid = Gwid.createEvent( "mcc.ReversibleEngine", "evtAwpOpenStop" );
      eventFormattersRegistry.registerFomatter( evGwid,
          new TriggeredJournalEventFormatter( "Стоп с АРМ на открытие", true ) );
      evGwid = Gwid.createEvent( "mcc.ReversibleEngine", "evtAwpCloseStart" );
      eventFormattersRegistry.registerFomatter( evGwid,
          new TriggeredJournalEventFormatter( "Пуск с АРМ на закрытие", true ) );
      evGwid = Gwid.createEvent( "mcc.ReversibleEngine", "evtAwpCloseStop" );
      eventFormattersRegistry.registerFomatter( evGwid,
          new TriggeredJournalEventFormatter( "Стоп с АРМ на закрытие", true ) );
      evGwid = Gwid.createEvent( "mcc.ReversibleEngine", "evtPnlOpenStart" );
      eventFormattersRegistry.registerFomatter( evGwid,
          new TriggeredJournalEventFormatter( "Пуск с панели на открытие", true ) );
      evGwid = Gwid.createEvent( "mcc.ReversibleEngine", "evtPnlOpenStop" );
      eventFormattersRegistry.registerFomatter( evGwid,
          new TriggeredJournalEventFormatter( "Стоп с панели на открытие", true ) );
      evGwid = Gwid.createEvent( "mcc.ReversibleEngine", "evtPnlCloseStart" );
      eventFormattersRegistry.registerFomatter( evGwid,
          new TriggeredJournalEventFormatter( "Пуск с панели на закрытие", true ) );
      evGwid = Gwid.createEvent( "mcc.ReversibleEngine", "evtPnlCloseStop" );
      eventFormattersRegistry.registerFomatter( evGwid,
          new TriggeredJournalEventFormatter( "Стоп с панели на закрытие", true ) );
      evGwid = Gwid.createEvent( "mcc.ReversibleEngine", "evtLocalOpenStart" );
      eventFormattersRegistry.registerFomatter( evGwid,
          new TriggeredJournalEventFormatter( "Пуск с ПРУ на открытие", true ) );
      evGwid = Gwid.createEvent( "mcc.ReversibleEngine", "evtLocalOpenStop" );
      eventFormattersRegistry.registerFomatter( evGwid,
          new TriggeredJournalEventFormatter( "Стоп с ПРУ на открытие", true ) );
      evGwid = Gwid.createEvent( "mcc.ReversibleEngine", "evtLocalCloseStart" );
      eventFormattersRegistry.registerFomatter( evGwid,
          new TriggeredJournalEventFormatter( "Пуск с ПРУ на закрытие", true ) );
      evGwid = Gwid.createEvent( "mcc.ReversibleEngine", "evtLocalCloseStop" );
      eventFormattersRegistry.registerFomatter( evGwid,
          new TriggeredJournalEventFormatter( "Стоп с ПРУ на закрытие", true ) );
      evGwid = Gwid.createEvent( "mcc.ReversibleEngine", "evtCtrlOpenStart" );
      eventFormattersRegistry.registerFomatter( evGwid,
          new TriggeredJournalEventFormatter( "Пуск с контроллера на открытие", true ) );
      evGwid = Gwid.createEvent( "mcc.ReversibleEngine", "evtCtrlOpenStop" );
      eventFormattersRegistry.registerFomatter( evGwid,
          new TriggeredJournalEventFormatter( "Стоп с контроллера на открытие", true ) );
      evGwid = Gwid.createEvent( "mcc.ReversibleEngine", "evtCtrlCloseStart" );
      eventFormattersRegistry.registerFomatter( evGwid,
          new TriggeredJournalEventFormatter( "Пуск с контроллера на закрытие", true ) );
      evGwid = Gwid.createEvent( "mcc.ReversibleEngine", "evtCtrlCloseStop" );
      eventFormattersRegistry.registerFomatter( evGwid,
          new TriggeredJournalEventFormatter( "Стоп с контроллера на закрытие", true ) );
      evGwid = Gwid.createEvent( "mcc.ReversibleEngine", "evtPwr" );
      eventFormattersRegistry.registerFomatter( evGwid, new TriggeredJournalEventFormatter( "в норме", "отсуствует" ) );
      evGwid = Gwid.createEvent( "mcc.ReversibleEngine", "evtLimitSwitchOpen" );
      eventFormattersRegistry.registerFomatter( evGwid, new TriggeredJournalEventFormatter( "", true ) );
      evGwid = Gwid.createEvent( "mcc.ReversibleEngine", "evtLimitSwitchClose" );
      eventFormattersRegistry.registerFomatter( evGwid, new TriggeredJournalEventFormatter( "", true ) );
      evGwid = Gwid.createEvent( "mcc.ReversibleEngine", "evtAuxOpen" );
      eventFormattersRegistry.registerFomatter( evGwid, new TriggeredJournalEventFormatter( "", true ) );
      evGwid = Gwid.createEvent( "mcc.ReversibleEngine", "evtAuxClose" );
      eventFormattersRegistry.registerFomatter( evGwid, new TriggeredJournalEventFormatter( "", true ) );
      evGwid = Gwid.createEvent( "mcc.ReversibleEngine", "evtOpenTime" );
      eventFormattersRegistry.registerFomatter( evGwid, valueChangeJournalEventFormatter );
      evGwid = Gwid.createEvent( "mcc.ReversibleEngine", "evtCloseTime" );
      eventFormattersRegistry.registerFomatter( evGwid, valueChangeJournalEventFormatter );
      evGwid = Gwid.createEvent( "mcc.ReversibleEngine", "evtAuxTime" );
      eventFormattersRegistry.registerFomatter( evGwid, valueChangeJournalEventFormatter );
      evGwid = Gwid.createEvent( "mcc.ReversibleEngine", "evtPowerControl" );
      eventFormattersRegistry.registerFomatter( evGwid, new TriggeredJournalEventFormatter( "в норме", "отсуствует" ) );
      evGwid = Gwid.createEvent( "mcc.ReversibleEngine", "evtImitation" );
      eventFormattersRegistry.registerFomatter( evGwid,
          new TriggeredJournalEventFormatter( "установлено", "сброшено" ) );
      evGwid = Gwid.createEvent( "mcc.ReversibleEngine", "evtEnabled" );
      eventFormattersRegistry.registerFomatter( evGwid,
          new TriggeredJournalEventFormatter( "установлено", "сброшено" ) );
      evGwid = Gwid.createEvent( "mcc.ReversibleEngine", "evtOpenFailure" );
      eventFormattersRegistry.registerFomatter( evGwid, new TriggeredJournalEventFormatter( "", true ) );
      evGwid = Gwid.createEvent( "mcc.ReversibleEngine", "evtCloseFailure" );
      eventFormattersRegistry.registerFomatter( evGwid, new TriggeredJournalEventFormatter( "", true ) );
      evGwid = Gwid.createEvent( "mcc.ReversibleEngine", "evtOpenOnFailure" );
      eventFormattersRegistry.registerFomatter( evGwid, new TriggeredJournalEventFormatter( "", true ) );
      evGwid = Gwid.createEvent( "mcc.ReversibleEngine", "evtOpenOffFailure" );
      eventFormattersRegistry.registerFomatter( evGwid, new TriggeredJournalEventFormatter( "", true ) );
      evGwid = Gwid.createEvent( "mcc.ReversibleEngine", "evtCloseOnFailure" );
      eventFormattersRegistry.registerFomatter( evGwid, new TriggeredJournalEventFormatter( "", true ) );
      evGwid = Gwid.createEvent( "mcc.ReversibleEngine", "evtCloseOffFailure" );
      eventFormattersRegistry.registerFomatter( evGwid, new TriggeredJournalEventFormatter( "", true ) );
      evGwid = Gwid.createEvent( "mcc.ReversibleEngine", "evtHourMeterMin" );
      eventFormattersRegistry.registerFomatter( evGwid, new TriggeredJournalEventFormatter( "Сброс счетчика", false ) );
      evGwid = Gwid.createEvent( "mcc.DigInput", "evtImitation" );
      eventFormattersRegistry.registerFomatter( evGwid,
          new TriggeredJournalEventFormatter( "установлено", "сброшено" ) );
      evGwid = Gwid.createEvent( "mcc.DigInput", "evtInvert" );
      eventFormattersRegistry.registerFomatter( evGwid,
          new TriggeredJournalEventFormatter( "установлено", "сброшено" ) );
      evGwid = Gwid.createEvent( "mcc.DigInput", "evtFilterConst" );
      eventFormattersRegistry.registerFomatter( evGwid, valueChangeJournalEventFormatter );
      evGwid = Gwid.createEvent( "mcc.DigOutput", "evtImitation" );
      eventFormattersRegistry.registerFomatter( evGwid,
          new TriggeredJournalEventFormatter( "установлено", "сброшено" ) );
      evGwid = Gwid.createEvent( "mcc.DigOutput", "evtInvert" );
      eventFormattersRegistry.registerFomatter( evGwid,
          new TriggeredJournalEventFormatter( "установлено", "сброшено" ) );
      evGwid = Gwid.createEvent( "mcc.CtrlSystem", "evtAwpCtrl" );
      eventFormattersRegistry.registerFomatter( evGwid, new TriggeredJournalEventFormatter( "", true ) );
      evGwid = Gwid.createEvent( "mcc.CtrlSystem", "evtPanelCtrl" );
      eventFormattersRegistry.registerFomatter( evGwid, new TriggeredJournalEventFormatter( "", true ) );
      evGwid = Gwid.createEvent( "mcc.CtrlSystem", "evtLocalCtrl" );
      eventFormattersRegistry.registerFomatter( evGwid, new TriggeredJournalEventFormatter( "", true ) );
      evGwid = Gwid.createEvent( "mcc.CtrlSystem", "evtAutoCtrl" );
      eventFormattersRegistry.registerFomatter( evGwid, new TriggeredJournalEventFormatter( "", true ) );
      evGwid = Gwid.createEvent( "mcc.CtrlSystem", "evtOilFilterAlarm" );
      eventFormattersRegistry.registerFomatter( evGwid, new TriggeredJournalEventFormatter( "грязный", true ) );
      evGwid = Gwid.createEvent( "mcc.CtrlSystem", "evtStarting" );
      eventFormattersRegistry.registerFomatter( evGwid,
          new TriggeredJournalEventFormatter( "Начало запуска в Автомате", "Окончание запуска в Автомате" ) );
      evGwid = Gwid.createEvent( "mcc.CtrlSystem", "evtStoping" );
      eventFormattersRegistry.registerFomatter( evGwid,
          new TriggeredJournalEventFormatter( "Начало останова в Автомате", "Окончание останова в автомате" ) );
      evGwid = Gwid.createEvent( "mcc.CtrlSystem", "evtOn" );
      eventFormattersRegistry.registerFomatter( evGwid, new TriggeredJournalEventFormatter( "Агрегат включен", true ) );
      evGwid = Gwid.createEvent( "mcc.CtrlSystem", "evtOff" );
      eventFormattersRegistry.registerFomatter( evGwid,
          new TriggeredJournalEventFormatter( "Агрегат выключен", true ) );
      evGwid = Gwid.createEvent( "mcc.CtrlSystem", "evtEmergencyStop" );
      eventFormattersRegistry.registerFomatter( evGwid,
          new TriggeredJournalEventFormatter( "Останов Аварийный", true ) );
      evGwid = Gwid.createEvent( "mcc.CtrlSystem", "evtReady" );
      eventFormattersRegistry.registerFomatter( evGwid,
          new TriggeredJournalEventFormatter( "Готов к запуску", "Не готов к запуску" ) );
      evGwid = Gwid.createEvent( "mcc.CtrlSystem", "evtEnableSiren" );
      eventFormattersRegistry.registerFomatter( evGwid, new TriggeredJournalEventFormatter( "разрешение", "запрет" ) );
      evGwid = Gwid.createEvent( "mcc.CtrlSystem", "evtBlowCmplt" );
      eventFormattersRegistry.registerFomatter( evGwid,
          new TriggeredJournalEventFormatter( "выполнена", "нет воздуха" ) );
      evGwid = Gwid.createEvent( "mcc.CtrlSystem", "evtBlowTime" );
      eventFormattersRegistry.registerFomatter( evGwid, valueChangeJournalEventFormatter );
      evGwid = Gwid.createEvent( "mcc.CtrlSystem", "evtOilPressDiff2On" );
      eventFormattersRegistry.registerFomatter( evGwid, valueChangeJournalEventFormatter );
      evGwid = Gwid.createEvent( "mcc.CtrlSystem", "evtOilPressDiff2Off" );
      eventFormattersRegistry.registerFomatter( evGwid, valueChangeJournalEventFormatter );
      evGwid = Gwid.createEvent( "mcc.CtrlSystem", "evtTimeWaitAfterVentStop" );
      eventFormattersRegistry.registerFomatter( evGwid, valueChangeJournalEventFormatter );
      evGwid = Gwid.createEvent( "mcc.CtrlSystem", "evtCaseHeatTarget" );
      eventFormattersRegistry.registerFomatter( evGwid, valueChangeJournalEventFormatter );
      evGwid = Gwid.createEvent( "mcc.CtrlSystem", "evtGEDTime" );
      eventFormattersRegistry.registerFomatter( evGwid, valueChangeJournalEventFormatter );
      evGwid = Gwid.createEvent( "mcc.CtrlSystem", "evtSetOilTemp" );
      eventFormattersRegistry.registerFomatter( evGwid, valueChangeJournalEventFormatter );
      evGwid = Gwid.createEvent( "mcc.TwoPositionReg", "evtSetPointOn" );
      eventFormattersRegistry.registerFomatter( evGwid, valueChangeJournalEventFormatter );
      evGwid = Gwid.createEvent( "mcc.TwoPositionReg", "evtSetPointOff" );
      eventFormattersRegistry.registerFomatter( evGwid, valueChangeJournalEventFormatter );
      evGwid = Gwid.createEvent( "mcc.TwoPositionReg", "evtAuto" );
      eventFormattersRegistry.registerFomatter( evGwid,
          new TriggeredJournalEventFormatter( "регулятор в работе", "регулятор отключен" ) );
      evGwid = Gwid.createEvent( "mcc.TwoPositionReg", "evtOn" );
      eventFormattersRegistry.registerFomatter( evGwid,
          new TriggeredJournalEventFormatter( "выход включен", "выход отключен" ) );
      evGwid = Gwid.createEvent( "mcc.AnalogOutput", "evtY1" );
      eventFormattersRegistry.registerFomatter( evGwid, valueChangeJournalEventFormatter );
      evGwid = Gwid.createEvent( "mcc.AnalogOutput", "evtY0" );
      eventFormattersRegistry.registerFomatter( evGwid, valueChangeJournalEventFormatter );
      evGwid = Gwid.createEvent( "mcc.AnalogOutput", "evtImitation" );
      eventFormattersRegistry.registerFomatter( evGwid,
          new TriggeredJournalEventFormatter( "установлено", "сброшено" ) );
      evGwid = Gwid.createEvent( "mcc.AnalogEngine", "evtAlarm" );
      eventFormattersRegistry.registerFomatter( evGwid,
          new TriggeredJournalEventFormatter( "Авария задания ДЗ", true ) );
      evGwid = Gwid.createEvent( "mcc.AnalogReg", "evtTask" );
      eventFormattersRegistry.registerFomatter( evGwid, valueChangeJournalEventFormatter );
      evGwid = Gwid.createEvent( "mcc.AnalogReg", "evtDead" );
      eventFormattersRegistry.registerFomatter( evGwid, valueChangeJournalEventFormatter );
      evGwid = Gwid.createEvent( "mcc.AnalogReg", "evtKp" );
      eventFormattersRegistry.registerFomatter( evGwid, valueChangeJournalEventFormatter );
      evGwid = Gwid.createEvent( "mcc.AnalogReg", "evtTi" );
      eventFormattersRegistry.registerFomatter( evGwid, valueChangeJournalEventFormatter );
      evGwid = Gwid.createEvent( "mcc.AnalogReg", "evtTd" );
      eventFormattersRegistry.registerFomatter( evGwid, valueChangeJournalEventFormatter );
      evGwid = Gwid.createEvent( "mcc.AnalogReg", "evtEnKp" );
      eventFormattersRegistry.registerFomatter( evGwid,
          new TriggeredJournalEventFormatter( "разрешено", "запрещено" ) );
      evGwid = Gwid.createEvent( "mcc.AnalogReg", "evtEnKi" );
      eventFormattersRegistry.registerFomatter( evGwid,
          new TriggeredJournalEventFormatter( "разрешено", "запрещено" ) );
      evGwid = Gwid.createEvent( "mcc.AnalogReg", "evtEnKd" );
      eventFormattersRegistry.registerFomatter( evGwid,
          new TriggeredJournalEventFormatter( "разрешено", "запрещено" ) );
      evGwid = Gwid.createEvent( "mcc.AnalogReg", "evtFullTime" );
      eventFormattersRegistry.registerFomatter( evGwid, valueChangeJournalEventFormatter );
      evGwid = Gwid.createEvent( "mcc.AnalogReg", "evtMax" );
      eventFormattersRegistry.registerFomatter( evGwid, valueChangeJournalEventFormatter );
      evGwid = Gwid.createEvent( "mcc.AnalogReg", "evtMin" );
      eventFormattersRegistry.registerFomatter( evGwid, valueChangeJournalEventFormatter );
      evGwid = Gwid.createEvent( "mcc.AnalogReg", "evtEnable" );
      eventFormattersRegistry.registerFomatter( evGwid, new TriggeredJournalEventFormatter( "включен", "отключен" ) );
    }
    catch( Exception exept ) {
      LoggerUtils.defaultLogger().error( exept );
    }

  }

  @Override
  protected void initWin( IEclipseContext aWinContext ) {
    ISkJournalsConstants.init( aWinContext );
  }

  /**
   * Класс осуществляющий форматирование события изменение сессии соединения с сервером.
   *
   * @author Dima
   */
  static class SessionChangeJournalEventFormatter
      implements ISkModJournalEventFormatter {

    private static final String STR_IP_ADDRESS  = "IP адрес: ";
    private static final String EVPARAM_IP_ADDR = "IP";

    @Override
    public String formatShortText( SkEvent aEvent, ITsGuiContext aContext ) {
      IOptionSet parvals = aEvent.paramValues();
      String ip = parvals.getStr( EVPARAM_IP_ADDR );

      return STR_IP_ADDRESS + ip;
    }

    @Override
    public String formatLongText( SkEvent aEvent, ITsGuiContext aContext ) {
      IOptionSet parvals = aEvent.paramValues();
      String ip = parvals.getStr( EVPARAM_IP_ADDR );

      return STR_IP_ADDRESS + ip;
    }

  }

  /**
   * Класс осуществляющий форматирование события изменение сессии соединения с сервером.
   *
   * @author Max
   */
  static class ValueChangeJournalEventFormatter
      implements ISkModJournalEventFormatter {

    private static final String STR_OLD_VALUE = "Старое значение= ";

    private static final String STR_NEW_VALUE = "Новое значение= ";

    private static final String STR_DELIMETER = "; ";

    /**
     * Формат наименования события.
     */
    private static final String VIS_NAME_FORMAT = "%s"; //$NON-NLS-1$

    @Override
    public String formatShortText( SkEvent aEvent, ITsGuiContext aContext ) {
      ISkConnection conn = aContext.get( ISkConnectionSupplier.class ).defConn();
      // Получаем объект события
      ISkObject skObject = conn.coreApi().objService().find( aEvent.eventGwid().skid() );
      // Получаем его класс
      ISkClassInfo skClass = conn.coreApi().sysdescr().findClassInfo( skObject.classId() );
      // Описание события
      IDtoEventInfo evInfo = skClass.events().list().findByKey( aEvent.eventGwid().propId() );

      return (String.format( VIS_NAME_FORMAT, evInfo.nmName() ));
    }

    @Override
    public String formatLongText( SkEvent aEvent, ITsGuiContext aContext ) {
      IOptionSet parvals = aEvent.paramValues();
      ISkConnection conn = aContext.get( ISkConnectionSupplier.class ).defConn();
      // Получаем объект события
      ISkObject skObject = conn.coreApi().objService().find( aEvent.eventGwid().skid() );
      // Получаем его класс
      ISkClassInfo skClass = conn.coreApi().sysdescr().findClassInfo( skObject.classId() );
      // Описание события
      IDtoEventInfo evInfo = skClass.events().list().findByKey( aEvent.eventGwid().propId() );

      if( !parvals.hasKey( EVPARAM_OLD_VAL ) || !parvals.hasKey( EVPARAM_NEW_VAL ) ) {
        return ERROR_FORMING_MESSAGE_STR;
      }

      IAtomicValue oldVal = parvals.getValue( EVPARAM_OLD_VAL );
      IAtomicValue newVal = parvals.getValue( EVPARAM_NEW_VAL );

      return String.format( VIS_NAME_FORMAT, evInfo.nmName() ) + STR_DELIMETER + STR_OLD_VALUE + oldVal.asString()
          + STR_DELIMETER + STR_NEW_VALUE + newVal.asString();
    }

  }

  /**
   * Класс осуществляющий форматирование события изменение сессии соединения с сервером.
   *
   * @author Max
   */
  static class TriggeredJournalEventFormatter
      implements ISkModJournalEventFormatter {

    private static final String MSG_DEFAULT_OFF_VALUE = "OFF";

    private static final String MSG_DEFAULT_ON_VALUE = "ON";

    private String frontVisName = TsLibUtils.EMPTY_STRING;

    private String antiFrontVisName = TsLibUtils.EMPTY_STRING;

    private static final String STR_MESSAGE = "Сообщение = ";

    private static final String STR_DELIMETER = "; ";

    /**
     * Формат наименования события.
     */
    private static final String VIS_NAME_FORMAT = "%s"; //$NON-NLS-1$

    TriggeredJournalEventFormatter( String aFrontVisName, String aAntiFrontVisName ) {
      frontVisName = aFrontVisName;
      antiFrontVisName = aAntiFrontVisName;
    }

    TriggeredJournalEventFormatter( String aVisName, boolean aIsFront ) {
      if( aIsFront ) {
        frontVisName = aVisName;
      }
      else {
        antiFrontVisName = aVisName;
      }
    }

    @Override
    public String formatShortText( SkEvent aEvent, ITsGuiContext aContext ) {
      ISkConnection conn = aContext.get( ISkConnectionSupplier.class ).defConn();
      // Получаем объект события
      ISkObject skObject = conn.coreApi().objService().find( aEvent.eventGwid().skid() );
      // Получаем его класс
      ISkClassInfo skClass = conn.coreApi().sysdescr().findClassInfo( skObject.classId() );
      // Описание события
      IDtoEventInfo evInfo = skClass.events().list().findByKey( aEvent.eventGwid().propId() );

      return (String.format( VIS_NAME_FORMAT, evInfo.nmName() ));
    }

    @Override
    public String formatLongText( SkEvent aEvent, ITsGuiContext aContext ) {
      IOptionSet parvals = aEvent.paramValues();
      ISkConnection conn = aContext.get( ISkConnectionSupplier.class ).defConn();
      // Получаем объект события
      ISkObject skObject = conn.coreApi().objService().find( aEvent.eventGwid().skid() );
      // Получаем его класс
      ISkClassInfo skClass = conn.coreApi().sysdescr().findClassInfo( skObject.classId() );
      // Описание события
      IDtoEventInfo evInfo = skClass.events().list().findByKey( aEvent.eventGwid().propId() );

      Boolean onVal = null;
      if( parvals.hasKey( EVPARAM_ON_VAL ) ) {
        // булевое значение
        onVal = Boolean.valueOf( parvals.getBool( EVPARAM_ON_VAL ) );
      }
      else
        if( parvals.hasKey( EVPARAM_NEW_VAL ) ) {
          IAtomicValue value = parvals.getValue( EVPARAM_NEW_VAL );
          if( value.atomicType() == EAtomicType.INTEGER ) {
            onVal = Boolean.valueOf( value.asInt() != 0 );
          }
          else
            if( value.atomicType() == EAtomicType.FLOATING ) {
              onVal = Boolean.valueOf( value.asFloat() != 0 );
            }
            else
              if( value.atomicType() == EAtomicType.BOOLEAN ) {
                onVal = Boolean.valueOf( value.asBool() );
              }
        }

      if( onVal == null ) {
        return ERROR_FORMING_MESSAGE_STR;
      }

      String message = onVal.booleanValue() ? frontVisName : antiFrontVisName;
      if( message.length() == 0 ) {
        message = onVal.booleanValue() ? MSG_DEFAULT_ON_VALUE : MSG_DEFAULT_OFF_VALUE;
      }

      return String.format( VIS_NAME_FORMAT, evInfo.nmName() ) + STR_DELIMETER + STR_MESSAGE + message;
    }

  }

}