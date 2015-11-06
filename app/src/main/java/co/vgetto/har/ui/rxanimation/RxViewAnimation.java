package co.vgetto.har.ui.rxanimation;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import rx.Observable;

/**
 * Created by Kovje on 13.10.2015..
 */
public final class RxViewAnimation {

  private static Animation loadAnimation(Context context, int animationResId) {
    return AnimationUtils.loadAnimation(context, animationResId);
  }

  public static Observable<ViewAnimationEvent> animate(@NonNull View view, int animation) {
    return Observable.create(
        new ViewAnimationOnSubscribe(view, loadAnimation(view.getContext(), animation)));
  }

  public static Observable<ViewAnimationEvent> animate(@NonNull View view, Animation animation) {
    return Observable.create(new ViewAnimationOnSubscribe(view, animation));
  }

  public static Observable<ViewAnimationEvent> animateAdd(@NonNull ViewGroup container,
      @NonNull View view, @NonNull int animation) {
    return animate(view, animation).map(animationEvent -> {
      if (animationEvent.kind() == ViewAnimationEvent.Kind.SET) {
        container.addView(animationEvent.view());
      }
      return animationEvent;
    });
  }

  public static Observable<ViewAnimationEvent> animateAdd(@NonNull ViewGroup container,
      @NonNull View view, @NonNull Animation animation) {
    return animate(view, animation).map(animationEvent -> {
      if (animationEvent.kind() == ViewAnimationEvent.Kind.SET) {
        container.addView(animationEvent.view());
      }
      return animationEvent;
    });
  }

  public static Observable<ViewAnimationEvent> animateRemove(@NonNull View view,
      @NonNull int animation) {
    return animate(view, animation).map(animationEvent -> {
      if (animationEvent.kind() == ViewAnimationEvent.Kind.SET) {
        View v = animationEvent.view();
        ViewGroup c = (ViewGroup) v.getParent();
        c.removeView(v);
        v = null;
        c = null;
        //container.removeView(animationEvent.view());
      }
      return animationEvent;
    });
  }

  public static Observable<ViewAnimationEvent> animateRemove(@NonNull View view,
      @NonNull Animation animation) {
    return animate(view, animation).map(animationEvent -> {
      if (animationEvent.kind() == ViewAnimationEvent.Kind.SET) {
        View v = animationEvent.view();
        ViewGroup c = (ViewGroup) v.getParent();
        c.removeView(v);
        v = null;
        c = null;
      }
      return animationEvent;
    });
  }

  private RxViewAnimation() {
    throw new AssertionError("No instances.");
  }
}
