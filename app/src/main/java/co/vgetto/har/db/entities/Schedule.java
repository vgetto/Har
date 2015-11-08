package co.vgetto.har.db.entities;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcelable;
import auto.parcel.AutoParcel;
import co.vgetto.har.db.Db;
import co.vgetto.har.db.entities.configurations.RecordingConfiguration;
import co.vgetto.har.db.entities.configurations.ScheduleConfiguration;
import co.vgetto.har.db.entities.configurations.UploadConfiguration;
import co.vgetto.har.db.tables.SchedulesTable;
import co.vgetto.har.ui.base.BaseModel;
import rx.functions.Func1;

/**
 * Created by Kovje on 21.8.2015..
 */
@AutoParcel public abstract class Schedule implements Parcelable {
  public static final int SCHEDULE_STATE_WAITING = 0;
  public static final int SCHEDULE_STATE_STARTED = 1;
  public static final String selectionById = SchedulesTable.ID + " = ?";

  public abstract long id();

  public abstract int state();

  public abstract ScheduleConfiguration scheduleConfiguration();

  public static Schedule create(long id, int state, ScheduleConfiguration scheduleConfiguration) {
    return new AutoParcel_Schedule(id, state, scheduleConfiguration);
  }

  public static Schedule getScheduleFromCursor(Cursor cursor) {
    long id = Db.getLong(cursor, SchedulesTable.ID);
    int state = Db.getInt(cursor, SchedulesTable.STATE);
    long startTime = Db.getLong(cursor, SchedulesTable.START_TIME);
    int duration = Db.getInt(cursor, SchedulesTable.DURATION);
    int delayBetween = Db.getInt(cursor, SchedulesTable.DELAY_BETWEEN);
    int numberOfRecordings = Db.getInt(cursor, SchedulesTable.NUMBER_OF_RECORDINGS);
    String filePrefix = Db.getString(cursor, SchedulesTable.FILE_PREFIX);
    String folderName = Db.getString(cursor, SchedulesTable.FOLDER_NAME);
    boolean dropboxUpload = Db.getBoolean(cursor, SchedulesTable.DROPBOX_UPLOAD);
    boolean deleteAfterUpload = Db.getBoolean(cursor, SchedulesTable.DELETE_AFTER_UPLOAD);
    boolean notifyOnStartRecording =
        Db.getBoolean(cursor, SchedulesTable.NOTIFY_ON_START_RECORDING);
    boolean notifyOnEndRecording = Db.getBoolean(cursor, SchedulesTable.NOTIFY_ON_END_RECORDING);
    boolean saveToHistory = Db.getBoolean(cursor, SchedulesTable.SAVE_TO_HISTORY);

    RecordingConfiguration recordingConfiguration =
        RecordingConfiguration.create(duration, delayBetween, numberOfRecordings, filePrefix,
            folderName);

    UploadConfiguration uploadConfiguration =
        UploadConfiguration.create(dropboxUpload, deleteAfterUpload, notifyOnStartRecording,
            notifyOnEndRecording, saveToHistory);

    ScheduleConfiguration scheduleConfiguration =
        ScheduleConfiguration.create(startTime, recordingConfiguration, uploadConfiguration);

    return new AutoParcel_Schedule(id, state, scheduleConfiguration);
  }

  public static final Func1<Cursor, Schedule> SINGLE_MAPPER = new Func1<Cursor, Schedule>() {
    @Override public Schedule call(Cursor cursor) {
      if (cursor.getCount() > 0) {
        cursor.moveToFirst();
        Schedule s = getScheduleFromCursor(cursor);
        cursor.close();
        return s;
      }
      cursor.close();
      return null;
    }
  };

  public static final Func1<Cursor, Schedule> BRITE_MAPPER = new Func1<Cursor, Schedule>() {
    @Override public Schedule call(Cursor cursor) {
      return getScheduleFromCursor(cursor);
    }
  };

  public ContentValues getContentValues() {
    return new ContentValuesBuilder().id(id())
        .state(state())
        .scheduleConfiguration(scheduleConfiguration())
        .build();
  }

  public static final class ContentValuesBuilder {
    private final ContentValues values = new ContentValues();

    public ContentValuesBuilder id(long id) {
      values.put(SchedulesTable.ID, id);
      return this;
    }

    public ContentValuesBuilder state(int state) {
      values.put(SchedulesTable.STATE, state);
      return this;
    }

    public ContentValuesBuilder scheduleConfiguration(ScheduleConfiguration scheduleConfiguration) {
      values.put(SchedulesTable.START_TIME, scheduleConfiguration.startTime());

      RecordingConfiguration recordingConfiguration =
          scheduleConfiguration.recordingConfiguration();
      UploadConfiguration uploadConfiguration = scheduleConfiguration.uploadConfiguration();

      values.put(SchedulesTable.DURATION, recordingConfiguration.duration());
      values.put(SchedulesTable.DELAY_BETWEEN, recordingConfiguration.delayBetween());
      values.put(SchedulesTable.NUMBER_OF_RECORDINGS, recordingConfiguration.numOfRecordings());
      values.put(SchedulesTable.FILE_PREFIX, recordingConfiguration.filePrefix());
      values.put(SchedulesTable.FOLDER_NAME, recordingConfiguration.folderName());

      values.put(SchedulesTable.DROPBOX_UPLOAD, uploadConfiguration.dropboxUpload());
      values.put(SchedulesTable.DELETE_AFTER_UPLOAD, uploadConfiguration.deleteAfterUpload());
      values.put(SchedulesTable.NOTIFY_ON_START_RECORDING,
          uploadConfiguration.notifyOnStartRecording());
      values.put(SchedulesTable.NOTIFY_ON_END_RECORDING,
          uploadConfiguration.notifyOnEndRecording());
      values.put(SchedulesTable.SAVE_TO_HISTORY, uploadConfiguration.saveToHistory());
      return this;
    }

    public ContentValues build() {
      return values; // TODO defensive copy?
    }
  }
}
