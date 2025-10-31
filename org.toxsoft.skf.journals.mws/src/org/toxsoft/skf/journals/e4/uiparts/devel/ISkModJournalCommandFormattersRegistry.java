package org.toxsoft.skf.journals.e4.uiparts.devel;

import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.gw.gwid.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Реестр форматировщиков текста для отображения событий.
 * <p>
 * Этот модуль (плагин) работы с журналами использует ссылку на этот реестр, который должен находится в контексте
 * приложения. Если модель при старте не обнаруживает ссылку в контексте, то он сам создает и размещает в контекст.
 * Форматировщики из этого реестра используются для отображения и печати событий в журналах.
 *
 * @author dima
 */
public interface ISkModJournalCommandFormattersRegistry {

  /**
   * Находит форматировщик для запрошенного события.
   *
   * @param aClassId String - идентификатор класса
   * @param aCmdId String - идентификатор команд
   * @return {@link ISkModJournalEventFormatter} - найденный форматировщик или <code>null</code>
   */
  ISkModJournalCommandFormatter find( String aClassId, String aCmdId );

  /**
   * Возвращает все зарегистрированные форматировщики.
   *
   * @return {@link IMap}&lt;{@link Gwid},{@link ISkModJournalCommandFormatter}&gt; - карта Gwid - форматировщик
   */
  IMap<Gwid, ISkModJournalCommandFormatter> formattersMap();

  /**
   * Регистрирует форматировщик.
   * <p>
   * Заменяет существующий форматировщик.
   *
   * @param aCmdGwid {@link Gwid} - UGWI команды регистрируемого форматировщика
   * @param aFormatter {@link ISkModJournalCommandFormatter} - регистрируемый форматировщик
   * @throws TsNullArgumentRtException любой аргумент = <code>null</code>
   * @throws TsIllegalArgumentRtException аргумент-GWID не имеет тип {@link EGwidKind#GW_CMD}
   */
  void registerFomatter( Gwid aCmdGwid, ISkModJournalCommandFormatter aFormatter );

  /**
   * Удаляет зарегистрированный форматировщик.
   * <p>
   * Если под запрошенным GWID форматировщик не был зарегистрирован, метод ничего не делает.
   *
   * @param aCmdGwid {@link Gwid} - GWID команды удаляемого форматировщика
   * @throws TsNullArgumentRtException любой аргумент = <code>null</code>
   */
  void unregisterFormatter( Gwid aCmdGwid );

  /**
   * Находит форматировщик для запрошенного события.
   *
   * @param aCmdGwid {@link Gwid} - GWID команды
   * @return {@link ISkModJournalEventFormatter} - найденный форматировщик или <code>null</code>
   * @throws TsNullArgumentRtException любой аргумент = <code>null</code>
   * @throws TsIllegalArgumentRtException аргумент-GWID не имеет тип {@link EGwidKind#GW_CMD}
   */
  default ISkModJournalCommandFormatter find( Gwid aCmdGwid ) {
    TsNullArgumentRtException.checkNull( aCmdGwid );
    TsIllegalArgumentRtException.checkTrue( aCmdGwid.kind() == EGwidKind.GW_CMD );
    return find( aCmdGwid.classId(), aCmdGwid.strid() );
  }
}
