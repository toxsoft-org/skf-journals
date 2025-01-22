package org.toxsoft.skf.journals;

import static org.toxsoft.skf.journals.e4.uiparts.main.ISkResources.*;

import org.eclipse.e4.core.contexts.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.uskat.core.api.users.ability.*;
import org.toxsoft.uskat.core.impl.dto.*;

/**
 * Plugin constants.
 * <p>
 *
 * @author hazard157
 */
@SuppressWarnings( "javadoc" )
public interface ISkJournalsConstants {

  // ------------------------------------------------------------------------------------
  // E4

  String PERSPID_JOURNALS = "org.toxsoft.skf.journals.perps"; //$NON-NLS-1$
  String JOURNALS_PREFIX  = "uskat.journals";                 //$NON-NLS-1$

  // ------------------------------------------------------------------------------------
  // Icons

  String PREFIX_OF_ICON_FIELD_NAME = "ICONID_"; //$NON-NLS-1$
  // String ICONID_APP_ICON = "app-icon"; //$NON-NLS-1$

  /**
   * id тип возможности «Журналы»
   */
  String ABKINDID_JOURNALS = "uskat.abkind.journals"; //$NON-NLS-1$

  /**
   * Create id ability to access journals
   */
  String ABILITYID_JOURNALS_PERSP = JOURNALS_PREFIX + ".ability.access.persp"; //$NON-NLS-1$

  /**
   * id возможности просмотра событий
   */
  String ABILITYID_JOURNALS_EVENTS = JOURNALS_PREFIX + ".ability.view_events"; //$NON-NLS-1$

  /**
   * id возможности просмотра команд
   */
  String ABILITYID_JOURNALS_COMMANDS = JOURNALS_PREFIX + ".ability.view_commands"; //$NON-NLS-1$

  /**
   * id возможности просмотра алармов
   */
  String ABILITYID_JOURNALS_ALARMS = JOURNALS_PREFIX + ".ability.view_alarms"; //$NON-NLS-1$

  /**
   * создание «своего» типа
   */
  IDtoSkAbilityKind ABKIND_JOURNALS =
      DtoSkAbilityKind.create( ABKINDID_JOURNALS, STR_ABKIND_JOURNALS, STR_ABKIND_JOURNALS_D );

  /**
   * Create ability to access journals
   */
  IDtoSkAbility ABILITY_ACCESS_JOURNALS = DtoSkAbility.create( ABILITYID_JOURNALS_PERSP, ABKINDID_JOURNALS,
      STR_ABILITY_ACCESS_JOURNALS, STR_ABILITY_ACCESS_JOURNALS_D );
  /**
   * создание возможности просмотр событий
   */
  IDtoSkAbility ABILITY_JOURNALS_EVENTS = DtoSkAbility.create( ABILITYID_JOURNALS_EVENTS, ABKINDID_JOURNALS,
      STR_ABILITY_JOURNALS_EVENTS, STR_ABILITY_JOURNALS_EVENTS_D );

  /**
   * создание возможности просмотр команд
   */
  IDtoSkAbility ABILITY_JOURNALS_COMMANDS = DtoSkAbility.create( ABILITYID_JOURNALS_COMMANDS, ABKINDID_JOURNALS,
      STR_ABILITY_JOURNALS_COMMANDS, STR_ABILITY_JOURNALS_COMMANDS_D );

  /**
   * создание возможности просмотр алармов
   */
  IDtoSkAbility ABILITY_JOURNALS_ALARMS = DtoSkAbility.create( ABILITYID_JOURNALS_ALARMS, ABKINDID_JOURNALS,
      STR_ABILITY_JOURNALS_ALARMS, STR_ABILITY_JOURNALS_ALARMS_D );

  /**
   * Constants registration.
   *
   * @param aWinContext {@link IEclipseContext} - windows level context
   */
  static void init( IEclipseContext aWinContext ) {
    ITsIconManager iconManager = aWinContext.get( ITsIconManager.class );
    iconManager.registerStdIconByIds( Activator.PLUGIN_ID, ISkJournalsConstants.class, PREFIX_OF_ICON_FIELD_NAME );
  }

}
