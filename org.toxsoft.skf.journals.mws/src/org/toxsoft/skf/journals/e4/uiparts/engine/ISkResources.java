package org.toxsoft.skf.journals.e4.uiparts.engine;

/**
 * Локализуемые ресурсы.
 *
 * @author dima
 */
@SuppressWarnings( "nls" )
interface ISkResources {

  /**
   * {@link EventM5Model},
   */
  String EVENTS_LIST_TABLE_DESCR = Messages.getString( "EVENTS_LIST_TABLE_DESCR" ); //$NON-NLS-1$
  String EVENTS_LIST_TABLE_NAME  = Messages.getString( "EVENTS_LIST_TABLE_NAME" );  //$NON-NLS-1$
  String EVENT_SRC_COL_DESCR     = Messages.getString( "EVENT_SRC_COL_DESCR" );     //$NON-NLS-1$
  String EVENT_SRC_COL_NAME      = Messages.getString( "EVENT_SRC_COL_NAME" );      //$NON-NLS-1$
  String EVENT_TIME_COL_DESCR    = Messages.getString( "EVENT_TIME_COL_DESCR" );    //$NON-NLS-1$
  String EVENT_TIME_COL_NAME     = Messages.getString( "EVENT_TIME_COL_NAME" );     //$NON-NLS-1$
  String EVENT_NAME_COL_DESCR    = Messages.getString( "EVENT_NAME_COL_DESCR" );    //$NON-NLS-1$
  String EVENT_NAME_COL_NAME     = Messages.getString( "EVENT_NAME_COL_NAME" );     //$NON-NLS-1$
  String DESCRIPTION_STR         = Messages.getString( "DESCRIPTION_STR" );         //$NON-NLS-1$
  String EV_DESCRIPTION          = Messages.getString( "EV_DESCRIPTION" );          //$NON-NLS-1$

  /**
   * {@link DialogConcerningEventsParams},
   */
  String STR_L_CLASSES               = Messages.getString( "STR_L_CLASSES" );               //$NON-NLS-1$
  String STR_BTN_CLEAR_FILTER        = Messages.getString( "STR_BTN_CLEAR_FILTER" );        //$NON-NLS-1$
  String STR_L_EVENTS                = Messages.getString( "STR_L_EVENTS" );                //$NON-NLS-1$
  String STR_L_COMMANDS              = Messages.getString( "STR_L_COMMANDS" );              //$NON-NLS-1$
  String STR_L_OBJECTS               = Messages.getString( "STR_L_OBJECTS" );               //$NON-NLS-1$
  String STR_COL_OBJ_NAME            = Messages.getString( "STR_COL_OBJ_NAME" );            //$NON-NLS-1$
  String STR_COL_EVENTS              = Messages.getString( "STR_COL_EVENTS" );              //$NON-NLS-1$
  String STR_COL_COMMANDS            = Messages.getString( "STR_COL_COMMANDS" );            //$NON-NLS-1$
  String DLG_T_CEP_EDIT              = Messages.getString( "DLG_T_CEP_EDIT" );              //$NON-NLS-1$
  String DLG_C_CEP_EDIT              = Messages.getString( "DLG_C_CEP_EDIT" );              //$NON-NLS-1$
  String STR_P_CHECK_ALL             = Messages.getString( "STR_P_CHECK_ALL" );             //$NON-NLS-1$
  String STR_P_UNCHECK_ALL           = Messages.getString( "STR_P_UNCHECK_ALL" );           //$NON-NLS-1$
  String CLASSES_N_PARAMS_TREE_TITLE = Messages.getString( "CLASSES_N_PARAMS_TREE_TITLE" ); //$NON-NLS-1$
  String ENTITY_TREE_TITLE           = Messages.getString( "ENTITY_TREE_TITLE" );           //$NON-NLS-1$

