package org.toxsoft.skf.journals.e4.uiparts.main;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.panels.*;
import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.skf.alarms.gui.panels.impl.*;

/**
 * Панель просмотра журнала тревог
 *
 * @author max
 */
public class AlarmsJournalPanel
    extends TsPanel
    implements IGenericChangeListener {

  /**
   * @param aParent родительская панель
   * @param aContext контекст
   * @throws TsException - ошибка создания
   */
  public AlarmsJournalPanel( Composite aParent, ITsGuiContext aContext )
      throws TsException {
    super( aParent, aContext );
    setLayout( new BorderLayout() );

    // initialize SWT
    SashForm sfMain = new SashForm( this, SWT.VERTICAL );

    AlertRtPanel alertPanel = new AlertRtPanel( aContext );
    alertPanel.createControl( sfMain );
    alertPanel.refresh();

    AlarmJournalPanel alarmJournalPanel = new AlarmJournalPanel( aContext );
    alarmJournalPanel.createControl( sfMain );
  }

  @Override
  public void onGenericChangeEvent( Object aSource ) {
    // IJournalParamsPanel journalPanel = (IJournalParamsPanel)aSource;
    // if( journalPanel.currentAction() == ECurrentAction.QUERY_ALL ) {
    // queryAlarms( true );
    // }
    // if( journalPanel.currentAction() == ECurrentAction.QUERY_SELECTED ) {
    // queryAlarms( false );
    // }
    // if( journalPanel.currentAction() == ECurrentAction.PRINT ) {
    // printAlarms();
    // }
  }
}
