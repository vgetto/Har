package co.vgetto.har;

import android.widget.TextView;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.jakewharton.rxbinding.widget.TextViewTextChangeEvent;
import java.util.concurrent.TimeUnit;
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

  public static <T> Observable.Transformer<T, T> schedulerIoIo() {
    return observable -> observable.subscribeOn(Schedulers.io())
        .observeOn(Schedulers.io());
  }

  public static Observable<TextViewTextChangeEvent> subscribeToTextChanges(TextView view) {
    return RxTextView.textChangeEvents(view).debounce(400, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread());
  }
}
