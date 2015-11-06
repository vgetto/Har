package co.vgetto.har.ui.addoredit.rx;

import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import butterknife.Bind;
import butterknife.ButterKnife;
import co.vgetto.har.R;
import co.vgetto.har.ui.addoredit.model.AddOrEditTriggerModel;
import co.vgetto.har.ui.addoredit.model.TriggerConfigurationModel;
import co.vgetto.har.ui.addoredit.model.UploadConfigurationModel;
import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxAdapterView;
import com.jakewharton.rxbinding.widget.RxCompoundButton;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.jakewharton.rxbinding.widget.TextViewTextChangeEvent;
import rx.Observable;
import rx.Subscriber;
import timber.log.Timber;

/**
 * Created by Kovje on 1.10.2015..
 */
public class TriggerConfigurationOnSubscribe
      implements Observable.OnSubscribe<TriggerConfigurationModel> {
  @Bind(R.id.spinnerTriggerType) Spinner spinnerTriggerType;

  @Bind(R.id.etTriggerNumber) EditText etTriggerNumber;

  private Subscriber subscriber;
  private TriggerConfigurationModel triggerConfigurationModel;

  public TriggerConfigurationOnSubscribe(View v, TriggerConfigurationModel model) {
    ButterKnife.bind(this, v);
    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(v.getContext(), R.array.trigger_type_spinner, android.R.layout.simple_spinner_item);
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    spinnerTriggerType.setAdapter(adapter);

    if (model == null) {
      triggerConfigurationModel = new TriggerConfigurationModel();
    } else {
      this.triggerConfigurationModel = model;
      // todo vidi jel ovo dobro
      spinnerTriggerType.setSelection(model.getType());
      etTriggerNumber.setText(model.getPhoneNumber());
    }
  }

  @Override public void call(Subscriber<? super TriggerConfigurationModel> subscriber) {
    this.subscriber = subscriber;
    createObservable();
  }

  public void createObservable() {
    Observable.combineLatest(RxAdapterView.itemSelections(spinnerTriggerType),
        RxTextView.textChangeEvents(etTriggerNumber),
        (Integer type, TextViewTextChangeEvent numberChange) -> {
          Timber.i("SPINNER event -> " + type.toString());
          triggerConfigurationModel.setType(type);
          triggerConfigurationModel.setPhoneNumber(numberChange.text().toString());
          AdapterView<SpinnerAdapter> adapterView = spinnerTriggerType;
          long id = adapterView.getSelectedItemId();
          Timber.i("SPINNER SELECTED ID -> " + Long.toString(id));
          return triggerConfigurationModel;
        }).subscribe(confModel -> subscriber.onNext(confModel));
  }
}
