package co.vgetto.har.ui.addoredit.base;

import android.content.ContentValues;
import co.vgetto.har.ui.addoredit.model.AddOrEditScheduleModel;
import co.vgetto.har.ui.base.BaseModel;

/**
 * Created by Kovje on 26.10.2015..
 */
public interface BaseAddEditPresenter {
  ContentValues getContentValuesFromModel(BaseModel baseModel);
}
