package org.toxsoft.skf.journals.e4.uiparts.devel;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.uskat.core.api.cmdserv.*;

/**
 * Интерфейс форматировщика текста для отображения команд в журнале.
 *
 * @author dima
 */
public interface ISkModJournalCommandFormatter {

  /**
   * Формирует однострочный текст описания исполнителя команды.
   *
   * @param aEntity {@link IDtoCompletedCommand} - отображаемая команда
   * @param aContext {@link ITsGuiContext} - контекст приложения
   * @return String - сформированный текст, не бывает <code>null</code>
   */
  String formatExecuterText( IDtoCompletedCommand aEntity, ITsGuiContext aContext );

  /**
   * Формирует однострочный текст для описания самой команды.
   *
   * @param aEntity {@link IDtoCompletedCommand} - отображаемая команда
   * @param aContext {@link ITsGuiContext} - контекст приложения
   * @return String - сформированный текст, не бывает <code>null</code>
   */
  String formatVisualName( IDtoCompletedCommand aEntity, ITsGuiContext aContext );

}
