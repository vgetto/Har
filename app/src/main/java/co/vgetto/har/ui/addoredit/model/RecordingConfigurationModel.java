package co.vgetto.har.ui.addoredit.model;

import co.vgetto.har.db.entities.Schedule;
import co.vgetto.har.db.entities.Trigger;
import co.vgetto.har.db.entities.configurations.RecordingConfiguration;

/**
 * Created by Kovje on 6.10.2015..
 */
public class RecordingConfigurationModel {
  private int duration;
  private int delayBetween;
  private int numOfRecordings;
  private String filePrefix;
  private String folderName;

  public RecordingConfigurationModel() {
    this.duration = -1;
    this.delayBetween = -1;
    this.numOfRecordings = -1;
  }

  public RecordingConfigurationModel(int duration, int delayBetween, int numOfRecordings,
      String filePrefix, String folderName) {
    this.duration = duration;
    this.delayBetween = delayBetween;
    this.numOfRecordings = numOfRecordings;
    this.filePrefix = filePrefix;
    this.folderName = folderName;
  }

  public static RecordingConfigurationModel fromSchedule(Schedule schedule) {
    RecordingConfiguration recordingConfiguration =
        schedule.scheduleConfiguration().recordingConfiguration();
    return new RecordingConfigurationModel(recordingConfiguration.duration(),
        recordingConfiguration.delayBetween(), recordingConfiguration.numOfRecordings(),
        recordingConfiguration.filePrefix(), recordingConfiguration.folderName());
  }

  public static RecordingConfigurationModel fromTrigger(Trigger trigger) {
    RecordingConfiguration recordingConfiguration =
        trigger.triggerConfiguration().recordingConfiguration();
    return new RecordingConfigurationModel(recordingConfiguration.duration(),
        recordingConfiguration.delayBetween(), recordingConfiguration.numOfRecordings(),
        recordingConfiguration.filePrefix(), recordingConfiguration.folderName());
  }

  private boolean isDurationValid() {
    if ((duration >= 5) && (duration <= 900)) {
      return true;
    } else {
      return false;
    }
  }

  public int getDuration() {
    return duration;
  }

  public String getDurationString() {
    if (duration != -1) {
      return Integer.toString(duration);
    } else {
      return "";
    }
  }

  public void setDuration(String duration) {
    if (!duration.equals("")) {
      this.duration = Integer.valueOf(duration);
    } else {
      this.duration = -1;
    }
  }

  public String handleDurationError() {
    if (isDurationValid() || (duration == -1)) {
      return null;
    } else {
      return "Duration must be between 5 and 900 seconds";
    }
  }

  private boolean isDelayBetweenValid() {
    if ((delayBetween >= 5) && (delayBetween <= 120)) {
      return true;
    } else {
      return false;
    }
  }

  public int getDelayBetween() {
    return delayBetween;
  }

  public String getDelayBetweenString() {
    if (delayBetween != -1) {
      return Integer.toString(delayBetween);
    } else {
      return "";
    }
  }

  public void setDelayBetween(String delayBetween) {
    if (!delayBetween.equals("")) {
      this.delayBetween = Integer.valueOf(delayBetween);
    } else {
      this.delayBetween = -1;
    }
  }

  public String handleDelayBetweenError() {
    if (isDelayBetweenValid() || (delayBetween == -1)) {
      return null;
    } else {
      return "Delay between recordings must  be between 5 and 60 seconds";
    }
  }

  private boolean isNumOfRecordingsValid() {
    if ((numOfRecordings >= 1) && (numOfRecordings <= 20)) {
      return true;
    } else {
      return false;
    }
  }

  public int getNumOfRecordings() {
    return numOfRecordings;
  }

  public String getNumOfRecordingsString() {
    if (numOfRecordings != -1) {
      return Integer.toString(numOfRecordings);
    } else {
      return "";
    }
  }

  public void setNumOfRecordings(String numOfRecordings) {
    if (!numOfRecordings.equals("")) {
      this.numOfRecordings = Integer.valueOf(numOfRecordings);
    } else {
      this.numOfRecordings = -1;
    }
  }

  public String handleNumOfRecordingsError() {
    if (isNumOfRecordingsValid() || (numOfRecordings == -1)) {
      return null;
    } else {
      return "You can record between 1 and 20 recordings";
    }
  }

  private boolean isFilePrefixValid() {
    if (!filePrefix.equals("")) {
      return true;
    } else {
      return false;
    }
  }

  public String getFilePrefix() {
    return filePrefix;
  }

  public void setFilePrefix(String filePrefix) {
    this.filePrefix = filePrefix;
  }

  private boolean isFolderNameValid() {
    if (!folderName.equals("")) {
      return true;
    } else {
      return false;
    }
  }

  public String getFolderName() {
    return folderName;
  }

  public void setFolderName(String folderName) {
    this.folderName = folderName;
  }

  public boolean isRecordingConfigurationValid() {
    return isDurationValid() && isDelayBetweenValid() && isNumOfRecordingsValid()
        && isFilePrefixValid() && isFolderNameValid();
  }

  public String toString() {
    return "Duration : " + Integer.toString(duration) + " delayBetween : " + Integer.toString(
        delayBetween) + " numOfRecordings : " + Integer.toString(numOfRecordings) +
        " fileprefix : " + filePrefix + " folder name: " + folderName;
  }
}
