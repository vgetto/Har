package co.vgetto.har.ui.rxanimation;

import android.view.View;
import android.view.animation.Animation;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by Kovje on 13.10.2015..
 */
final class ViewAnimationOnSubscribe implements Observable.OnSubscribe<ViewAnimationEvent>{
  private final View view;
  private final Animation animation;

  ViewAnimationOnSubscribe(View view, Animation animation) {
    this.view = view;
    this.animation = animation;
  }

  @Override public void call(Subscriber<? super ViewAnimationEvent> subscriber) {
    if (!subscriber.isUnsubscribed()) {
      Animation.AnimationListener listener = new Animation.AnimationListener() {
        @Override public void onAnimationStart(Animation animation) {
          subscriber.onNext(ViewAnimationEvent.create(view, ViewAnimationEvent.Kind.START));
        }

        @Override public void onAnimationEnd(Animation animation) {
          subscriber.onNext(ViewAnimationEvent.create(view, ViewAnimationEvent.Kind.END));
          subscriber.onCompleted();
        }

        @Override public void onAnimationRepeat(Animation animation) {
          subscriber.onNext(
              ViewAnimationEvent.create(view, ViewAnimationEvent.Kind.REPEAT));
        }
      };

      animation.setAnimationListener(listener);
      view.setAnimation(animation);
      subscriber.onNext(ViewAnimationEvent.create(view, ViewAnimationEvent.Kind.SET));
    }
  }
}
