package org.toxsoft.skf.journals.e4.uiparts.engine;

import org.eclipse.core.runtime.IProgressMonitor;
import org.toxsoft.core.tslib.bricks.time.ITimeInterval;
import org.toxsoft.core.tslib.coll.IList;
import org.toxsoft.core.tslib.utils.errors.TsIllegalStateRtException;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;
import org.toxsoft.uskat.core.gui.glib.query.ISkQueryCancelProducer;

/**
 * Движок запроса и получения историческийх сущностей (события, команды и т.д.)
 * <p>
 * Движок осуществляет запрос синхрнонно в методе
 * {@link #query(ITimeInterval, IJournalQueryFilter, IProgressMonitor, ISkQueryCancelProducer)}.
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
   * @param aMonitor {@link IProgressMonitor} монитор выполнения запроса
   * @param aCancelProducer {@link ISkQueryCancelProducer} механизм отмены выполнения запроса
   * @return {@link IList} - список полученных сущностей
   * @throws TsNullArgumentRtException любой аргумент = null
   * @throws TsIllegalStateRtException предыдущий запрос еще не завершен
   */
  IList<T> query( ITimeInterval aInterval, IJournalQueryFilter aFilter, IProgressMonitor aMonitor,
      ISkQueryCancelProducer aCancelProducer );

}
