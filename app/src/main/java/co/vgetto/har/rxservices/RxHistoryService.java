package co.vgetto.har.rxservices;

import android.content.ContentValues;
import android.content.Context;
import co.vgetto.har.db.Db;
import co.vgetto.har.db.entities.History;
import co.vgetto.har.db.tables.HistoryTable;
import java.util.List;
import rx.Observable;

/**
 * Created by Kovje on 10.9.2015..
 */

/**
 * Helper class for working with history
 */

public final class RxHistoryService {
  private final RxDbService rxDbService;
  private final Context context;

  public RxHistoryService(Context context, RxDbService rxDbService) {
    this.context = context;
    this.rxDbService = rxDbService;
  }

  /**
   *  returns and observable that emit's a list of items every time history table is updated
   *  as long a subscription to this observable is alive
   */
  public Observable<List<History>> getHistoryQueryObservable() {
    return rxDbService.getQueryObservable(History.class).mapToList(History.BRITE_MAPPER);
  }


  /**
   *  returns one history object base on id
   * @param id
   * @return
   */
  public Observable<History> getHistoryById(long id) {
    return rxDbService.get(History.class, History.selectionById, Db.getSelectionArgsForId(id), null)
        .map(History.SINGLE_MAPPER);
  }

  /**
   *  returns a history object based on foreignId and type, foreignId can be the same, as it
   *  references a row in either Schedule or Trigger table
   * @param foreignId
   * @param type
   * @return
   */
  public Observable<History> getHistoryByForeignIdAndType(long foreignId, int type) {
    return rxDbService.get(History.class, History.selectionByForeignIdAndType,
        Db.getSelectionArgsForForeignIdAndType(foreignId, type), null).map(History.SINGLE_MAPPER);
  }

  // no comments needed for these methods..

  public Observable<Long> insertHistory(ContentValues values) {
    return rxDbService.insert(History.class, values);
  }

  public Observable<Integer> editHistory(ContentValues values) {
    return rxDbService.edit(History.class, values, History.selectionById,
        Db.getSelectionArgsForId(values.getAsLong(HistoryTable.ID)));
  }

  public Observable<Integer> editHistoryByForeignId(ContentValues values) {
    return rxDbService.edit(History.class, values, History.selectionByForeignIdAndType,
        Db.getSelectionArgsForForeignIdAndType(
            values.getAsLong(HistoryTable.FOREIGN_ID), values.getAsInteger(HistoryTable.TYPE)));
  }

  public Observable<Integer> deleteHistory(long id) {
    return rxDbService.delete(History.class, History.selectionById,
        Db.getSelectionArgsForId(id));
  }
}
