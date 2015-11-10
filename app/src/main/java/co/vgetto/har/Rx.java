package co.vgetto.har;

import android.content.SharedPreferences;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Kovje on 27.8.2015..
 */
public class Rx {
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
