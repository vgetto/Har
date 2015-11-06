package co.vgetto.har.db.entities;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcelable;
import auto.parcel.AutoParcel;
import co.vgetto.har.db.Db;
import co.vgetto.har.db.entities.configurations.RecordingConfiguration;
import co.vgetto.har.db.entities.configurations.TriggerConfiguration;
import co.vgetto.har.db.entities.configurations.UploadConfiguration;
import co.vgetto.har.db.tables.SchedulesTable;
import co.vgetto.har.db.tables.TriggersTable;
import rx.functions.Func1;

/**
 * Created by Kovje on 21.8.2015..
 */
@AutoParcel public abstract class Trigger implements Parcelable {
  public abstract long id();

  public static final String selectionById = TriggersTable.ID + " = ?";

  public abstract TriggerConfiguration triggerConfiguration();

  public static Trigger create(long id, TriggerConfiguration triggerConfiguration) {
    return new AutoParcel_Trigger(id, triggerConfiguration);
  }

  public static final Func1<Cursor, Trigger> SINGLE_MAPPER = new Func1<Cursor, Trigger>() {
    @Override public Trigger call(Cursor cursor) {
      if (cursor.getCount() > 0) {
        cursor.moveToFirst();

        long id = Db.getLong(cursor, TriggersTable.ID);
        int triggerType = Db.getInt(cursor, TriggersTable.TRIGGER_TYPE);
        String triggerNumber = Db.getString(cursor, TriggersTable.TRIGGER_NUMBER);
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

        cursor.close();

        RecordingConfiguration recordingConfiguration =
            RecordingConfiguration.create(duration, delayBetween, numberOfRecordings, filePrefix,
                folderName);

        UploadConfiguration uploadConfiguration =
            UploadConfiguration.create(dropboxUpload, deleteAfterUpload, notifyOnStartRecording,
                notifyOnEndRecording, saveToHistory);

        TriggerConfiguration triggerConfiguration =
            TriggerConfiguration.create(triggerType, triggerNumber, recordingConfiguration,
                uploadConfiguration);
        return new AutoParcel_Trigger(id, triggerConfiguration);
      }
      cursor.close();
      return null;
    }
  };

  public static final Func1<Cursor, Trigger> BRITE_MAPPER = new Func1<Cursor, Trigger>() {
    @Override public Trigger call(Cursor cursor) {
      long id = Db.getLong(cursor, TriggersTable.ID);
      int triggerType = Db.getInt(cursor, TriggersTable.TRIGGER_TYPE);
      String triggerNumber = Db.getString(cursor, TriggersTable.TRIGGER_NUMBER);
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
          TriggerConfiguration.create(triggerType, triggerNumber, recordingConfiguration,
              uploadConfiguration);

      cursor.close(); // todo close it here ?

      return new AutoParcel_Trigger(id, triggerConfiguration);
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
      values.put(TriggersTable.TRIGGER_NUMBER, triggerConfiguration.number());

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

  //local schedulei , ako je remote prilikom svake promjene objekata u lokalnoj bazi salji promjenu
  //novi schedule -> uzmi podatke -> napravi objekt -> spremi u bazu (state_set)) -> postavi alarm
  //              -> alarm okinio uspjesno/neuspjesno -> postavi state_ok / state_error ovisno o tome -> upload ak treba -> brisanje datoteke ak treba

  //edit schedule -> uzmi nove podatke -> uzmi stari objekt i makni alarm -> spremi nove promjene (state_unset) -> postavi novi alarm -> ...

  //delete schedule -> uzmi podatke -> makni alarm -> obrisi schedule

  //todo dodaj targetGcmId -> gcm id uredaja na koji treba instalirati schedule

  //novi REMOTE schedule -> uzmi podatke -> napravi objekt -> spremi u bazu (state_unset) -> posalji na cloud -> cloud salje tom uredaju
  //                      -> taj uredaj sprema schedule (unset) -> postavlja alarm -> sprema promjene u bazu (state_set)
  //                      -> salje promjenu cloudu (cloud preko gcm-a salje ostalima)

  // schedules tijekom snimanja sprema u recorded files tablicu
  // tamo su imena svih fileova i di su spremljeni, imaju id schedulea, te imaju sync_state
  // nakon sto uploada sve promjeni i state u scheduleu
}
