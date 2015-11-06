package co.vgetto.har.ui.addoredit.rx;

import android.view.View;
import android.widget.EditText;
import butterknife.Bind;
import butterknife.ButterKnife;
import co.vgetto.har.R;
import co.vgetto.har.ui.addoredit.model.RecordingConfigurationModel;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.jakewharton.rxbinding.widget.TextViewTextChangeEvent;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by Kovje on 1.10.2015..
 */
public class RecordingConfigurationOnSubscribe
    implements Observable.OnSubscribe<RecordingConfigurationModel> {
  @Bind(R.id.duration) EditText duration;

  @Bind(R.id.delayBetween) EditText delayBetween;

  @Bind(R.id.numOfRecordings) EditText numOfRecordings;

  @Bind(R.id.prefix) EditText filePrefix;

  @Bind(R.id.folder) EditText folderName;

  private Subscriber subscriber;

  private RecordingConfigurationModel recordingConfigurationModel;

  public RecordingConfigurationOnSubscribe(View v, RecordingConfigurationModel recordingConfigurationModel) {
    ButterKnife.bind(this, v);

    if (recordingConfigurationModel == null) {
      this.recordingConfigurationModel = new RecordingConfigurationModel();
    } else {
      this.recordingConfigurationModel = recordingConfigurationModel;
      duration.setText(recordingConfigurationModel.getDurationString());
      delayBetween.setText(recordingConfigurationModel.getDelayBetweenString());
      numOfRecordings.setText(recordingConfigurationModel.getNumOfRecordingsString());
      filePrefix.setText(recordingConfigurationModel.getFilePrefix());
      folderName.setText(recordingConfigurationModel.getFolderName());
    }
  }

  @Override public void call(Subscriber<? super RecordingConfigurationModel> subscriber) {
    this.subscriber = subscriber;
    createObservable();
  }

  private RecordingConfigurationModel handleInputErrors(RecordingConfigurationModel currentConfiguration) {
    this.duration.setError(currentConfiguration.handleDurationError());
    this.delayBetween.setError(currentConfiguration.handleDelayBetweenError());
    this.numOfRecordings.setError(currentConfiguration.handleNumOfRecordingsError());
    return currentConfiguration;
  }

  public void createObservable() {
    Observable.combineLatest(
        RxTextView.textChangeEvents(duration), RxTextView.textChangeEvents(delayBetween),
        RxTextView.textChangeEvents(numOfRecordings), RxTextView.textChangeEvents(filePrefix),
        RxTextView.textChangeEvents(folderName),
        (TextViewTextChangeEvent durationChange, TextViewTextChangeEvent delayBetweenChange, TextViewTextChangeEvent numOfRecordingsChange, TextViewTextChangeEvent filePrefixChange, TextViewTextChangeEvent folderNameChange) -> {
          recordingConfigurationModel.setDuration(durationChange.text().toString());
          recordingConfigurationModel.setDelayBetween(delayBetweenChange.text().toString());
          recordingConfigurationModel.setNumOfRecordings(numOfRecordingsChange.text().toString());
          recordingConfigurationModel.setFilePrefix(filePrefixChange.text().toString());
          recordingConfigurationModel.setFolderName(folderNameChange.text().toString());
          return recordingConfigurationModel;
          // every time change occurs, make new object, check all data, set hint's if necesarry and send object to Configurationpresenter
          // he will determine if he has to show the next button or not
        })
        .map(recordingConfiguration -> handleInputErrors(recordingConfiguration))
        .subscribe(c -> subscriber.onNext(c));
  }
}
