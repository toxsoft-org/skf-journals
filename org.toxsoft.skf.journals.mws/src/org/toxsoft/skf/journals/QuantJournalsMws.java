package org.toxsoft.skf.journals;

import static org.toxsoft.skf.journals.ISkJournalsConstants.*;

import org.eclipse.e4.core.contexts.*;
import org.toxsoft.core.tsgui.bricks.quant.*;
import org.toxsoft.uskat.core.api.*;
import org.toxsoft.uskat.core.devapi.*;
import org.toxsoft.uskat.core.impl.*;

/**
 * The library quant.
 *
 * @author max
 */
public class QuantJournalsMws
    extends AbstractQuant
    implements ISkCoreExternalHandler {

  /**
   * Constructor.
   */
  public QuantJournalsMws() {
    super( QuantJournalsMws.class.getSimpleName() );
    SkCoreUtils.registerCoreApiHandler( this );
  }

  @Override
  protected void doInitApp( IEclipseContext aAppContext ) {
    // nop
  }

  @Override
  protected void doInitWin( IEclipseContext aWinContext ) {
    // nop
  }

  @Override
  public void processSkCoreInitialization( IDevCoreApi aCoreApi ) {
    // register abilities
    aCoreApi.userService().abilityManager().defineKind( ABKIND_JOURNALS );
    aCoreApi.userService().abilityManager().defineAbility( ABILITY_ACCESS_JOURNALS );
    aCoreApi.userService().abilityManager().defineAbility( ABILITY_JOURNALS_EVENTS );
    aCoreApi.userService().abilityManager().defineAbility( ABILITY_JOURNALS_COMMANDS );
    aCoreApi.userService().abilityManager().defineAbility( ABILITY_JOURNALS_ALARMS );
  }

}
