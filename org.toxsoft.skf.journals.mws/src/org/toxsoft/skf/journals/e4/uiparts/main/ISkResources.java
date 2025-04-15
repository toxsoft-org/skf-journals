package org.toxsoft.skf.journals.e4.uiparts.main;

import org.toxsoft.skf.journals.*;

/**
 * Локализуемые ресурсы.
 *
 * @author dima
 */
@SuppressWarnings( { "nls", "javadoc" } )
public interface ISkResources {

  /**
   * {@link EventsJournalPanel},
   */
  String PRINT_EVENT_LIST_TITLE_FORMAT   = Messages.getString( "PRINT_EVENT_LIST_TITLE_FORMAT" );
  String PRINT_COMMAND_LIST_TITLE_FORMAT = Messages.getString( "PRINT_COMMAND_LIST_TITLE_FORMAT" );
  String PRINT_ALARM_LIST_TITLE_FORMAT   = Messages.getString( "PRINT_ALARM_LIST_TITLE_FORMAT" );
  String AUTHOR_STR                      = Messages.getString( "AUTHOR_STR" );
  String DATE_STR                        = Messages.getString( "DATE_STR" );
  String MSG_QUERIENG_EVENTS             = Messages.getString( "MSG_QUERIENG_EVENTS" );
  String MSG_PREPARE_EVENTS_QUERY        = Messages.getString( "MSG_PREPARE_EVENTS_QUERY" );
  String MSG_PREPARE_EVENTS_VIEW         = Messages.getString( "MSG_PREPARE_EVENTS_VIEW" );

  /**
   * {@link CommandsJournalPanel},
   */
  String MSG_QUERIENG_CMDS      = Messages.getString( "MSG_QUERIENG_CMDS" );
  String MSG_PREPARE_CMDS_QUERY = Messages.getString( "MSG_PREPARE_CMDS_QUERY" );
  String MSG_PREPARE_CMDS_VIEW  = Messages.getString( "MSG_PREPARE_CMDS_VIEW" );

  /**
   * {@link JournalsPanel},
   */
  String EVENTS_STR       = Messages.getString( "EVENTS_STR" );
  String CMDS_STR         = Messages.getString( "CMDS_STR" );
  String STR_EVENTS_LIMIT = "Количество полученных событий(%d) превышает предел панели событий (%d).\n\n"
      + "События будут отображены в интервале:\n%s.";

  /**
   * {@link ISkJournalsConstants},
   */
  String STR_ABKIND_JOURNALS             = Messages.getString( "STR_ABKIND_JOURNALS" );
  String STR_ABKIND_JOURNALS_D           = Messages.getString( "STR_ABKIND_JOURNALS_D" );
  String STR_ABILITY_ACCESS_JOURNALS     = Messages.getString( "STR_ABILITY_ACCESS_JOURNALS" );
  String STR_ABILITY_ACCESS_JOURNALS_D   = Messages.getString( "STR_ABILITY_ACCESS_JOURNALS_D" );
  String STR_ABILITY_JOURNALS_EVENTS     = Messages.getString( "STR_ABILITY_JOURNALS_EVENTS" );
  String STR_ABILITY_JOURNALS_EVENTS_D   = Messages.getString( "STR_ABILITY_JOURNALS_EVENTS_D" );
  String STR_ABILITY_JOURNALS_COMMANDS   = Messages.getString( "STR_ABILITY_JOURNALS_COMMANDS" );
  String STR_ABILITY_JOURNALS_COMMANDS_D = Messages.getString( "STR_ABILITY_JOURNALS_COMMANDS_D" );
  String STR_ABILITY_JOURNALS_ALARMS     = Messages.getString( "STR_ABILITY_JOURNALS_ALARMS" );
  String STR_ABILITY_JOURNALS_ALARMS_D   = Messages.getString( "STR_ABILITY_JOURNALS_ALARMS_D" );

}
