package org.toxsoft.skf.journals.e4.uiparts.devel.formatters;

import static org.toxsoft.skf.journals.e4.uiparts.devel.formatters.ISkResources.*;

import org.toxsoft.core.tslib.av.*;
import org.toxsoft.uskat.core.api.evserv.*;
import org.toxsoft.uskat.core.api.objserv.*;
import org.toxsoft.uskat.core.api.sysdescr.*;
import org.toxsoft.uskat.core.api.sysdescr.dto.*;

/**
 * Класс осуществляющий форматирование события c парой параметров 'oldVal' & 'newVal'
 *
 * @author dima
 */
public class OldVal2NewValParamsJournalEventFormatter
    extends AbstractJournalEventFormatter {

  String EVPRMID__OLDVAL = "oldVal"; // [Integer] старое значение //$NON-NLS-1$
  String EVPRMID__NEWVAL = "newVal"; // [Integer] новое значение //$NON-NLS-1$

  @Override
  protected String doFormatShortText( SkEvent aEvent, ISkClassInfo aSkClass, IDtoEventInfo aEvInfo,
      ISkObject aAttrObject ) {
    IAtomicValue oldVal = aEvent.paramValues().findByKey( EVPRMID__OLDVAL );
    IAtomicValue newVal = aEvent.paramValues().findByKey( EVPRMID__NEWVAL );

    // формируем описание события
    StringBuilder sb = new StringBuilder();
    sb.append( String.format( " %s => %s ", oldVal.toString(), newVal.toString() ) ); //$NON-NLS-1$
    return sb.toString();
  }

  @Override
  protected String doFormatLongText( SkEvent aEvent, ISkClassInfo aSkClass, IDtoEventInfo aEvInfo,
      ISkObject aAttrObject ) {
    IAtomicValue oldVal = aEvent.paramValues().findByKey( EVPRMID__OLDVAL );
    IAtomicValue newVal = aEvent.paramValues().findByKey( EVPRMID__NEWVAL );

    // формируем описание события
    StringBuilder sb = new StringBuilder();
    // описание объекта
    sb.append( String.format( FMT_OBJ_NAME_DESCR, aEvInfo.nmName(), aAttrObject.description() ) );

    sb.append( String.format( FMT_OLD_TO_NEW, oldVal.toString(), newVal.toString() ) );
    return sb.toString();
  }
}
