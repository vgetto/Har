package co.vgetto.har.db.entities.configurations;

import android.os.Parcelable;
import auto.parcel.AutoParcel;

/**
 * Created by Kovje on 17.10.2015..
 */
@AutoParcel
public abstract class UploadConfiguration implements Parcelable{
  public abstract boolean dropboxUpload();
  public abstract boolean deleteAfterUpload();
  public abstract boolean notifyOnStartRecording();
  public abstract boolean notifyOnEndRecording();
  public abstract boolean saveToHistory();

  public static UploadConfiguration create(boolean dropboxUpload, boolean deleteAfterUpload, boolean nofityOnStartRecording, boolean notifyOnEndRecording, boolean saveToHistory) {
    return new AutoParcel_UploadConfiguration(dropboxUpload, deleteAfterUpload, nofityOnStartRecording, notifyOnEndRecording, saveToHistory);
  }
}