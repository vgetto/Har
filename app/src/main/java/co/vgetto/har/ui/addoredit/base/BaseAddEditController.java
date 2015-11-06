package co.vgetto.har.ui.addoredit.base;

import co.vgetto.har.ui.addoredit.model.RecordingConfigurationModel;
import co.vgetto.har.ui.addoredit.model.TriggerConfigurationModel;
import co.vgetto.har.ui.addoredit.model.UploadConfigurationModel;
import co.vgetto.har.ui.base.BaseController;
import co.vgetto.har.ui.base.BaseModel;
import java.util.List;

/**
 * Created by Kovje on 26.10.2015..
 */
public interface BaseAddEditController extends BaseController {
  // todo, when fast clicked next crashes the app, you can click if you're fast, disable next button faster !!
  void init(BaseModel savedModel);
  void btnBackClicked();
  void btnNextClicked();
  boolean onBackPressed();
  void dateChanged(List<Integer> newDate);
  void timeChanged(List<Integer> newTime);
  void triggerConfigurationChanged(TriggerConfigurationModel triggerConfigurationModel);
  void recordingConfigurationChanged(RecordingConfigurationModel recordingConfigurationModel);
  void uploadConfigurationChanged(UploadConfigurationModel uploadConfigurationModel);
}
