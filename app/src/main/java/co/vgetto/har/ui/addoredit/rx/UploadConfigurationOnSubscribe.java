package co.vgetto.har.ui.addoredit.rx;

import android.view.View;
import android.widget.CheckBox;
import butterknife.Bind;
import butterknife.ButterKnife;
import co.vgetto.har.R;
import co.vgetto.har.ui.addoredit.model.UploadConfigurationModel;
import com.jakewharton.rxbinding.widget.RxCompoundButton;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by Kovje on 1.10.2015..
 */
public class UploadConfigurationOnSubscribe
    implements Observable.OnSubscribe<UploadConfigurationModel> {
  @Bind(R.id.cbDropboxUpload) CheckBox dropboxUpload;

  @Bind(R.id.cbDeleteAfterUpload) CheckBox deleteAfterUpload;

  @Bind(R.id.cbNotifyOnStartRecording) CheckBox notifyOnStartRecording;

  @Bind(R.id.cbNotifyOnEndRecording) CheckBox notifyOnEndRecording;

  @Bind(R.id.cbSaveToHistory) CheckBox saveToHistory;

  private Subscriber subscriber;

  private UploadConfigurationModel uploadConfigurationModel;

  public UploadConfigurationOnSubscribe(View v, UploadConfigurationModel uploadConfigurationModel) {
    ButterKnife.bind(this, v);

    if (uploadConfigurationModel == null) {
      this.uploadConfigurationModel = new UploadConfigurationModel(false, false, false, false, false);
    } else {
      this.uploadConfigurationModel = uploadConfigurationModel;
      dropboxUpload.setChecked(uploadConfigurationModel.isDropboxUpload());
      deleteAfterUpload.setChecked(uploadConfigurationModel.isDeleteAfterUpload());
      notifyOnStartRecording.setChecked(uploadConfigurationModel.isNotifyOnStartRecording());
      notifyOnEndRecording.setChecked(uploadConfigurationModel.isNotifyOnEndRecording());
      saveToHistory.setChecked(uploadConfigurationModel.isSaveToHistory());
    }
  }

  @Override public void call(Subscriber<? super UploadConfigurationModel> subscriber) {
    this.subscriber = subscriber;
    createObservable();
  }

  public void createObservable() {
    Observable.combineLatest(RxCompoundButton.checkedChanges(dropboxUpload),
        RxCompoundButton.checkedChanges(deleteAfterUpload),
        RxCompoundButton.checkedChanges(notifyOnStartRecording),
        RxCompoundButton.checkedChanges(notifyOnEndRecording),
        RxCompoundButton.checkedChanges(saveToHistory),
        (Boolean dropboxUpload, Boolean deleteAfterUpload, Boolean notifyOnStartRecording, Boolean notifyOnEndRecording, Boolean saveToHistory) -> {
          uploadConfigurationModel.setDropboxUpload(dropboxUpload);
          uploadConfigurationModel.setDeleteAfterUpload(deleteAfterUpload);
          uploadConfigurationModel.setNotifyOnStartRecording(notifyOnStartRecording);
          uploadConfigurationModel.setNotifyOnEndRecording(notifyOnEndRecording);
          uploadConfigurationModel.setSaveToHistory(saveToHistory);
          return uploadConfigurationModel;
        }).subscribe(confModel -> subscriber.onNext(confModel));
  }
}
