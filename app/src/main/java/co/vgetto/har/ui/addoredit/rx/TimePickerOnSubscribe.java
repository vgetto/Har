package co.vgetto.har.ui.addoredit.rx;

import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;
import butterknife.Bind;
import butterknife.ButterKnife;
import co.vgetto.har.R;
import java.util.List;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by Kovje on 30.9.2015..
 */
public class TimePickerOnSubscribe implements Observable.OnSubscribe<Integer> {
  @Bind(R.id.timePicker)
  TimePicker timePicker;

  @Bind(R.id.tvCurrentDate)
  TextView tvCurrentDate;

  private final List<Integer> time;
  private final String currentDate;


  public TimePickerOnSubscribe(View v, String currentDate, List<Integer> time) {
    ButterKnife.bind(this, v);
    this.time = time;
    this.currentDate = currentDate;
  }

  @Override public void call(Subscriber<? super Integer> subscriber) {
    TimePicker.OnTimeChangedListener listener = new TimePicker.OnTimeChangedListener() {
      @Override public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
        if(!subscriber.isUnsubscribed()) {
          subscriber.onNext(hourOfDay);
          subscriber.onNext(minute);
        }
      }
    };

    timePicker.setCurrentHour(time.get(0));
    timePicker.setCurrentMinute(time.get(1));
    timePicker.setOnTimeChangedListener(listener);
    tvCurrentDate.setText(currentDate);
    subscriber.onNext(time.get(0));
    subscriber.onNext(time.get(1));
  }
}
