package org.toxsoft.skf.journals.e4.uiparts.devel.formatters;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.skf.journals.e4.uiparts.devel.*;
import org.toxsoft.uskat.core.api.evserv.*;
import org.toxsoft.uskat.core.api.objserv.*;
import org.toxsoft.uskat.core.api.sysdescr.*;
import org.toxsoft.uskat.core.api.sysdescr.dto.*;
import org.toxsoft.uskat.core.connection.*;
import org.toxsoft.uskat.core.gui.conn.*;

/**
 * Базовый класс для форматеров событий
 *
 * @author dima
 */
public abstract class AbstractJournalEventFormatter
    implements ISkModJournalEventFormatter {

  @Override
  public String formatShortText( SkEvent aEvent, ITsGuiContext aContext ) {
    ISkConnectionSupplier conSupp = aContext.get( ISkConnectionSupplier.class );
    ISkConnection conn = conSupp.defConn();

    // Получаем его класс
    ISkClassInfo skClass = conn.coreApi().sysdescr().findClassInfo( aEvent.eventGwid().classId() );
    // Описание события
    IDtoEventInfo evInfo = skClass.events().list().findByKey( aEvent.eventGwid().propId() );
    // Получаем объект атрибута
    ISkObject attrObject = conn.coreApi().objService().find( aEvent.eventGwid().skid() );

    return doFormatShortText( aEvent, skClass, evInfo, attrObject );
  }

  @Override
  public String formatLongText( SkEvent aEvent, ITsGuiContext aContext ) {
    ISkConnectionSupplier conSupp = aContext.get( ISkConnectionSupplier.class );
    ISkConnection conn = conSupp.defConn();

    // Получаем его класс
    ISkClassInfo skClass = conn.coreApi().sysdescr().findClassInfo( aEvent.eventGwid().classId() );
    // Описание события
    IDtoEventInfo evInfo = skClass.events().list().findByKey( aEvent.eventGwid().propId() );
    // Получаем объект атрибута
    ISkObject attrObject = conn.coreApi().objService().find( aEvent.eventGwid().skid() );

    return doFormatLongText( aEvent, skClass, evInfo, attrObject );
  }

  protected abstract String doFormatShortText( SkEvent aEvent, ISkClassInfo aSkClass, IDtoEventInfo aEvInfo,
      ISkObject aAttrObject );

  protected abstract String doFormatLongText( SkEvent aEvent, ISkClassInfo aSkClass, IDtoEventInfo aEvInfo,
      ISkObject aAttrObject );
}
