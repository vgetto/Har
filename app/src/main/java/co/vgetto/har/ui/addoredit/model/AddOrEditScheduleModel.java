package co.vgetto.har.ui.addoredit.model;

import co.vgetto.har.db.entities.Schedule;
import co.vgetto.har.ui.addoredit.base.BaseAddEditModel;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by Kovje on 20.10.2015..
 */

// todo rewrite this to make sense !

public class AddOrEditScheduleModel extends BaseAddEditModel {
  private long scheduleId;
  private int year;
  private int month;
  private int day;
  private int hour;
  private int minute;

  private RecordingConfigurationModel recordingConfigurationModel;
  private UploadConfigurationModel uploadConfigurationModel;

  private Calendar calendar;
  private Date date;

  private List<Integer> dateList = new ArrayList<>(3);
  private List<Integer> timeList = new ArrayList<>(2);

  public AddOrEditScheduleModel(Calendar calendar, Date date) {
    super();
    this.calendar = calendar;
    this.date = new Date();

    calendar.setTimeInMillis(date.getTime());
    year = calendar.get(Calendar.YEAR);
    month = calendar.get(Calendar.MONTH);
    day = calendar.get(Calendar.DAY_OF_MONTH);
    hour = calendar.get(Calendar.HOUR_OF_DAY);
    minute = calendar.get(Calendar.MINUTE);

    setLayoutType(0); // add schedule
    setCurrentPage(0);
    setModelRestored(false);
  }

  public static AddOrEditScheduleModel createFromSchedule(Schedule schedule) {
    AddOrEditScheduleModel model = new AddOrEditScheduleModel(Calendar.getInstance(), new Date(schedule.scheduleConfiguration().startTime()));
    model.setScheduleId(schedule.id());
    model.setRecordingConfigurationModel(RecordingConfigurationModel.fromSchedule(schedule));
    model.setUploadConfigurationModel(UploadConfigurationModel.fromSchedule(schedule));
    model.setLayoutType(1); // edit schedule
    return model;
  }

  public long setDate(List<Integer> date) {
    this.year = date.get(0);
    this.month = date.get(1);
    this.day = date.get(2);
    return getCurrentDate();
  }

  public long setTime(List<Integer> time) {
    this.hour = time.get(0);
    this.minute = time.get(1);
    return getCurrentDate();
  }

  public void setDateTimeFromLong(long startTime) {
    calendar.setTimeInMillis(startTime);

  }

  public long getCurrentDate() {
    calendar.set(Calendar.YEAR, year);
    calendar.set(Calendar.MONTH, month);
    calendar.set(Calendar.DAY_OF_MONTH, day);
    calendar.set(Calendar.HOUR_OF_DAY, hour);
    calendar.set(Calendar.MINUTE, minute);
    calendar.set(Calendar.SECOND, 0);
    return calendar.getTimeInMillis();
  }

  public String getCurrentDateString() {
    DateFormat df = DateFormat.getDateInstance();
    df.setTimeZone(TimeZone.getDefault());
    return "Current set date : " + df.format(new Date(getCurrentDate()));
  }

  public List<Integer> getCurrentDateAsList() {
    dateList.clear();
    dateList.add(year);
    dateList.add(month);
    dateList.add(day);
    return dateList;
  }

  public List<Integer> getCurrentTimeAsList() {
    List<Integer> list = new ArrayList<>(2);
    list.add(hour);
    list.add(minute);
    return list;
  }

  public boolean dateCheck() {
    long currentTime, setTime;
    calendar.setTimeInMillis(date.getTime());
    calendar.set(Calendar.HOUR_OF_DAY, 0);
    calendar.set(Calendar.MINUTE, 0);

    currentTime = calendar.getTimeInMillis();

    calendar.set(Calendar.YEAR, year);
    calendar.set(Calendar.MONTH, month);
    calendar.set(Calendar.DAY_OF_MONTH, day);

    setTime = calendar.getTimeInMillis();

    if (currentTime <= setTime) {
      return true;
    } else {
      return false;
    }
  }

  public boolean timeCheck() {
    if (date.getTime() < getCurrentDate()) {
      return true;
    } else {
      return false;
    }
  }

  public RecordingConfigurationModel getRecordingConfigurationModel() {
    return recordingConfigurationModel;
  }

  public void setRecordingConfigurationModel(RecordingConfigurationModel recordingConfigurationModel) {
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

  public long getScheduleId() {
    return scheduleId;
  }

  public void setScheduleId(long scheduleId) {
    this.scheduleId = scheduleId;
  }
}
