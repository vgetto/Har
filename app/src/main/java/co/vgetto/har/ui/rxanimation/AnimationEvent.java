package co.vgetto.har.ui.rxanimation;

import android.support.annotation.NonNull;
import android.view.View;

/**
 * Created by Kovje on 13.10.2015..
 */
public abstract class AnimationEvent<T extends View> {
  private final T view;

  protected AnimationEvent(@NonNull T view) {
    this.view = view;
  }

  /** The view from which this event occurred. */
  public @NonNull T view() {
    return view;
  }
}
