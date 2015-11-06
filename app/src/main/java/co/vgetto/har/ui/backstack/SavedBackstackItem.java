package co.vgetto.har.ui.backstack;

import android.os.Parcelable;
import auto.parcel.AutoParcel;
import co.vgetto.har.ui.base.BaseModel;

/**
 * Created by Kovje on 10.10.2015..
 */
@AutoParcel public abstract class SavedBackstackItem implements Parcelable{
  public abstract int viewId();
  public abstract BaseModel model();

  public static SavedBackstackItem create(int viewId, BaseModel model) {
    return new AutoParcel_SavedBackstackItem(viewId, model);
  }
}
