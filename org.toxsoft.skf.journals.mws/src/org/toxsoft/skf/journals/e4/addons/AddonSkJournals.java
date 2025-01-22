package org.toxsoft.skf.journals.e4.addons;

import static org.toxsoft.skf.journals.ISkJournalsConstants.*;
import static org.toxsoft.skf.journals.e4.uiparts.ISkJournalsHardConstants.*;

import org.eclipse.e4.core.contexts.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.bricks.quant.*;
import org.toxsoft.core.tsgui.mws.bases.*;
import org.toxsoft.skf.journals.*;
import org.toxsoft.uskat.core.gui.utils.*;
import org.toxsoft.uskat.core.impl.*;

/**
 * Plugin adoon.
 *
 * @author hazard157
 */
public class AddonSkJournals
    extends MwsAbstractAddon {

  /**
   * Constructor.
   */
  public AddonSkJournals() {
    super( Activator.PLUGIN_ID );
  }

  // ------------------------------------------------------------------------------------
  // MwsAbstractAddon
  //

  @Override
  protected void initApp( IEclipseContext aAppContext ) {
    // без реализации
  }

  @Override
  protected void initWin( IEclipseContext aWinContext ) {
    ISkJournalsConstants.init( aWinContext );
    // implement access rights
    GuiE4ElementsToAbilitiesBinder binder = new GuiE4ElementsToAbilitiesBinder( new TsGuiContext( aWinContext ) );
    binder.bindPerspective( ABILITYID_JOURNALS_PERSP, E4_VISUAL_ELEM_ID_PERSP_JOURNALS );
    binder.bindMenuElement( ABILITYID_JOURNALS_PERSP, E4_VISUAL_ELEM_ID_MENU_ITEEM_JOURNALS );
    binder.bindToolItem( ABILITYID_JOURNALS_PERSP, E4_VISUAL_ELEM_ID_TOOL_ITEEM_JOURNALS );
    SkCoreUtils.registerCoreApiHandler( binder );
  }

  @Override
  protected void doRegisterQuants( IQuantRegistrator aQuantRegistrator ) {
    aQuantRegistrator.registerQuant( new QuantJournalsMws() );
  }

}
