package co.vgetto.har.ui.addoredit.rx;

import android.view.View;
import android.widget.DatePicker;
import butterknife.Bind;
import butterknife.ButterKnife;
import co.vgetto.har.R;
import java.util.List;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by Kovje on 30.9.2015..
 */
public class DatePickerOnSubscribe implements Observable.OnSubscribe<Integer> {
  @Bind(R.id.datePicker)
  DatePicker datePicker;

  private final List<Integer> date;

  public DatePickerOnSubscribe(View v, List<Integer> date) {
    ButterKnife.bind(this, v);
    this.date = date;
  }

  @Override public void call(Subscriber<? super Integer> subscriber) {
    DatePicker.OnDateChangedListener listener = new DatePicker.OnDateChangedListener() {
      @Override
      public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        if(!subscriber.isUnsubscribed()) {
          subscriber.onNext(year);
          subscriber.onNext(monthOfYear);
          subscriber.onNext(dayOfMonth);
        }
      }
    };
    datePicker.init(date.get(0), date.get(1), date.get(2), listener);
    subscriber.onNext(date.get(0));
    subscriber.onNext(date.get(1));
    subscriber.onNext(date.get(2));
  }
}
