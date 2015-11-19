package co.vgetto.har.rxservices;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import co.vgetto.har.db.Db;
import co.vgetto.har.db.entities.Schedule;
import co.vgetto.har.db.tables.SchedulesTable;
import co.vgetto.har.receivers.AlarmBroadcastReceiver;
import com.fernandocejas.frodo.annotation.RxLogObservable;
import java.util.List;
import rx.Observable;

/**
 * Created by Kovje on 10.9.2015..
 */

/**
 * Helper class for working with schedules
 */
public final class RxScheduleService {
  private final AlarmManager alarmManager;
  private final RxDbService rxDbService;
  private final RxHistoryService rxHistoryService;
  private final Context context;

  public RxScheduleService(Context context, RxDbService rxDbService,
      RxHistoryService rxHistoryService, AlarmManager manager) {
    this.context = context;
    this.alarmManager = manager;
    this.rxDbService = rxDbService;
    this.rxHistoryService = rxHistoryService;
  }

  /**
   * returns an Observable that emits a list of schedules on every db update, as long as
   * subscribed
   */
  public Observable<List<Schedule>> getScheduleQueryObservable() {
    return rxDbService.getQueryObservable(Schedule.class).mapToList(Schedule.BRITE_MAPPER);
  }

  /**
   * returns a schedule object based on it's id in db
   */
  public Observable<Schedule> getScheduleById(long id) {
    return rxDbService.get(Schedule.class, Schedule.selectionById, Db.getSelectionArgsForId(id),
        null).map(Schedule.SINGLE_MAPPER);
  }

  /**
   * adds a new schedule in db based on content values, then returns it from db as a schedule
   * object
   * and sets alarm with it's data
   */
  @RxLogObservable public Observable<Boolean> addSchedule(ContentValues values) {
    return rxDbService.insert(Schedule.class, values) // insert schedule to db
        .concatMap(this::getScheduleById) // query inserted schedule
        .map(this::setAlarm); // set alarm for this schedule
  }

  /**
   * edit's a schedule based on content values
   * first it cancel's the alarm for the start time that is currently in db, then it edit's the data
   * in db with new data from content values, and finally it set's the alarm
   */
  public Observable<Boolean> editSchedule(ContentValues values) {
    long id = values.getAsLong(SchedulesTable.ID);
    return Observable.just(id).map(this::cancelAlarm) // emit ID and cancel alarm for that ID
        .concatMap(alarmCanceled -> rxDbService.edit(Schedule.class, values, Schedule.selectionById,
            Db.getSelectionArgsForId(id))) // edit schedule in db
        .concatMap(this::getScheduleById) // get edited schedule from db
        .map(this::setAlarm); // set alarm for new schedule
  }

  /**
   * deletes a schedule base on schedule object provided
   * cancels the alarm first, then deletes from db
   * @param schedule
   * @return
   */
  public Observable<Integer> deleteSchedule(Schedule schedule) {
    return Observable.just(cancelAlarm(schedule.id())) // cancel alarm
        .concatMap(canceled -> rxDbService.delete(Schedule.class, Schedule.selectionById,
            Db.getSelectionArgsForId(schedule.id()))); // delete from db
  }

  /**
   * changes state of schedule (either WAITING or STARTED), base on schedule id in db
   */
  public Observable<Integer> setScheduleState(long id, int state) {
    ContentValues values = new ContentValues();
    values.put(SchedulesTable.ID, id);
    values.put(SchedulesTable.STATE, state);
    return rxDbService.edit(Schedule.class, values, Schedule.selectionById,
        Db.getSelectionArgsForId(id));
  }

  /**
   * creates a PendingIntent for AlarmBroadcastReceiver
   */
  private PendingIntent createIntent(long scheduleId) {
    Intent i = new Intent(context, AlarmBroadcastReceiver.class);
    // request code set to id of the schedule
    i.putExtra("id", scheduleId);
    return PendingIntent.getBroadcast(context, Integer.valueOf(Long.toString(scheduleId)), i,
        PendingIntent.FLAG_UPDATE_CURRENT);
  }

  /**
   * set alarm based on schedule start time
   * TODO set or setExact ? make sdk check
   */
  private Boolean setAlarm(Schedule schedule) {
    PendingIntent intent = createIntent(schedule.id());
    alarmManager.set(AlarmManager.RTC_WAKEUP, schedule.scheduleConfiguration().startTime(), intent);
    return true;
  }

  /**
   * cancel an alarm ( when editing or deleting a schedule)
   */
  private Boolean cancelAlarm(long scheduleId) {
    PendingIntent intent = createIntent(scheduleId);
    alarmManager.cancel(intent);
    return true;
  }
}
