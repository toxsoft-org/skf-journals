package org.toxsoft.skf.journals.e4.addons;

import org.eclipse.e4.core.contexts.*;
import org.toxsoft.core.tsgui.mws.bases.*;
import org.toxsoft.skf.journals.*;

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
  }

}
