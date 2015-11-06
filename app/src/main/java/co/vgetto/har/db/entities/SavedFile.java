package co.vgetto.har.db.entities;

import android.os.Parcelable;
import auto.parcel.AutoParcel;

/**
 * Created by Kovje on 21.8.2015..
 */
@AutoParcel public abstract class SavedFile implements Parcelable {
  public abstract String filePath();

  public abstract long recordingEndedTime();

  public abstract boolean synced();

  public static SavedFile create(String filePath, long recordingEndedTime, boolean synced) {
    return new AutoParcel_SavedFile(filePath, recordingEndedTime, synced);
  }

}
