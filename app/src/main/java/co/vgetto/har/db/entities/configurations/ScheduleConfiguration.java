package co.vgetto.har.db.entities.configurations;

import android.os.Parcelable;
import auto.parcel.AutoParcel;

/**
 * Created by Kovje on 21.8.2015..
 */
@AutoParcel
public abstract class ScheduleConfiguration implements Parcelable {
    public abstract long startTime();
    public abstract RecordingConfiguration recordingConfiguration();
    public abstract UploadConfiguration uploadConfiguration();

    public static ScheduleConfiguration create(long startTime, RecordingConfiguration recordingConfiguration, UploadConfiguration uploadConfiguration) {
        return new AutoParcel_ScheduleConfiguration(startTime, recordingConfiguration, uploadConfiguration);
    }
}
