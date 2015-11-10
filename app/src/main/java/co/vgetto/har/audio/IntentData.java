package co.vgetto.har.audio;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcelable;
import auto.parcel.AutoParcel;
import co.vgetto.har.db.Db;
import co.vgetto.har.db.entities.SavedFile;
import co.vgetto.har.db.entities.configurations.RecordingConfiguration;
import co.vgetto.har.db.entities.configurations.UploadConfiguration;
import co.vgetto.har.db.tables.HistoryTable;
import java.util.List;
import rx.functions.Func1;

@AutoParcel public abstract class IntentData implements Parcelable {
  public abstract long foreignId();

  public abstract int type();

  public abstract RecordingConfiguration recordingConfiguration();

  public abstract UploadConfiguration uploadConfiguration();

  public static IntentData create(long foreignId, int type, RecordingConfiguration recordingConfiguration,
      UploadConfiguration uploadConfiguration) {
    return new AutoParcel_IntentData(foreignId, type, recordingConfiguration, uploadConfiguration);
  }
}
