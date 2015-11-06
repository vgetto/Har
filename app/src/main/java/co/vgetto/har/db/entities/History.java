package co.vgetto.har.db.entities;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcelable;
import auto.parcel.AutoParcel;
import co.vgetto.har.db.Db;
import co.vgetto.har.db.tables.HistoryTable;
import java.util.List;
import rx.functions.Func1;

/**
 * Created by Kovje on 21.8.2015..
 */
@AutoParcel public abstract class History implements Parcelable {
  public abstract long id(); // history event id

  public abstract long foreignId();

  public abstract int type(); // type 0 - schedule, 1 trigger

  public abstract int state(); // 0 - still recording, 1 - recorded successfull, -1 error when rec.

  public abstract long startedRecordingDate(); // when recording started

  public abstract long endedRecordingDate(); // when recording finished

  public abstract List<SavedFile> savedFiles(); // list of file paths to saved files

  public static int HISTORY_TYPE_SCHEDULE = 0;
  public static int HISTORY_TYPE_TRIGGER = 1;

  public static int HISTORY_STATE_RECORDING = 2;
  public static int HISTORY_STATE_SUCCESSFUL = 3;
  public static int HISTORY_STATE_ERROR_WHEN_RECORDING = -1;

  public static final String selectionByForeignIdAndType = HistoryTable.FOREIGN_ID + " = ? AND " + HistoryTable.TYPE + " = ?";

  public static final String selectionById = HistoryTable.ID + " = ?";

  public static History create(long id,long foreignId, int type, int state, long startedRecordingDate,
      long endedRecordingDate, List<SavedFile> savedFiles) {
    return new AutoParcel_History(id, foreignId, type, state, startedRecordingDate, endedRecordingDate,
        savedFiles);
  }

  public static final Func1<Cursor, History> SINGLE_MAPPER = new Func1<Cursor, History>() {
    @Override public History call(Cursor cursor) {
      if (cursor.getCount() > 0) {
        cursor.moveToFirst();

        long id = Db.getLong(cursor, HistoryTable.ID);
        long foreignId = Db.getLong(cursor, HistoryTable.FOREIGN_ID);
        int type = Db.getInt(cursor, HistoryTable.TYPE);
        int state = Db.getInt(cursor, HistoryTable.STATE);
        long startedRecordingDate = Db.getLong(cursor, HistoryTable.STARTED_RECORDING_DATE);
        long endedRecordingDate = Db.getLong(cursor, HistoryTable.ENDED_RECORDING_DATE);
        List<SavedFile> savedFiles = Db.convertStringToSavedFilesList(
            Db.getString(cursor, HistoryTable.SAVED_FILES));

        cursor.close();

        return new AutoParcel_History(id, foreignId, type, state, startedRecordingDate, endedRecordingDate, savedFiles);
      }
      cursor.close();
      return null;
    }
  };

  public static final Func1<Cursor, History> BRITE_MAPPER = new Func1<Cursor, History>() {
    @Override public History call(Cursor cursor) {
      long id = Db.getLong(cursor, HistoryTable.ID);
      long foreignId = Db.getLong(cursor, HistoryTable.FOREIGN_ID);
      int type = Db.getInt(cursor, HistoryTable.TYPE);
      int state = Db.getInt(cursor, HistoryTable.STATE);
      long startedRecordingDate = Db.getLong(cursor, HistoryTable.STARTED_RECORDING_DATE);
      long endedRecordingDate = Db.getLong(cursor, HistoryTable.ENDED_RECORDING_DATE);
      List<SavedFile> savedFiles = Db.convertStringToSavedFilesList(
          Db.getString(cursor, HistoryTable.SAVED_FILES));

      return new AutoParcel_History(id, foreignId, type, state, startedRecordingDate, endedRecordingDate, savedFiles);
    }
  };

  public ContentValues getContentValues() {
    return new ContentValuesBuilder().id(id())
        .type(type())
        .state(state())
        .startedRecordingDate(startedRecordingDate())
        .endedRecordingDate(endedRecordingDate())
        .savedFiles(savedFiles())
        .build();
  }

  public static final class ContentValuesBuilder {
    private final ContentValues values = new ContentValues();

    public ContentValuesBuilder id(long id) {
      values.put(HistoryTable.ID, id);
      return this;
    }

    public ContentValuesBuilder foreignId(long foreignId) {
      values.put(HistoryTable.FOREIGN_ID, foreignId);
      return this;
    }

    public ContentValuesBuilder type(int type) {
      values.put(HistoryTable.TYPE, type);
      return this;
    }

    public ContentValuesBuilder state(int state) {
      values.put(HistoryTable.STATE, state);
      return this;
    }


    public ContentValuesBuilder startedRecordingDate(long startedRecordingDate) {
      values.put(HistoryTable.STARTED_RECORDING_DATE, startedRecordingDate);
      return this;
    }

    public ContentValuesBuilder endedRecordingDate(long endedRecordingDate) {
      values.put(HistoryTable.ENDED_RECORDING_DATE, endedRecordingDate);
      return this;
    }

    public ContentValuesBuilder savedFiles(List<SavedFile> savedFiles) {
      values.put(HistoryTable.SAVED_FILES, Db.convertSavedFilesListToString(savedFiles));
      return this;
    }

    public ContentValues build() {
      return values; // TODO defensive copy?
    }
  }
}
