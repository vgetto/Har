package co.vgetto.har.rxservices;

import android.content.SharedPreferences;
import rx.Observable;

/**
 * Created by Kovje on 7.11.2015..
 */
public class RxSharedPreferences {
  private SharedPreferences sharedPreferences;

  public RxSharedPreferences(SharedPreferences preferences) {
    this.sharedPreferences = preferences;
  }

  //emits an editor for shared preferences
  private Observable<SharedPreferences.Editor> getPreferencesEditor() {
    return Observable.just(sharedPreferences.edit());
  }

  //emits a shared preferences object, so methods can read values
  private Observable<SharedPreferences> getPreferences() {
    return Observable.just(sharedPreferences);
  }

  protected final Observable<Boolean> spWriteString(String key, String value) {
    return getPreferencesEditor().map(editor -> {
      editor.putString(key, value);
      return editor.commit();
    });
  }

  protected final Observable<Boolean> spWriteBoolean(String key, Boolean value) {
    return getPreferencesEditor().map(editor -> {
      editor.putBoolean(key, value);
      return editor.commit();
    });
  }

  protected final Observable<Boolean> spWriteLong(String key, Long value) {
    return getPreferencesEditor().map(editor -> {
      editor.putLong(key, value);
      return editor.commit();
    });
  }

  protected final Observable<String> spReadString(String key) {
    return getPreferences().map(preferences -> preferences.getString(key, ""));
  }

  protected final Observable<Boolean> spReadBoolean(String key) {
    return getPreferences().map(preferences -> preferences.getBoolean(key, false));
  }

  protected final Observable<Long> spReadLong(String key) {
    return getPreferences().map(preferences -> preferences.getLong(key, 0));
  }

  protected final Observable<Boolean> spRemove(String key) {
    return getPreferencesEditor().map(editor -> {
      editor.remove(key);
      return editor.commit();
    });
  }
}
