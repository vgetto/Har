package co.vgetto.har.db.entities;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcelable;
import auto.parcel.AutoParcel;
import co.vgetto.har.db.Db;
import co.vgetto.har.db.entities.configurations.RecordingConfiguration;
import co.vgetto.har.db.entities.configurations.TriggerConfiguration;
import co.vgetto.har.db.entities.configurations.UploadConfiguration;
import co.vgetto.har.db.tables.TriggersTable;
import rx.functions.Func1;

/**
 * Created by Kovje on 21.8.2015..
 */
@AutoParcel public abstract class Trigger implements Parcelable {
  public static final String selectionById = TriggersTable.ID + " = ?";
  public static final String selectionByTypeAndPhoneNumber = TriggersTable.TRIGGER_TYPE + " = ? AND " + TriggersTable.TRIGGER_PHONE_NUMBER + " = ?";

  public abstract long id();
  public abstract TriggerConfiguration triggerConfiguration();

  public static Trigger create(long id, TriggerConfiguration triggerConfiguration) {
    return new AutoParcel_Trigger(id, triggerConfiguration);
  }

  public static Trigger getTriggerFromCursor(Cursor cursor) {
    long id = Db.getLong(cursor, TriggersTable.ID);
    int triggerType = Db.getInt(cursor, TriggersTable.TRIGGER_TYPE);
    String triggerPhoneNumber = Db.getString(cursor, TriggersTable.TRIGGER_PHONE_NUMBER);
    String triggerSmsText = Db.getString(cursor, TriggersTable.TRIGGER_SMS_TEXT);
    int duration = Db.getInt(cursor, TriggersTable.DURATION);
    int delayBetween = Db.getInt(cursor, TriggersTable.DELAY_BETWEEN);
    int numberOfRecordings = Db.getInt(cursor, TriggersTable.NUMBER_OF_RECORDINGS);
    String filePrefix = Db.getString(cursor, TriggersTable.FILE_PREFIX);
    String folderName = Db.getString(cursor, TriggersTable.FOLDER_NAME);
    boolean dropboxUpload = Db.getBoolean(cursor, TriggersTable.DROPBOX_UPLOAD);
    boolean deleteAfterUpload = Db.getBoolean(cursor, TriggersTable.DELETE_AFTER_UPLOAD);
    boolean notifyOnStartRecording =
        Db.getBoolean(cursor, TriggersTable.NOTIFY_ON_START_RECORDING);
    boolean notifyOnEndRecording = Db.getBoolean(cursor, TriggersTable.NOTIFY_ON_END_RECORDING);
    boolean saveToHistory = Db.getBoolean(cursor, TriggersTable.SAVE_TO_HISTORY);

    RecordingConfiguration recordingConfiguration =
        RecordingConfiguration.create(duration, delayBetween, numberOfRecordings, filePrefix,
            folderName);

    UploadConfiguration uploadConfiguration =
        UploadConfiguration.create(dropboxUpload, deleteAfterUpload, notifyOnStartRecording,
            notifyOnEndRecording, saveToHistory);

    TriggerConfiguration triggerConfiguration =
        TriggerConfiguration.create(triggerType, triggerPhoneNumber, triggerSmsText, recordingConfiguration,
            uploadConfiguration);
    return new AutoParcel_Trigger(id, triggerConfiguration);
  }

  public static final Func1<Cursor, Trigger> SINGLE_MAPPER = new Func1<Cursor, Trigger>() {
    @Override public Trigger call(Cursor cursor) {
      if (cursor.getCount() > 0) {
        cursor.moveToFirst();
        Trigger t = getTriggerFromCursor(cursor);
        cursor.close();
        return t;
      }
      cursor.close();
      return null;
    }
  };

  public static final Func1<Cursor, Trigger> BRITE_MAPPER = new Func1<Cursor, Trigger>() {
    @Override public Trigger call(Cursor cursor) {
      return getTriggerFromCursor(cursor);
    }
  };

  public ContentValues getContentValues() {
    return new ContentValuesBuilder().id(id())
        .triggerConfiguration(triggerConfiguration())
        .build();
  }

  public static final class ContentValuesBuilder {
    private final ContentValues values = new ContentValues();

    public ContentValuesBuilder id(long id) {
      values.put(TriggersTable.ID, id);
      return this;
    }

    public ContentValuesBuilder triggerConfiguration(TriggerConfiguration triggerConfiguration) {
      values.put(TriggersTable.TRIGGER_TYPE, triggerConfiguration.type());
      values.put(TriggersTable.TRIGGER_PHONE_NUMBER, triggerConfiguration.phoneNumber());
      values.put(TriggersTable.TRIGGER_SMS_TEXT, triggerConfiguration.smsText());

      RecordingConfiguration recordingConfiguration = triggerConfiguration.recordingConfiguration();
      UploadConfiguration uploadConfiguration = triggerConfiguration.uploadConfiguration();

      values.put(TriggersTable.DURATION, recordingConfiguration.duration());
      values.put(TriggersTable.DELAY_BETWEEN, recordingConfiguration.delayBetween());
      values.put(TriggersTable.NUMBER_OF_RECORDINGS, recordingConfiguration.numOfRecordings());
      values.put(TriggersTable.FILE_PREFIX, recordingConfiguration.filePrefix());
      values.put(TriggersTable.FOLDER_NAME, recordingConfiguration.folderName());

      values.put(TriggersTable.DROPBOX_UPLOAD, uploadConfiguration.dropboxUpload());
      values.put(TriggersTable.DELETE_AFTER_UPLOAD, uploadConfiguration.deleteAfterUpload());
      values.put(TriggersTable.NOTIFY_ON_START_RECORDING,
          uploadConfiguration.notifyOnStartRecording());
      values.put(TriggersTable.NOTIFY_ON_END_RECORDING, uploadConfiguration.notifyOnEndRecording());
      values.put(TriggersTable.SAVE_TO_HISTORY, uploadConfiguration.saveToHistory());
      return this;
    }

    public ContentValues build() {
      return values; // TODO defensive copy?
    }
  }
}
