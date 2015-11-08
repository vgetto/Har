package co.vgetto.har.db.entities.configurations;

import android.os.Parcelable;
import auto.parcel.AutoParcel;

/**
 * Created by Kovje on 21.8.2015..
 */
@AutoParcel public abstract class TriggerConfiguration implements Parcelable {
  public static final int TRIGGER_TYPE_ON_RECEIVED_SMS = 0;
  public static final int TRIGGER_TYPE_AFTER_MISSED_CALL = 1;
  public static final int TRIGGER_TYPE_AFTER_OUTGOING_CALL = 2;
  public static final int TRIGGER_TYPE_AFTER_INCOMING_CALL = 3;

  public abstract int type(); // one of the above

  public abstract String phoneNumber(); // phone number, must be set if type isn't ON_RECEIVED_SMS

  public abstract String smsText(); // sms text, can be set with phone number or solo, if solo Har will trigger on that smstext from any number

  public abstract RecordingConfiguration recordingConfiguration();

  public abstract UploadConfiguration uploadConfiguration();

  public static TriggerConfiguration create(int type, String phoneNuumber, String smsText,
      RecordingConfiguration recordingConfiguration, UploadConfiguration uploadConfiguration) {
    return new AutoParcel_TriggerConfiguration(type, phoneNuumber, smsText, recordingConfiguration,
        uploadConfiguration);
  }
}
