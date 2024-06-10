package org.toxsoft.skf.journals.e4.uiparts;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tsgui.widgets.*;
import org.toxsoft.skf.journals.e4.uiparts.main.*;
import org.toxsoft.uskat.core.gui.e4.uiparts.*;

/**
 * Журнал событий и команд.
 *
 * @author max
 */
public class UipartJournals
    extends SkMwsAbstractPart {
  // extends MwsAbstractPart {

  JournalsPanel panel;

  @Override
  protected void doCreateContent( TsComposite aParent ) {
    ITsGuiContext ctx = new TsGuiContext( tsContext() );
    panel = new JournalsPanel( aParent, ctx );
    panel.setLayoutData( BorderLayout.CENTER );
  }

}
