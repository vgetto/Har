package co.vgetto.har.syncadapter.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import co.vgetto.har.Constants;
import co.vgetto.har.MyApplication;
import co.vgetto.har.db.entities.Schedule;
import co.vgetto.har.db.tables.HistoryTable;
import co.vgetto.har.db.tables.SchedulesTable;
import co.vgetto.har.db.tables.TriggersTable;
import co.vgetto.har.db.tables.UserTable;
import javax.inject.Inject;

/**
 * Created by Kovje on 8.9.2015..
 */
/*
 * HarProvider is a dummy
 */
public class HarProvider extends ContentProvider {
    /*
     * Always return true, indicating that the
     * provider loaded correctly.
     */

  @Inject SQLiteOpenHelper dbHelper;
  private SQLiteDatabase writeableDatabase, readableDatabase;
  private static final int LOCAL_SCHEDULE = 1;
  private static final int LOCAL_TRIGGER = 2;
  private static final int LOCAL_HISTORY = 3;
  private static final int USER = 4;

  private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);

  static {
    sURIMatcher.addURI(Constants.AUTHORITY, "/schedule", LOCAL_SCHEDULE);
    sURIMatcher.addURI(Constants.AUTHORITY, "/trigger", LOCAL_TRIGGER);
    sURIMatcher.addURI(Constants.AUTHORITY, "/history", LOCAL_HISTORY);
    sURIMatcher.addURI(Constants.AUTHORITY, "/user", USER);
  }

  @Override public boolean onCreate() {
    MyApplication.get(getContext()).getAppComponent().inject(this);
    this.writeableDatabase = dbHelper.getWritableDatabase();
    this.readableDatabase = dbHelper.getReadableDatabase();
    return true;
  }

  /*
   * Return no type for MIME type
   */
  @Override public String getType(Uri uri) {
    return null;
  }

  /*
   * query() always returns no results
   *
   */
  @Override public Cursor query(Uri uri, String[] projection, String selection,
      String[] selectionArgs, String sortOrder) {
    return readableDatabase.query(getTableByUri(uri), projection, selection, selectionArgs, sortOrder, null, null);
  }

  @Override public Uri insert(Uri uri, ContentValues values) {
    long id = writeableDatabase.insert(getTableByUri(uri), null, values);
    if (id != 0) {
      getContext().getContentResolver().notifyChange(uri, null);
      return Uri.withAppendedPath(uri, Long.toString(id));
    }
    return null;
  }

  /*
   * delete() always returns "no rows affected" (0)
   */
  @Override public int delete(Uri uri, String selection, String[] selectionArgs) {
    int rowsAffected = writeableDatabase.delete(getTableByUri(uri), selection, selectionArgs);
    if (rowsAffected != 0) {
      getContext().getContentResolver().notifyChange(uri, null);
    }
    return rowsAffected;
  }

  /*
   * update() always returns "no rows affected" (0)
   */
  public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
    int rowsAffected = writeableDatabase.update(getTableByUri(uri), values, selection, selectionArgs);
    if (rowsAffected != 0) {
      getContext().getContentResolver().notifyChange(uri, null);
    }
    return rowsAffected;
  }

  private String getTableByUri(Uri uri) {
    switch (sURIMatcher.match(uri)) {
      case LOCAL_SCHEDULE:
        return SchedulesTable.TABLE;
      case LOCAL_TRIGGER:
        return TriggersTable.TABLE;
      case USER:
        return UserTable.TABLE;
      default:
        return HistoryTable.TABLE;
    }
  }
}
