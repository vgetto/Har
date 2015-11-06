package co.vgetto.har.ui.addoredit.addeditschedule;

import android.content.ContentValues;
import co.vgetto.har.Constants;
import co.vgetto.har.db.entities.Schedule;
import co.vgetto.har.db.entities.configurations.ScheduleConfiguration;
import co.vgetto.har.db.entities.configurations.RecordingConfiguration;
import co.vgetto.har.db.entities.configurations.UploadConfiguration;
import co.vgetto.har.ui.addoredit.base.BaseAddEditModel;
import co.vgetto.har.ui.addoredit.base.BaseAddEditPresenter;
import co.vgetto.har.ui.addoredit.model.RecordingConfigurationModel;
import co.vgetto.har.ui.addoredit.model.AddOrEditScheduleModel;
import co.vgetto.har.ui.addoredit.model.UploadConfigurationModel;
import co.vgetto.har.ui.base.BaseModel;

/**
 * Created by Kovje on 26.10.2015..
 */
public class AddOrEditSchedulePresenter implements BaseAddEditPresenter {
  @Override public ContentValues getContentValuesFromModel(BaseModel baseModel) {
    AddOrEditScheduleModel model = (AddOrEditScheduleModel) baseModel;
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

    ScheduleConfiguration scheduleConfiguration =
        ScheduleConfiguration.create(model.getCurrentDate(), recordingConfiguration,
            uploadConfiguration);

    if (model.getLayoutType() == BaseAddEditModel.LAYOUT_TYPE_EDIT) {
      // editing a schedule
      // schedule id should be set
      return new Schedule.ContentValuesBuilder().id(model.getScheduleId())
          .state(Schedule.SCHEDULE_STATE_WAITING)
          .scheduleConfiguration(scheduleConfiguration)
          .build();
    } else {
      // adding new schedule, id will be set after inserting to db
      return new Schedule.ContentValuesBuilder().state(Schedule.SCHEDULE_STATE_WAITING)
          .scheduleConfiguration(scheduleConfiguration)
          .build();
    }
  }
}
