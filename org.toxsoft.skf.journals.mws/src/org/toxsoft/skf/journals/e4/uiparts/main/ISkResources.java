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
  String PRINT_EVENT_LIST_TITLE_FORMAT   = Messages.PRINT_EVENT_LIST_TITLE_FORMAT;
  String PRINT_COMMAND_LIST_TITLE_FORMAT = Messages.PRINT_COMMAND_LIST_TITLE_FORMAT;
  String PRINT_ALARM_LIST_TITLE_FORMAT   = Messages.PRINT_ALARM_LIST_TITLE_FORMAT;
  String AUTHOR_STR                      = Messages.AUTHOR_STR;
  String DATE_STR                        = Messages.DATE_STR;
  String MSG_QUERIENG_EVENTS             = "Запрос на выбранные события...";
  String MSG_PREPARE_EVENTS_QUERY        = "Подготовка запроса событий";
  String MSG_PREPARE_EVENTS_VIEW         = "Подготовка к отображению %d событий: этап 2";

  /**
   * {@link CommandsJournalPanel},
   */
  String MSG_QUERIENG_CMDS      = "Запрос на выбранные команды...";
  String MSG_PREPARE_CMDS_QUERY = "Подготовка запроса команд";
  String MSG_PREPARE_CMDS_VIEW  = "Подготовка к отображению %d команд: этап 2";

  /**
   * {@link JournalsPanel},
   */
  String EVENTS_STR = Messages.EVENTS_STR;
  String CMDS_STR   = Messages.CMDS_STR;

}
