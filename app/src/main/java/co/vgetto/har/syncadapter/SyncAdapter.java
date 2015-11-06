package co.vgetto.har.syncadapter;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.SyncResult;
import android.content.UriMatcher;
import android.os.Bundle;
import co.vgetto.har.Constants;
import co.vgetto.har.MyApplication;
import co.vgetto.har.rxservices.RxScheduleService;
import com.squareup.sqlbrite.BriteDatabase;
import javax.inject.Inject;

/**
 * Created by Kovje on 8.9.2015..
 */
public class SyncAdapter extends AbstractThreadedSyncAdapter {
  @Inject RxScheduleService scheduleManager;
  @Inject BriteDatabase db;

  private static final int CREATE_LOCAL_SCHEDULE = 1;
  private static final int EDIT_LOCAL_SCHEDULE = 2;
  private static final int DELETE_LOCAL_SCHEDULE = 3;

  private static final int CREATE_REMOTE_SCHEDULE = 10;
  private static final int EDIT_REMOTE_SCHEDULE = 20;
  private static final int DELETE_REMOTE_SCHEDULE = 30;

  private static final int FETCH_DATA = 40;

  private static final int UPLOAD_FILE = 50;

  //private DropboxAPI<AndroidAuthSession> mDBApi;
  //private AppKeyPair appKeys;
  //private AndroidAuthSession session;

