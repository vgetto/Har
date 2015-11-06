package co.vgetto.har.db.entities.configurations;

import android.os.Parcelable;
import auto.parcel.AutoParcel;

/**
 * Created by Kovje on 21.8.2015..
 */
@AutoParcel
public abstract class TriggerConfiguration implements Parcelable {
    public abstract int type();
    public abstract String number();
    public abstract RecordingConfiguration recordingConfiguration();
    public abstract UploadConfiguration uploadConfiguration();

    public static TriggerConfiguration create(int type, String number, RecordingConfiguration recordingConfiguration, UploadConfiguration uploadConfiguration) {
        return new AutoParcel_TriggerConfiguration(type, number, recordingConfiguration, uploadConfiguration);
    }
}
