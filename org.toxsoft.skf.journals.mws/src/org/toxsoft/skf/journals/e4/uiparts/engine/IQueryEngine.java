package org.toxsoft.skf.journals.e4.uiparts.engine;

import org.toxsoft.core.tslib.bricks.time.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Движок запроса и получения историческийх сущностей (события, команды и т.д.)
 * <p>
 * Движок осуществляет запрос синхрнонно в методе {@link #query(ITimeInterval, IJournalQueryFilter)}.
 * <p>
 *
 * @author goga
 * @param <T> - класс запрашиваемых историческийх сущностей.
 */
public interface IQueryEngine<T> {

  /**
   * Синхронно запрашивает события у сервера и возвращает их в удобно-отображаемом виде.
   *
   * @param aInterval {@link ITimeInterval} - интервал запроса
   * @param aFilter {@link IConcerningEventsParams} - параметры (события и объекты) запроса
   * @return {@link IList} - список полученных сущностей
   * @throws TsNullArgumentRtException любой аргумент = null
   * @throws TsIllegalStateRtException предыдущий запрос еще не завершен
   */
  IList<T> query( ITimeInterval aInterval, IJournalQueryFilter aFilter );

}
