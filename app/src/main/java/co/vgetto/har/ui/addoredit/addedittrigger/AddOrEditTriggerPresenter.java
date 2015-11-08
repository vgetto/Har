package co.vgetto.har.ui.addoredit.addedittrigger;

import android.content.ContentValues;
import co.vgetto.har.db.entities.Trigger;
import co.vgetto.har.db.entities.configurations.RecordingConfiguration;
import co.vgetto.har.db.entities.configurations.TriggerConfiguration;
import co.vgetto.har.db.entities.configurations.UploadConfiguration;
import co.vgetto.har.ui.addoredit.base.BaseAddEditModel;
import co.vgetto.har.ui.addoredit.model.AddOrEditTriggerModel;
import co.vgetto.har.ui.addoredit.model.RecordingConfigurationModel;
import co.vgetto.har.ui.addoredit.model.UploadConfigurationModel;
import co.vgetto.har.ui.addoredit.base.BaseAddEditPresenter;
import co.vgetto.har.ui.base.BaseModel;

/**
 * Created by Kovje on 26.10.2015..
 */
public class AddOrEditTriggerPresenter implements BaseAddEditPresenter {
  @Override public ContentValues getContentValuesFromModel(BaseModel baseModel) {
    AddOrEditTriggerModel model = (AddOrEditTriggerModel) baseModel;

    RecordingConfigurationModel recordingConf = model.getRecordingConfigurationModel();
    UploadConfigurationModel uploadConfigurationModel = model.getUploadConfigurationModel();

    // make pojo
    RecordingConfiguration recordingConfiguration =
        RecordingConfiguration.create(recordingConf.getDuration(), recordingConf.getDelayBetween(),
            recordingConf.getNumOfRecordings(), recordingConf.getFilePrefix(),
            recordingConf.getFolderName());

    UploadConfiguration uploadConfiguration =
        UploadConfiguration.create(uploadConfigurationModel.isDropboxUpload(),
            uploadConfigurationModel.isDeleteAfterUpload(),
            uploadConfigurationModel.isNotifyOnStartRecording(),
            uploadConfigurationModel.isNotifyOnEndRecording(),
            uploadConfigurationModel.isSaveToHistory());

    // make trigger configuration

    TriggerConfiguration c =
        TriggerConfiguration.create(model.getTriggerConfigurationModel().getType(),
            model.getTriggerConfigurationModel().getPhoneNumber(), model.getTriggerConfigurationModel().getSmsText(), recordingConfiguration,
            uploadConfiguration);

    if (model.getLayoutType() == BaseAddEditModel.LAYOUT_TYPE_EDIT) {
      return new Trigger.ContentValuesBuilder().id(model.getTriggerId())
          .triggerConfiguration(c)
          .build();
    } else {
      return new Trigger.ContentValuesBuilder().triggerConfiguration(c).build();
    }

    // return content values

  }
}