  /**
   * {@link EventQueryEngine}
   */
  String MSG_INFO_QUERIENG_EVENTS = Messages.getString( "MSG_INFO_QUERIENG_EVENTS" ); //$NON-NLS-1$
  String MSG_PREPARE_EVENTS_QUERY = Messages.getString( "MSG_PREPARE_EVENTS_QUERY" ); //$NON-NLS-1$
  String MSG_PREPARE_EVENTS_VIEW  = Messages.getString( "MSG_PREPARE_EVENTS_VIEW" );  //$NON-NLS-1$
  String ERR_QUERY_EVENTS_FAILED  = Messages.getString( "ERR_QUERY_EVENTS_FAILED" );  //$NON-NLS-1$
  String MSG_QUERIENG_CMDS        = Messages.getString( "MSG_QUERIENG_CMDS" );        //$NON-NLS-1$
  String MSG_PREPARE_CMDS_QUERY   = Messages.getString( "MSG_PREPARE_CMDS_QUERY" );   //$NON-NLS-1$
  String MSG_PREPARE_CMDS_VIEW    = Messages.getString( "MSG_PREPARE_CMDS_VIEW" );    //$NON-NLS-1$

  /**
   * {@link EventQueryEngine}
   */
  String MSG_INFO_QUERIENG_CMDS = Messages.getString( "MSG_INFO_QUERIENG_CMDS" ); //$NON-NLS-1$
  String MSG_PREPARE_CMDS_VIEW1 = Messages.getString( "MSG_PREPARE_CMDS_VIEW1" ); //$NON-NLS-1$
  String ERR_QUERY_CMDS_FAILED  = Messages.getString( "ERR_QUERY_CMDS_FAILED" );  //$NON-NLS-1$

  /**
   * {@link JournalParamsPanel}, { JournalCmdParamsPanel}
   */
  String STR_P_TEXT_START_TIME = Messages.getString( "STR_P_TEXT_START_TIME" ); //$NON-NLS-1$
  String STR_P_TEXT_DURATION   = Messages.getString( "STR_P_TEXT_DURATION" );   //$NON-NLS-1$
  String STR_L_JPP_NAME        = Messages.getString( "STR_L_JPP_NAME" );        //$NON-NLS-1$
  String STR_SEARCH_BTN_NAME   = Messages.getString( "STR_SEARCH_BTN_NAME" );   //$NON-NLS-1$
  String DLG_T_EXPORT_FILE     = Messages.getString( "DLG_T_EXPORT_FILE" );     //$NON-NLS-1$

  /**
   * {@link CommandM5Model},
   */
  String CMDS_LIST_TABLE_DESCR = Messages.getString( "CMDS_LIST_TABLE_DESCR" ); //$NON-NLS-1$
  String CMDS_LIST_TABLE_NAME  = Messages.getString( "CMDS_LIST_TABLE_NAME" );  //$NON-NLS-1$
  String CMD_EXEC_COL_DESCR    = Messages.getString( "CMD_EXEC_COL_DESCR" );    //$NON-NLS-1$
  String CMD_EXEC_COL_NAME     = Messages.getString( "CMD_EXEC_COL_NAME" );     //$NON-NLS-1$
  String AUTHOR_COL_DESCR      = Messages.getString( "AUTHOR_COL_DESCR" );      //$NON-NLS-1$
  String AUTHOR_COL_NAME       = Messages.getString( "AUTHOR_COL_NAME" );       //$NON-NLS-1$
  String TIME_COL_DESCR        = Messages.getString( "TIME_COL_DESCR" );        //$NON-NLS-1$
  String TIME_COL_NAME         = Messages.getString( "TIME_COL_NAME" );         //$NON-NLS-1$
  String VIS_NAME_COL_DESCR    = Messages.getString( "VIS_NAME_COL_DESCR" );    //$NON-NLS-1$
  String VIS_NAME_COL_NAME     = Messages.getString( "VIS_NAME_COL_NAME" );     //$NON-NLS-1$
  String COMMAND_ARGUMENTS     = Messages.getString("COMMAND_ARGUMENTS"); //$NON-NLS-1$

  /**
   * {@link ObjectsTreeComposite},
   */
  String STR_SEARCH = Messages.getString( "STR_SEARCH" ); //$NON-NLS-1$

  /**
   * Относительный путь к иконке "Фильтра запроса событий".
   */
  String ICON_FILTER    = "icons/is24x24/filter.png"; //$NON-NLS-1$
  /**
   * Относительный путь к иконке "Запуск запроса".
   */
  String ICON_RUN_QUERY = "icons/is24x24/run.png";    //$NON-NLS-1$

  /**
   * Относительный путь к иконке "Экспорт в Excel".
   */
  String ICON_EXCEL = "icons/is16x16/page_excel.png"; //$NON-NLS-1$

  /**
   * Относительный путь к иконке "Печать".
   */
  String ICON_PRINT = "icons/is24x24/print.png"; //$NON-NLS-1$

}
