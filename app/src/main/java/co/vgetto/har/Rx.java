package co.vgetto.har;

import android.content.SharedPreferences;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Kovje on 27.8.2015..
 */
public class Rx {
  private SharedPreferences sharedPreferences;

  public Rx(SharedPreferences preferences) {
    this.sharedPreferences = preferences;
  }

  //emits an editor for shared preferences
  protected Observable<SharedPreferences.Editor> getPreferencesEditor() {
    return Observable.just(sharedPreferences.edit());
  }

  //emits a shared preferences object, so methods can read values
  protected Observable<SharedPreferences> getPreferences() {
    return Observable.just(sharedPreferences);
  }

  protected Observable<Boolean> spWriteString(String key, String value) {
    return getPreferencesEditor().map(editor -> {
      editor.putString(key, value);
      return editor.commit();
    });
  }

  protected Observable<Boolean> spWriteBoolean(String key, Boolean value) {
    return getPreferencesEditor().map(editor -> {
      editor.putBoolean(key, value);
      return editor.commit();
    });
  }

  protected Observable<Boolean> spWriteLong(String key, Long value) {
    return getPreferencesEditor().map(editor -> {
      editor.putLong(key, value);
      return editor.commit();
    });
  }

  protected Observable<String> spReadString(String key) {
    return getPreferences().map(preferences -> preferences.getString(key, ""));
  }

  protected Observable<Boolean> spReadBoolean(String key) {
    return getPreferences().map(preferences -> preferences.getBoolean(key, false));
  }

  protected Observable<Long> spReadLong(String key) {
    return getPreferences().map(preferences -> preferences.getLong(key, 0));
  }

  protected Observable<Boolean> spRemove(String key) {
    return getPreferencesEditor().map(editor -> {
      editor.remove(key);
      return editor.commit();
    });
  }

  // todo move to another class
  public static <T> Observable.Transformer<T, T> schedulersIoUi() {
    return observable -> observable.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread());
  }

  public static <T> Observable.Transformer<T, T> schedulersUiIo() {
    return observable -> observable.subscribeOn(AndroidSchedulers.mainThread())
        .observeOn(Schedulers.io());
  }

  public static <T> Observable.Transformer<T, T> schedulersUiUi() {
    return observable -> observable.subscribeOn(AndroidSchedulers.mainThread())
        .observeOn(AndroidSchedulers.mainThread());
  }
}
