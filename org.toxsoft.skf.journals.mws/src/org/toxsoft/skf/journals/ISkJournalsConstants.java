package org.toxsoft.skf.journals;

import org.eclipse.e4.core.contexts.*;
import org.toxsoft.core.tsgui.graphics.icons.*;

/**
 * Plugin constants.
 * <p>
 *
 * @author hazard157
 */
@SuppressWarnings( "javadoc" )
public interface ISkJournalsConstants {

  // ------------------------------------------------------------------------------------
  // E4

  String PERSPID_JOURNALS = "org.toxsoft.skf.journals.perps"; //$NON-NLS-1$

  // ------------------------------------------------------------------------------------
  // Icons

  String PREFIX_OF_ICON_FIELD_NAME = "ICONID_"; //$NON-NLS-1$
  // String ICONID_APP_ICON = "app-icon"; //$NON-NLS-1$

  /**
   * Constants registration.
   *
   * @param aWinContext {@link IEclipseContext} - windows level context
   */
  static void init( IEclipseContext aWinContext ) {
    ITsIconManager iconManager = aWinContext.get( ITsIconManager.class );
    iconManager.registerStdIconByIds( Activator.PLUGIN_ID, ISkJournalsConstants.class, PREFIX_OF_ICON_FIELD_NAME );
  }

}
