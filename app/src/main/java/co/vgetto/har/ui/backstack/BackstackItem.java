package co.vgetto.har.ui.backstack;

import android.os.Parcelable;
import android.view.View;
import auto.parcel.AutoParcel;

/**
 * Created by Kovje on 10.10.2015..
 */
@AutoParcel public abstract class BackstackItem implements Parcelable{
  public abstract int viewId();
  public abstract View view();

  public static BackstackItem create(int viewId, View view) {
    return new AutoParcel_BackstackItem(viewId, view);
  }
}
