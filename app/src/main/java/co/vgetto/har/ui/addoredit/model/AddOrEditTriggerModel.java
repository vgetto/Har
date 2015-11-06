package co.vgetto.har.ui.addoredit.model;

import co.vgetto.har.db.entities.Trigger;
import co.vgetto.har.ui.addoredit.base.BaseAddEditModel;

/**
 * Created by Kovje on 20.10.2015..
 */

// todo rewrite this to make sense !

public class AddOrEditTriggerModel extends BaseAddEditModel {
  private long triggerId;

  private TriggerConfigurationModel triggerConfigurationModel;
  private RecordingConfigurationModel recordingConfigurationModel;
  private UploadConfigurationModel uploadConfigurationModel;

  public AddOrEditTriggerModel() {
    super();
    setLayoutType(0);
    setCurrentPage(0);
    setModelRestored(false);
  }

  public static AddOrEditTriggerModel createFromTrigger(Trigger trigger) {
    AddOrEditTriggerModel model = new AddOrEditTriggerModel();
    model.setTriggerId(trigger.id());
    model.setTriggerConfigurationModel(TriggerConfigurationModel.fromTrigger(trigger));
        model.setRecordingConfigurationModel(RecordingConfigurationModel.fromTrigger(trigger));
    model.setUploadConfigurationModel(UploadConfigurationModel.fromTrigger(trigger));
    model.setLayoutType(1); // edit schedule
    return model;
  }

  public RecordingConfigurationModel getRecordingConfigurationModel() {
    return recordingConfigurationModel;
  }

  public void setRecordingConfigurationModel(
      RecordingConfigurationModel recordingConfigurationModel) {
    this.recordingConfigurationModel = recordingConfigurationModel;
  }

  public boolean recordingConfigurationCheck() {
    return recordingConfigurationModel.isRecordingConfigurationValid();
  }

  public UploadConfigurationModel getUploadConfigurationModel() {
    return uploadConfigurationModel;
  }

  public void setUploadConfigurationModel(UploadConfigurationModel uploadConfigurationModel) {
    this.uploadConfigurationModel = uploadConfigurationModel;
  }

  public long getTriggerId() {
    return triggerId;
  }

  public void setTriggerId(long triggerId) {
    this.triggerId = triggerId;
  }

  public TriggerConfigurationModel getTriggerConfigurationModel() {
    return triggerConfigurationModel;
  }

  public void setTriggerConfigurationModel(TriggerConfigurationModel triggerConfigurationModel) {
    this.triggerConfigurationModel = triggerConfigurationModel;
  }
}
