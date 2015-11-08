package co.vgetto.har.rxservices;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.util.Pair;
import co.vgetto.har.Constants;
import co.vgetto.har.db.entities.Schedule;
import co.vgetto.har.db.entities.Trigger;
import co.vgetto.har.db.tables.HistoryTable;
import co.vgetto.har.db.tables.SchedulesTable;
import co.vgetto.har.db.tables.TriggersTable;
import com.fernandocejas.frodo.annotation.RxLogObservable;
import com.squareup.sqlbrite.BriteContentResolver;
import com.squareup.sqlbrite.QueryObservable;
import rx.Observable;

/**
 * Created by Kovje on 28.10.2015..
 */
public class RxDbService {
  // todo make other db related services extends this one, make it base
  private final ContentResolver contentResolver;
  private final BriteContentResolver briteContentResolver;

  public RxDbService(ContentResolver contentResolver, BriteContentResolver briteContentResolver) {
    this.contentResolver = contentResolver;
    this.briteContentResolver = briteContentResolver;
  }

  /**
   * based on input Class return a pair containing URI and projection for that type
   * @param c
   * @return
   */
  private Pair<Uri, String[]> getUriAndProjectionForClass(Class c) {
    if (c.equals(Schedule.class)) {
      return new Pair<>(Constants.BASE_SCHEDULE_URI, SchedulesTable.projection);
    } else if (c.equals(Trigger.class)) {
      return new Pair<>(Constants.BASE_TRIGGER_URI, TriggersTable.projection);
    } else {
      return new Pair<>(Constants.BASE_HISTORY_URI, HistoryTable.projection);
    }
  }

  /**
   * return a query observable (sqlbrite) based on input, should be mapped before subscribed to
   * @param input
   * @return
   */
  public final QueryObservable getQueryObservable(Class input) {
    Pair<Uri, String[]> queryData = getUriAndProjectionForClass(input);
    return briteContentResolver.createQuery(queryData.first, queryData.second, null, null, null,
        true);
  }

  /**
   *  return cursor based on Class, selection, selection args and sort order
   */
  public final Observable<Cursor> get(Class c, String selection, String[] selectionArgs, String sortOrder) {
    Pair<Uri, String[]> uriProjection = getUriAndProjectionForClass(c);
    return Observable.just(
        contentResolver.query(uriProjection.first, uriProjection.second, selection, selectionArgs,
            sortOrder));
  }

  /**
   * return ID for inserted item
   */
  public final Observable<Long> insert(Class c, ContentValues values) {
    return Observable.just(contentResolver.insert(getUriAndProjectionForClass(c).first, values))
        .map(uri -> Long.valueOf(uri.getLastPathSegment()));
  }

  /**
   *  delete row(s) base on class type, selection and selection args, return number of rows affected, should be only 1
   */
  public final Observable<Integer> delete(Class c, String selection, String[] selectionArgs) {
    return Observable.just(
        contentResolver.delete(getUriAndProjectionForClass(c).first, selection, selectionArgs));
  }

  /**
   * edit row based on class, values, selection and selection args
   */
  public final Observable<Integer> edit(Class c, ContentValues values, String selection,
      String[] selectionArgs) {
    return Observable.just(
        contentResolver.update(getUriAndProjectionForClass(c).first, values, selection,
            selectionArgs));
  }
}
