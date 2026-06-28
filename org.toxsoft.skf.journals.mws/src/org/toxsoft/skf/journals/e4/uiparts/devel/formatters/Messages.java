package org.toxsoft.skf.journals.e4.uiparts.devel.formatters;

import org.eclipse.osgi.util.*;

public class Messages
    extends NLS {

  private static final String BUNDLE_NAME = Messages.class.getPackageName() + ".messages"; //$NON-NLS-1$
  public static String        FMT_OBJ_NAME_DESCR;
  public static String        FMT_OLD_TO_NEW;
  static {
    // initialize resource bundle
    NLS.initializeMessages( BUNDLE_NAME, Messages.class );
  }

  private Messages() {
  }
}