  private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);

  static {
    sURIMatcher.addURI(Constants.AUTHORITY, "/create/local/#", CREATE_LOCAL_SCHEDULE);
    sURIMatcher.addURI(Constants.AUTHORITY, "/edit/local/#", EDIT_LOCAL_SCHEDULE);
    sURIMatcher.addURI(Constants.AUTHORITY, "/delete/local/#", DELETE_LOCAL_SCHEDULE);
    sURIMatcher.addURI(Constants.AUTHORITY, "/create/remote/#", CREATE_REMOTE_SCHEDULE);
    sURIMatcher.addURI(Constants.AUTHORITY, "/edit/remote/#", EDIT_REMOTE_SCHEDULE);
    sURIMatcher.addURI(Constants.AUTHORITY, "/delete/remote/#", DELETE_REMOTE_SCHEDULE);
    sURIMatcher.addURI(Constants.AUTHORITY, "/fetch/data", FETCH_DATA);
    sURIMatcher.addURI(Constants.AUTHORITY, "/upload/data/#", UPLOAD_FILE);
  }

  public SyncAdapter(Context context, boolean autoInitialize) {
    super(context, autoInitialize);
    MyApplication.get(getContext()).getAppComponent().inject(this);

    /*
    loginManager.getDropboxToken().map(token -> {
      if (!token.equals("")) {
        Timber.i(token);
        appKeys = new AppKeyPair(Constants.APP_KEY, Constants.APP_SECRET);
        session = new AndroidAuthSession(appKeys, token);
        mDBApi = new DropboxAPI<AndroidAuthSession>(session);
      }
      return true;
    }).toBlocking().first();
    */
  }

  public SyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs) {
    super(context, autoInitialize, allowParallelSyncs);
    MyApplication.get(context).getAppComponent().inject(this);
  }

  @Override public void onPerformSync(Account account, Bundle extras, String authority,
      ContentProviderClient provider, SyncResult syncResult) {

  }

  /*
  private void create(Uri uri) {
    Long id = Long.valueOf(uri.getLastPathSegment());
    Timber.i("local schedule mathed, id -> " + id.toString());
    scheduleManager.queryScheduleById(id) // get schedule
        .flatMap(schedule -> { // set STATE_POST
          Timber.i("Got schedule -> " + schedule.id());
          return scheduleManager.setSyncState(schedule.getContentValuesFromModel(), Constants.STATE_POST);
        })
        .flatMap(schedulePost -> { // make network call
          Timber.i("making network call -> schedule changed -> " + Integer.toString(
              schedulePost.syncState()));
          return service.createSchedule( // make network call
              schedulePost.targetGcmId(), schedulePost.setterGcmId(), schedulePost.startTime(),
              schedulePost.expireTime(), schedulePost.state(), schedulePost.scheduleConfiguration());
        })
        .map(
            scheduleResponse -> { // write back the response to local table, now with state 0 for SYNC, and new cloud id
              Timber.i("Response -> " + scheduleResponse.toString());
              ContentValues values = scheduleResponse.getContentValuesFromModel();
              values.put(SchedulesTable.ID, id);
              scheduleManager.updateScheduleById(values);
              return true;
            })
        .toBlocking()
        .first();
  }

  private void edit(Uri uri) {
    Long editId = Long.valueOf(uri.getLastPathSegment());
    Timber.i("editing remote id, edit id -> " + editId.toString());
    scheduleManager.queryScheduleById(editId) // get schedule
        .flatMap(schedule -> { // set STATE_PUT
          Timber.i("Got schedule id -> " + schedule.id() + " cloud id : " + Long.toString(
              schedule.cloudId()));
          return scheduleManager.setSyncState(schedule.getContentValuesFromModel(), Constants.STATE_PUT);
        })
        .flatMap(schedulePut -> { // make network call
          Timber.i("making network call -> schedule changed -> " + Integer.toString(
              schedulePut.syncState()));
          //TODO editSchedule call !
          return service.updateSchedule(schedulePut.targetGcmId(), schedulePut.setterGcmId(),
              schedulePut.startTime(), schedulePut.expireTime(), schedulePut.state(),
              schedulePut.cloudId(), schedulePut.scheduleConfiguration());
        })
        .map(
            scheduleResponse -> { // write back the response to local table, now with state 0 for SYNC and new cloud id
              Timber.i("Schedule edited ");
              Timber.i("Response -> " + scheduleResponse.toString());
              return true;
            })
        .toBlocking()
        .first();
  }

  private void delete(Uri uri) {
    Long deleteId = Long.valueOf(uri.getLastPathSegment());
    Timber.i("editing local, edit id -> " + deleteId.toString());
    scheduleManager.queryScheduleById(deleteId) // get schedule
        .flatMap(schedule -> { // set STATE_DELETE
          Timber.i("Got schedule -> " + schedule.id());
          return scheduleManager.setSyncState(schedule.getContentValuesFromModel(), Constants.STATE_DELETE);
        }).map(schedulePut -> { // make network call
      Timber.i("making network call -> schedule changed -> " + Integer.toString(
          schedulePut.syncState()));
      //TODO editSchedule call !
      service.deleteSchedule(schedulePut.targetGcmId(), schedulePut.setterGcmId(),
          schedulePut.cloudId(), schedulePut.state()).toBlocking().first();

      return schedulePut.id();
    }).map(localId -> {
      Timber.i("Deleted from cloud, deleting from local db");
      return scheduleManager.deleteScheduleById(localId);
    }).toBlocking().first();
  }

  public void uploadFile(String filePath, String delete) {
    File file = new File(filePath);
    Uri u = Uri.parse(filePath);
    String pathOnDropbox = "/" + u.getLastPathSegment();
    FileInputStream inputStream = null;
    try {
      inputStream = new FileInputStream(file);
      DropboxAPI.Entry response =
          mDBApi.putFile(pathOnDropbox, inputStream, file.length(), null, null);

      if (delete.equals("true")) {
        file.delete();
      }
      Log.i("DbExampleLog", "The uploaded file's rev is: " + response.rev);
    } catch (FileNotFoundException e) {
      Log.i("DbExampleLog", "Exception file not found");
      e.printStackTrace();
    } catch (DropboxException e) {
      Log.i("DbExampleLog", "Dropbox exception..");
      e.printStackTrace();
    }
  }

  @Override public void onPerformSync(Account account, Bundle extras, String authority,
      ContentProviderClient provider, SyncResult syncResult) {
    Uri uri = Uri.parse(extras.getString("uri"));

    List<String> pathSegments = uri.getPathSegments();
    StringBuilder builder = new StringBuilder();
    String delete = "";

    int size = pathSegments.size();
    for (int i = 0; i < size; i++) {
      if (pathSegments.get(i).equals("storage")) {
        for (int k = i; k < size - 1; k++) {
          builder.append("/" + pathSegments.get(k));
        }
        delete = uri.getLastPathSegment();
        break;
      }
    }

    if (!delete.equals("")) {
      String s = builder.toString();
      uploadFile(s, delete);
    } else {

      int match = sURIMatcher.match(uri);
      switch (match) {
        // creating new local schedule
        case CREATE_LOCAL_SCHEDULE:
          create(uri);
          break;

        // editing a local schedule
        case EDIT_LOCAL_SCHEDULE:
          edit(uri);
          break;

        case DELETE_LOCAL_SCHEDULE:
          delete(uri);
          break;

        case CREATE_REMOTE_SCHEDULE:
          create(uri);
          break;

        case EDIT_REMOTE_SCHEDULE:
          edit(uri);
          break;

        case DELETE_REMOTE_SCHEDULE:
          delete(uri);
          break;

        default:
          break;
      }
    }
  }
  */
}
