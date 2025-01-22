package org.toxsoft.skf.journals.e4.uiparts.main;

import static org.toxsoft.skf.journals.ISkJournalsConstants.*;
import static org.toxsoft.skf.journals.e4.uiparts.main.ISkResources.*;

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.panels.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.logs.impl.*;
import org.toxsoft.skf.journals.e4.uiparts.engine.*;
import org.toxsoft.uskat.core.api.users.ability.*;
import org.toxsoft.uskat.core.connection.*;
import org.toxsoft.uskat.core.gui.conn.*;

/**
 * Панель журналов (событий, команд и т.д.)
 *
 * @author max
 */
public class JournalsPanel
    extends TsPanel {

  /**
   * @param aParent родительский компонент
   * @param aContext контекст
   */
  public JournalsPanel( Composite aParent, ITsGuiContext aContext ) {
    super( aParent, aContext );

    setLayout( new BorderLayout() );

    TabFolder paramsFolder = new TabFolder( this, SWT.NONE );

    IM5Domain m5 = eclipseContext().get( IM5Domain.class );
    try {
      ITsGuiContext eventContext = new TsGuiContext( aContext );
      ISkConnection connection = eventContext.get( ISkConnectionSupplier.class ).defConn();
      // check access to functional
      ISkAbility accessEvents =
          connection.coreApi().userService().abilityManager().findAbility( ABILITY_JOURNALS_EVENTS.id() );
      if( connection.coreApi().userService().abilityManager().isAbilityAllowed( accessEvents.id() ) ) {
        if( !m5.models().hasKey( EventM5Model.MODEL_ID ) ) {

          m5.addModel( new EventM5Model( connection, eventContext ) );
        }
        createEventsTable( paramsFolder, eventContext );
      }
    }
    catch( TsException ex ) {
      LoggerUtils.errorLogger().error( ex );
    }

    try {
      ITsGuiContext cmdContext = new TsGuiContext( aContext );
      ISkConnection connection = cmdContext.get( ISkConnectionSupplier.class ).defConn();
      // check access to functional
      ISkAbility accessCommands =
          connection.coreApi().userService().abilityManager().findAbility( ABILITY_JOURNALS_COMMANDS.id() );
      if( connection.coreApi().userService().abilityManager().isAbilityAllowed( accessCommands.id() ) ) {
        if( !m5.models().hasKey( CommandM5Model.MODEL_ID ) ) {
          m5.addModel( new CommandM5Model( connection, false ) );
        }
        createCommandsTable( paramsFolder, cmdContext );
      }
    }
    catch( TsException ex ) {
      LoggerUtils.errorLogger().error( ex );
    }

    try {
      ITsGuiContext alarmContext = new TsGuiContext( aContext );
      ISkConnection connection = alarmContext.get( ISkConnectionSupplier.class ).defConn();
      // check access to functional
      ISkAbility accessAlarms =
          connection.coreApi().userService().abilityManager().findAbility( ABILITY_JOURNALS_ALARMS.id() );
      if( connection.coreApi().userService().abilityManager().isAbilityAllowed( accessAlarms.id() ) ) {
        // 2024-10-17 mvk remove legacy alarms impl
        // if( !m5.models().hasKey( SkAlarmM5Model.MODEL_ID ) ) {
        // m5.addModel( new SkAlarmM5Model( false ) );
        // }
        createAlarmsTable( paramsFolder, alarmContext );
      }
    }
    catch( TsException ex ) {
      LoggerUtils.errorLogger().error( ex );
    }
  }

  private static void createEventsTable( TabFolder aParent, ITsGuiContext aContext )
      throws TsException {

    TabItem item = new TabItem( aParent, SWT.NONE );
    item.setText( EVENTS_STR );

    item.setControl( new EventsJournalPanel( aParent, aContext ) );
  }

  private static void createCommandsTable( TabFolder aParent, ITsGuiContext aContext )
      throws TsException {

    TabItem item = new TabItem( aParent, SWT.NONE );
    item.setText( CMDS_STR );

    item.setControl( new CommandsJournalPanel( aParent, aContext ) );
  }

  private static void createAlarmsTable( TabFolder aParent, ITsGuiContext aContext )
      throws TsException {

    TabItem item = new TabItem( aParent, SWT.NONE );
    item.setText( "Тревоги" ); //$NON-NLS-1$
    item.setControl( new AlarmsJournalPanel( aParent, aContext ) );
  }

}
