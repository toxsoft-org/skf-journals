package org.toxsoft.skf.journals.e4.uiparts.devel;

import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.gw.gwid.*;

/**
 * Реестер форматтеров для отображения событий системы в журнале.
 *
 * @author max
 */
public class DefaultMwsModJournalEventFormattersRegistry
    implements ISkModJournalEventFormattersRegistry {

  private IMapEdit<Gwid, ISkModJournalEventFormatter> formattersMap = new ElemMap<>();

  @Override
  public ISkModJournalEventFormatter find( String aClassId, String aEventId ) {
    Gwid gwid = Gwid.createEvent( aClassId, aEventId );
    return formattersMap.findByKey( gwid );
  }

  @Override
  public IMap<Gwid, ISkModJournalEventFormatter> formattersMap() {
    return formattersMap;
  }

  @Override
  public void registerFomatter( Gwid aEventGwid, ISkModJournalEventFormatter aFormatter ) {
    formattersMap.put( aEventGwid, aFormatter );
  }

  @Override
  public void unregisterFormatter( Gwid aEventGwid ) {
    formattersMap.removeByKey( aEventGwid );
  }
}
