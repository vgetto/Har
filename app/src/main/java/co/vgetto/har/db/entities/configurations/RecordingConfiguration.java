package co.vgetto.har.db.entities.configurations;

import android.os.Parcelable;
import auto.parcel.AutoParcel;

/**
 * Created by Kovje on 17.10.2015..
 */
@AutoParcel
public abstract class RecordingConfiguration implements Parcelable{
  public abstract int duration();
  public abstract int delayBetween();
  public abstract int numOfRecordings();
  public abstract String filePrefix();
  public abstract String folderName();

  public static RecordingConfiguration create(int duration, int delayBetween, int numOfRecordings, String filePrefix, String folderName) {
    return new AutoParcel_RecordingConfiguration(duration, delayBetween, numOfRecordings, filePrefix, folderName);
  }
}