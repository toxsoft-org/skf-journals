package org.toxsoft.skf.journals.e4.uiparts;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tsgui.widgets.*;
import org.toxsoft.core.tslib.utils.logs.impl.*;
import org.toxsoft.skf.journals.e4.uiparts.main.*;
import org.toxsoft.uskat.core.connection.*;
import org.toxsoft.uskat.core.gui.conn.*;
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
  public ISkConnection skConn() {
    ISkConnectionSupplier cs = tsContext().get( ISkConnectionSupplier.class );
    if( cs != null ) {
      return cs.defConn();
    }
    LoggerUtils.errorLogger().error( "ISkConnectionSupplier - null" ); //$NON-NLS-1$
    return null;
  }

  @Override
  protected void doCreateContent( TsComposite aParent ) {
    ITsGuiContext ctx = new TsGuiContext( tsContext() );
    panel = new JournalsPanel( aParent, ctx );
    panel.setLayoutData( BorderLayout.CENTER );
  }

}
