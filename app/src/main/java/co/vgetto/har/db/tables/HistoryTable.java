package co.vgetto.har.db.tables;

/**
 * Created by Kovje on 6.9.2015..
 */
public class HistoryTable {
  public static final String TABLE = "history";

  public static final String ID = "_id";
  public static final String FOREIGN_ID = "foreignId";
  public static final String TYPE = "type";
  public static final String STATE = "state";
  public static final String STARTED_RECORDING_DATE = "startedRecordingDate";
  public static final String ENDED_RECORDING_DATE = "finishedRecordingDate";
  public static final String SAVED_FILES = "savedFiles";


  private static final String historyColumns = " (" +
      ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
      FOREIGN_ID + " INTEGER, " +
      TYPE + " INTEGER, " +
      STATE + " INTEGER, " +
      STARTED_RECORDING_DATE + " INTEGER, " +
      ENDED_RECORDING_DATE + " INTEGER, " +
      SAVED_FILES + " TEXT" +
      ")";

  public static String getLocalHistoryTableQuery() {
    return "CREATE TABLE " + TABLE + historyColumns;
  }

  public static final String[] projection = {
      ID, FOREIGN_ID, TYPE, STATE, STARTED_RECORDING_DATE, ENDED_RECORDING_DATE, SAVED_FILES
  };
}

