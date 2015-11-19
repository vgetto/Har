package co.vgetto.har.rxservices;

import android.content.ContentValues;
import android.net.Uri;
import android.util.Log;
import co.vgetto.har.audio.IntentData;
import co.vgetto.har.db.Db;
import co.vgetto.har.db.entities.Trigger;
import co.vgetto.har.db.entities.User;
import co.vgetto.har.db.tables.TriggersTable;
import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.exception.DropboxException;
import com.dropbox.client2.session.AppKeyPair;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.concurrent.TimeUnit;
import rx.Observable;

/**
 * Created by Kovje on 10.9.2015..
 */

/**
 * Helper class for working with triggers
 */

public final class RxUserService {
  private final RxDbService rxDbService;
  private final AppKeyPair appKeyPair;
  private DropboxAPI<AndroidAuthSession> mDBApi;

  public RxUserService(RxDbService rxDbService, AppKeyPair appKeyPair) {
    this.rxDbService = rxDbService;
    this.appKeyPair = appKeyPair;
    this.mDBApi = null;
  }

  /**
   * returns and observable that emit's a a User object every time user table is updated
   * as long a subscription to this observable is alive
   * if User is logged in it returns the User object, otherwise returns null
   */
  public final Observable<User> getLoggedToDropboxQueryObservable() {
    return rxDbService.getQueryObservable(User.class).mapToOneOrNull(User.BRITE_MAPPER);
  }

  // todo, change to bool ? or observable<bool> ?
  public final Observable<User> isLoggedToDropbox() {
    return rxDbService.get(User.class, null, null, null).map(User.SINGLE_MAPPER).concatMap(user -> {
      if (user != null) {
        return Observable.just(user);
      } else {
        return Observable.just(null);
      }
    });
  }

  public final Observable<Long> loginToDropbox(ContentValues values) {
    return rxDbService.insert(User.class, values);
  }

  public final boolean uploadFile(String filePath, boolean delete) {
    File file = new File(filePath);
    Uri u = Uri.parse(filePath);
    String pathOnDropbox = "/" + u.getLastPathSegment();
    FileInputStream inputStream = null;

    if (mDBApi == null) {
      User user = isLoggedToDropbox().toBlocking().first();
      if (user != null) {
        AndroidAuthSession session = new AndroidAuthSession(appKeyPair, user.token());
        mDBApi = new DropboxAPI<AndroidAuthSession>(session);
      }
    }

    if (mDBApi != null) {
      try {
        inputStream = new FileInputStream(file);
        DropboxAPI.Entry response =
            mDBApi.putFile(pathOnDropbox, inputStream, file.length(), null, null);

        if (delete) {
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
      return true;
    }
    return false;
  }

  public final Observable<Integer> logoutFromDropbox() {
    return isLoggedToDropbox().concatMap(user -> rxDbService.delete(User.class, User.selectionById,
        Db.getSelectionArgsForId(user.id())));
  }
}
