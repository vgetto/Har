package co.vgetto.har.ui.addoredit.rx;

import android.view.View;
import co.vgetto.har.Rx;
import co.vgetto.har.ui.addoredit.model.AddOrEditScheduleModel;
import co.vgetto.har.ui.addoredit.model.AddOrEditTriggerModel;
import co.vgetto.har.ui.addoredit.base.BaseAddEditModel;
import co.vgetto.har.ui.addoredit.model.RecordingConfigurationModel;
import co.vgetto.har.ui.addoredit.model.TriggerConfigurationModel;
import co.vgetto.har.ui.addoredit.model.UploadConfigurationModel;
import java.util.List;
import rx.Observable;

/**
 * Created by Kovje on 20.10.2015..
 */
public class PageSubscriptions {
  public static Observable<List<Integer>> setDatePickerObservable(View v,
      List<Integer> currentDate) {
    return Observable.create(new DatePickerOnSubscribe(v, currentDate))
        .compose(Rx.schedulersUiUi())
        .buffer(3)
        .compose(Rx.schedulersUiUi());
  }

  public static Observable<List<Integer>> setTimePickerObservable(View v, String currentDate,
      List<Integer> currentTime) {
    return Observable.create(new TimePickerOnSubscribe(v, currentDate, currentTime))
        .buffer(2)
        .compose(Rx.schedulersUiUi());
  }

  public static Observable<TriggerConfigurationModel> setTriggerConfigurationObservable(View v,
      BaseAddEditModel baseModel) {
    // only trigger has a trigger configuration, don't have to check if schedule or trigger add/edit
    AddOrEditTriggerModel model = (AddOrEditTriggerModel) baseModel;
    TriggerConfigurationModel triggerConfigurationModel = null;

    if (model.isModelRestored()) {
      triggerConfigurationModel = model.getTriggerConfigurationModel();
    }

    return Observable.create(new TriggerConfigurationOnSubscribe(v, triggerConfigurationModel))
        .compose(Rx.schedulersUiUi());
  }

  public static Observable<RecordingConfigurationModel> setRecordingConfigurationObservable(View v,
      BaseAddEditModel model) {
    if (model.isModelRestored()) {
      // if model is restored, check if it is a schedule or trigger model, get recording configuration and make an observable
      if (model instanceof AddOrEditScheduleModel) {
        return Observable.create(new RecordingConfigurationOnSubscribe(v,
            ((AddOrEditScheduleModel) model).getRecordingConfigurationModel()))
            .compose(Rx.schedulersUiUi());
      } else if (model instanceof AddOrEditTriggerModel) {
        return Observable.create(new RecordingConfigurationOnSubscribe(v,
            ((AddOrEditTriggerModel) model).getRecordingConfigurationModel()))
            .compose(Rx.schedulersUiUi());
      }
    }
    // if model isn't restored, just pass null as model
    return Observable.create(new RecordingConfigurationOnSubscribe(v, null))
        .compose(Rx.schedulersUiUi());
  }

  public static Observable<UploadConfigurationModel> setUploadConfigurationObservable(View v,
      BaseAddEditModel model) {
    if (model.isModelRestored()) {
      if (model instanceof AddOrEditScheduleModel) {
        return Observable.create(new UploadConfigurationOnSubscribe(v,
            ((AddOrEditScheduleModel) model).getUploadConfigurationModel()))
            .compose(Rx.schedulersUiUi());
      } else if (model instanceof AddOrEditTriggerModel) {
        return Observable.create(new UploadConfigurationOnSubscribe(v,
            ((AddOrEditTriggerModel) model).getUploadConfigurationModel()))
            .compose(Rx.schedulersUiUi());
      }
    }
    return Observable.create(new UploadConfigurationOnSubscribe(v, null))
        .compose(Rx.schedulersUiUi());
  }
}
