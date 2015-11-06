package co.vgetto.har.ui.rxanimation;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.view.View;

/**
 * Created by Kovje on 13.10.2015..
 */
public class ViewAnimationEvent extends AnimationEvent<View> {
  public enum Kind {
    START, REPEAT, END, SET
  }

  @CheckResult @NonNull
  public static ViewAnimationEvent create(@NonNull View view, @NonNull Kind kind) {
    return new ViewAnimationEvent(view, kind);
  }

  private final Kind kind;

  private ViewAnimationEvent(@NonNull View view, @NonNull Kind kind) {
    super(view);
    this.kind = kind;
  }

  @NonNull
  public Kind kind() {
    return kind;
  }
}
