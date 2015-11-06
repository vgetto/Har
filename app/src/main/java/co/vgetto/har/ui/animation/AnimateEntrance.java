package co.vgetto.har.ui.animation;

import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by Kovje on 8.10.2015..
 */

/**
 *  TODO remove whole animation package, change where this is used to user rxanimation
 */
public class AnimateEntrance extends BaseAnimationObservable implements Observable.OnSubscribe<Integer> {
  private final ViewGroup container;

  public AnimateEntrance(ViewGroup container, View view, int animation) {
    super(view, animation);
    this.container = container;
  }

  @Override public void call(Subscriber<? super Integer> subscriber) {
    if (!subscriber.isUnsubscribed()) {
      Animation.AnimationListener listener = new Animation.AnimationListener() {
        @Override public void onAnimationStart(Animation animation) {
          subscriber.onNext(ANIMATION_START);
        }

        @Override public void onAnimationEnd(Animation animation) {
          subscriber.onNext(ANIMATION_END);
          subscriber.onCompleted();
        }

        @Override public void onAnimationRepeat(Animation animation) {

        }
      };
      animation.setAnimationListener(listener);
      view.startAnimation(animation);
      container.addView(view);
    }
  }
}
