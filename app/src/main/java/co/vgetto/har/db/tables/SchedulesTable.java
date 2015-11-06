package co.vgetto.har.db.tables;

/**
 * Created by Kovje on 6.9.2015..
 */
public class SchedulesTable {
  public static final String TABLE = "schedules";

  public static final String ID = "_id";
  public static final String STATE = "state"; // SCHEDULE_STATE_WAITING / _STARTED

  // time scheduleConfiguration
  public static final String START_TIME = "startTime";

  // recording scheduleConfiguration
  public static final String DURATION = "duration";
  public static final String DELAY_BETWEEN = "delayBetween";
  public static final String NUMBER_OF_RECORDINGS = "numberOfRecordings";
  public static final String FILE_PREFIX = "filePrefix";
  public static final String FOLDER_NAME = "folderName";

  // upload scheduleConfiguration
  public static final String DROPBOX_UPLOAD = "dropboxUpload";
  public static final String DELETE_AFTER_UPLOAD = "deleteAfterUpload";
  public static final String NOTIFY_ON_START_RECORDING = "notifyOnStartRecording";
  public static final String NOTIFY_ON_END_RECORDING = "notifyOnEndRecording";
  public static final String SAVE_TO_HISTORY = "saveToHistory";

  private static final String scheduleColumns = " (" +
      ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
      START_TIME + " INTEGER, " +
      STATE + " INTEGER, " +
      DELAY_BETWEEN + " INTEGER, " +
      NUMBER_OF_RECORDINGS + " INTEGER, " +
      FILE_PREFIX + " TEXT, " +
      FOLDER_NAME + " TEXT, " +
      DURATION + " INTEGER, " +
      DROPBOX_UPLOAD + " INTEGER, " +
      DELETE_AFTER_UPLOAD + " INTEGER, " +
      NOTIFY_ON_START_RECORDING + " INTEGER, " +
      NOTIFY_ON_END_RECORDING + " INTEGER, " +
      SAVE_TO_HISTORY + " INTEGER " +
      ")";

  public static String getScheduleLocalTableQuery() {
    return "CREATE TABLE " + TABLE + scheduleColumns;
  }

  public static final String[] projection = {
      ID, START_TIME, STATE, DELAY_BETWEEN, NUMBER_OF_RECORDINGS, FILE_PREFIX, FOLDER_NAME,
      DURATION, DROPBOX_UPLOAD, DELETE_AFTER_UPLOAD, NOTIFY_ON_START_RECORDING,
      NOTIFY_ON_END_RECORDING, SAVE_TO_HISTORY
  };
}

