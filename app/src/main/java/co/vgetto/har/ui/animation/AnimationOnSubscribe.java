package co.vgetto.har.ui.animation;

import android.view.View;
import android.view.animation.Animation;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by Kovje on 9.10.2015..
 */
public class AnimationOnSubscribe extends BaseAnimationObservable implements Observable.OnSubscribe<Integer> {
  private final View view;

  public AnimationOnSubscribe(View view, int animation) {
    super(view, animation);
    this.view = view;
  }

  @Override public void call(Subscriber<? super Integer> subscriber) {
    Animation.AnimationListener listener = new Animation.AnimationListener() {
      @Override public void onAnimationStart(Animation animation) {
          subscriber.onNext(BaseAnimationObservable.ANIMATION_START);
      }

      @Override public void onAnimationEnd(Animation animation) {
          subscriber.onNext(BaseAnimationObservable.ANIMATION_END);
          subscriber.onCompleted();
      }

      @Override public void onAnimationRepeat(Animation animation) {

      }
    };
    animation.setAnimationListener(listener);
    view.startAnimation(animation);
  }
}
