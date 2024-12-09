package org.toxsoft.skf.journals.e4.uiparts.main;

/**
 * Локализуемые ресурсы.
 *
 * @author dima
 */
@SuppressWarnings( "nls" )
interface ISkResources {

  /**
   * {@link EventsJournalPanel},
   */
  String PRINT_EVENT_LIST_TITLE_FORMAT   = Messages.getString( "PRINT_EVENT_LIST_TITLE_FORMAT" );   //$NON-NLS-1$
  String PRINT_COMMAND_LIST_TITLE_FORMAT = Messages.getString( "PRINT_COMMAND_LIST_TITLE_FORMAT" ); //$NON-NLS-1$
  String PRINT_ALARM_LIST_TITLE_FORMAT   = Messages.getString( "PRINT_ALARM_LIST_TITLE_FORMAT" );   //$NON-NLS-1$
  String AUTHOR_STR                      = Messages.getString( "AUTHOR_STR" );                      //$NON-NLS-1$
  String DATE_STR                        = Messages.getString( "DATE_STR" );                        //$NON-NLS-1$
  String MSG_QUERIENG_EVENTS             = Messages.getString( "MSG_QUERIENG_EVENTS" );             //$NON-NLS-1$
  String MSG_PREPARE_EVENTS_QUERY        = Messages.getString( "MSG_PREPARE_EVENTS_QUERY" );        //$NON-NLS-1$
  String MSG_PREPARE_EVENTS_VIEW         = Messages.getString( "MSG_PREPARE_EVENTS_VIEW" );         //$NON-NLS-1$

  /**
   * {@link CommandsJournalPanel},
   */
  String MSG_QUERIENG_CMDS      = Messages.getString( "MSG_QUERIENG_CMDS" );      //$NON-NLS-1$
  String MSG_PREPARE_CMDS_QUERY = Messages.getString( "MSG_PREPARE_CMDS_QUERY" ); //$NON-NLS-1$
  String MSG_PREPARE_CMDS_VIEW  = Messages.getString( "MSG_PREPARE_CMDS_VIEW" );  //$NON-NLS-1$

  /**
   * {@link JournalsPanel},
   */
  String EVENTS_STR = Messages.getString( "EVENTS_STR" ); //$NON-NLS-1$
  String CMDS_STR   = Messages.getString( "CMDS_STR" );   //$NON-NLS-1$

}
