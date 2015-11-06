package co.vgetto.har.ui.addoredit.model;

import co.vgetto.har.db.entities.Schedule;
import co.vgetto.har.db.entities.Trigger;
import co.vgetto.har.db.entities.configurations.UploadConfiguration;

/**
 * Created by Kovje on 6.10.2015..
 */
public class UploadConfigurationModel {
  private boolean dropboxUpload;
  private boolean deleteAfterUpload;
  private boolean notifyOnStartRecording;
  private boolean notifyOnEndRecording;
  private boolean saveToHistory;

  public UploadConfigurationModel(boolean dropboxUpload, boolean deleteAfterUpload, boolean notifyOnStartRecording, boolean notifyOnEndRecording, boolean saveToHistory) {
    this.dropboxUpload = dropboxUpload;
    this.deleteAfterUpload = deleteAfterUpload;
    this.notifyOnStartRecording = notifyOnStartRecording;
    this.notifyOnEndRecording = notifyOnEndRecording;
    this.saveToHistory = saveToHistory;
  }

  public static UploadConfigurationModel fromSchedule(Schedule schedule) {
    UploadConfiguration uploadConfiguration = schedule.scheduleConfiguration().uploadConfiguration();

    return new UploadConfigurationModel(uploadConfiguration.dropboxUpload(), uploadConfiguration.deleteAfterUpload(),
        uploadConfiguration.notifyOnStartRecording(), uploadConfiguration.notifyOnEndRecording(), uploadConfiguration.saveToHistory());
  }

  public static UploadConfigurationModel fromTrigger(Trigger trigger) {
    UploadConfiguration uploadConfiguration = trigger.triggerConfiguration().uploadConfiguration();

    return new UploadConfigurationModel(uploadConfiguration.dropboxUpload(), uploadConfiguration.deleteAfterUpload(),
        uploadConfiguration.notifyOnStartRecording(), uploadConfiguration.notifyOnEndRecording(), uploadConfiguration.saveToHistory());
  }

  public boolean isDropboxUpload() {
    return dropboxUpload;
  }

  public void setDropboxUpload(boolean dropboxUpload) {
    this.dropboxUpload = dropboxUpload;
  }

  public boolean isDeleteAfterUpload() {
    return deleteAfterUpload;
  }

  public void setDeleteAfterUpload(boolean deleteAfterUpload) {
    this.deleteAfterUpload = deleteAfterUpload;
  }

  public boolean isNotifyOnStartRecording() {
    return notifyOnStartRecording;
  }

  public void setNotifyOnStartRecording(boolean notifyOnStartRecording) {
    this.notifyOnStartRecording = notifyOnStartRecording;
  }

  public boolean isNotifyOnEndRecording() {
    return notifyOnEndRecording;
  }

  public void setNotifyOnEndRecording(boolean notifyOnEndRecording) {
    this.notifyOnEndRecording = notifyOnEndRecording;
  }

  public boolean isSaveToHistory() {
    return saveToHistory;
  }

  public void setSaveToHistory(boolean saveToHistory) {
    this.saveToHistory = saveToHistory;
  }
}
