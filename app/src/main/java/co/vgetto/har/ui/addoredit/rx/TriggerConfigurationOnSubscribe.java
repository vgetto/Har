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
import co.vgetto.har.Rx;
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

  @Bind(R.id.etPhoneNumber) EditText etPhoneNumber;

  @Bind(R.id.etSmsText) EditText etSmsText;

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
      etPhoneNumber.setText(model.getPhoneNumber());
      etSmsText.setText(model.getSmsText());
    }
  }

  @Override public void call(Subscriber<? super TriggerConfigurationModel> subscriber) {
    this.subscriber = subscriber;
    createObservable();
  }

  public void createObservable() {
    Observable.combineLatest(RxAdapterView.itemSelections(spinnerTriggerType),
        Rx.subscribeToTextChanges(etPhoneNumber), Rx.subscribeToTextChanges(etSmsText), (Integer type, TextViewTextChangeEvent
            numberChange, TextViewTextChangeEvent smsChange) -> {
          triggerConfigurationModel.setType(type);
          // TODO based on type, hide/show etSmsText, also enable/disable next based on this
          triggerConfigurationModel.setPhoneNumber(numberChange.text().toString());
          triggerConfigurationModel.setSmsText(smsChange.text().toString());
          return triggerConfigurationModel;
          //AdapterView<SpinnerAdapter> adapterView = spinnerTriggerType;
          //long id = adapterView.getSelectedItemId();
        }).subscribe(confModel -> subscriber.onNext(confModel));
  }
}
