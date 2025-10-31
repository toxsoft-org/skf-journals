package org.toxsoft.skf.journals.e4.uiparts.devel;

import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.gw.gwid.*;

/**
 * Реестер форматтеров для отображения команд системы в журнале.
 *
 * @author dima
 */
public class DefaultMwsModJournalCommandFormattersRegistry
    implements ISkModJournalCommandFormattersRegistry {

  private IMapEdit<Gwid, ISkModJournalCommandFormatter> formattersMap = new ElemMap<>();

  @Override
  public ISkModJournalCommandFormatter find( String aClassId, String aCmdId ) {
    Gwid gwid = Gwid.createCmd( aClassId, aCmdId );
    return formattersMap.findByKey( gwid );
  }

  @Override
  public IMap<Gwid, ISkModJournalCommandFormatter> formattersMap() {
    return formattersMap;
  }

  @Override
  public void registerFomatter( Gwid aCmdGwid, ISkModJournalCommandFormatter aFormatter ) {
    formattersMap.put( aCmdGwid, aFormatter );
  }

  @Override
  public void unregisterFormatter( Gwid aCmdGwid ) {
    formattersMap.removeByKey( aCmdGwid );
  }
}
