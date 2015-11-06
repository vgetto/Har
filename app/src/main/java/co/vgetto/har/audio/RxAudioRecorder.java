package co.vgetto.har.audio;

import android.media.MediaRecorder;
import android.util.Log;
import co.vgetto.har.db.entities.configurations.RecordingConfiguration;
import com.fernandocejas.frodo.annotation.RxLogObservable;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import rx.Observable;

/**
 * Created by Kovje on 11.8.2015..
 */
public class RxAudioRecorder {
  private RecordingConfiguration recordingConfiguration;
  private MediaRecorder mRecorder = null;
  private String TAG = this.getClass().getCanonicalName();

  public RxAudioRecorder(RecordingConfiguration recordingConfiguration) {
    this.recordingConfiguration = recordingConfiguration;
  }

  private String startRecording() {
    String fileName = StorageHelper.getAudioFileName(recordingConfiguration.folderName(),
        recordingConfiguration.filePrefix());
    if (mRecorder == null) {
      mRecorder = new MediaRecorder();
      mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
      mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
      mRecorder.setOutputFile(fileName);
      mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
      try {
        mRecorder.prepare();
      } catch (IOException e) {
        Log.e(TAG, "prepare() failed");
      }

      mRecorder.start();
      return fileName;
    }
    return null;
  }

  private void stopRecording() {
    mRecorder.stop();
    mRecorder.release();
    mRecorder = null;
  }

  @RxLogObservable
  public Observable<String> start() {
    //Observable that returns a filename of audio file right after it's recorded
    //the chain starts with an interval timer, that will emit an item every delay+recordingseconds
    //if seconds to record are set to 17s, and the delay is 5s, it will emit an item every 22s
    //it will only take mNumberOfRecordings items, and then complete

    //Observable chain starts with a call to startRecording
    //it goes down the chain only if it returns a non null filename (filter)
    //in flatmap a timer observable set to number of seconds to record, and a observable with just filename
    //are "ziped", and when both emit, stopRecording is called, and filename is emited, the subscriber gets the filename

    return Observable.interval(0,
        recordingConfiguration.delayBetween() + recordingConfiguration.duration(), TimeUnit.SECONDS)
        .flatMap(l -> Observable.just(startRecording())
                .filter(fileName -> fileName != null)
                .flatMap(fileName -> Observable.zip(
                        Observable.timer(recordingConfiguration.duration() + 1, TimeUnit.SECONDS),
                        Observable.just(fileName), (Long, String) -> {
                          stopRecording();
                          return fileName;
                        })))
        .take(recordingConfiguration.numOfRecordings());
  }
}
