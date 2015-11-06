package co.vgetto.har.ui;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import co.vgetto.har.R;
import co.vgetto.har.ui.addoredit.addedittrigger.AddOrEditTriggerLayout;
import co.vgetto.har.ui.addoredit.model.AddOrEditTriggerModel;
import co.vgetto.har.ui.backstack.Backstack;
import co.vgetto.har.ui.base.BaseModel;
import co.vgetto.har.ui.addoredit.model.AddOrEditScheduleModel;
import co.vgetto.har.ui.addoredit.addeditschedule.AddOrEditScheduleLayout;
import co.vgetto.har.ui.historydetail.HistoryDetailLayout;
import co.vgetto.har.ui.historylist.HistoryListLayout;
import co.vgetto.har.ui.schedulelist.ScheduleListLayout;
import co.vgetto.har.ui.settings.SettingsLayout;
import co.vgetto.har.ui.rxanimation.RxViewAnimation;
import co.vgetto.har.ui.rxanimation.ViewAnimationEvent;
import co.vgetto.har.ui.triggerlist.TriggerListLayout;
import rx.Observable;

/**
 * Created by Kovje on 19.10.2015..
 */
public class MainActivityTransactions {
  public static Observable<Integer> initTransaction(Context context, ViewGroup container,
      Backstack backstack, int nextLayoutId) {
    return Observable.create(subscriber -> {
      container.removeAllViews();

      if (backstack.getSize() > 0) {
        container.addView(backstack.getCurrentView());
      } else {
        container.addView(getLayoutById(context, nextLayoutId, null));
        backstack.push(nextLayoutId, container.getChildAt(0));
      }
      subscriber.onCompleted();
    });
  }

  public static Observable<Integer> replaceTransaction(Context context, ViewGroup container,
      Backstack backstack, int nextLayoutId) {
    return RxViewAnimation.animateRemove(backstack.pop(), R.anim.fade_out)
        .filter(viewAnimationEvent -> viewAnimationEvent.kind() == ViewAnimationEvent.Kind.START)
        .flatMap(
            e -> RxViewAnimation.animateAdd(container, getLayoutById(context, nextLayoutId, null),
                R.anim.move_from_right))
        .filter(viewAnimationAdd -> viewAnimationAdd.kind() == ViewAnimationEvent.Kind.END)
        .map(end -> {
          backstack.push(nextLayoutId, container.getChildAt(0));
          return 0;
        });
  }

  public static Observable<Integer> addOnTopTransaction(Context context, ViewGroup container,
      Backstack backstack, int nextLayoutId, BaseModel model) {
    //Animation a = new ScaleAnimation(0f, 1f, 0f, 1f, Animation.ABSOLUTE, xy.first, Animation.ABSOLUTE, xy.second);
    Animation a = new ScaleAnimation(0f, 1f, 0f, 1f, Animation.RELATIVE_TO_SELF, 0.9f,
        Animation.RELATIVE_TO_SELF, 0.9f);
    a.setInterpolator(new AccelerateInterpolator(1.0f));
    a.setDuration(300);
    a.setFillAfter(true);

    return RxViewAnimation.animateRemove(backstack.getCurrentView(), R.anim.fade_out)
        .filter(animationEvent -> animationEvent.kind() == ViewAnimationEvent.Kind.START)
        .flatMap(event -> RxViewAnimation.animateAdd(container,
            getLayoutById(context, nextLayoutId, model), a))
        .filter(viewAnimationAdd -> viewAnimationAdd.kind() == ViewAnimationEvent.Kind.END)
        .map(end -> {
          backstack.push(nextLayoutId, container.getChildAt(0));
          return 0;
        });
  }

  public static Observable<Integer> goBackTransaction(Context context, ViewGroup container,
      Backstack backstack, int nextLayoutId) {
    Animation animation;
    animation = new ScaleAnimation(1f, 0f, 1f, 0f, Animation.RELATIVE_TO_SELF, 0.9f,
        Animation.RELATIVE_TO_SELF, 0.9f);
    animation.setInterpolator(new AccelerateInterpolator(1.5f));
    animation.setDuration(200);
    animation.setFillBefore(true);
    animation.setFillAfter(false);

    return RxViewAnimation.animateRemove(backstack.pop(), animation)
        .filter(animationEvent -> animationEvent.kind() == ViewAnimationEvent.Kind.START)
        .flatMap(
            e -> RxViewAnimation.animateAdd(container, backstack.getCurrentView(), R.anim.fade_in))
        .filter(viewAnimationEvent -> viewAnimationEvent.kind() == ViewAnimationEvent.Kind.END)
        .map(end -> 0); // return 0
  }

  public static Observable<Integer> getObservableTransactionForAction(Context context,
      ViewGroup container, Backstack backstack, int nextLayoutId, int action, BaseModel model) {
    Observable<Integer> observable;

    switch (action) {
      case UiConstants.ACTION_INIT:
        observable = initTransaction(context, container, backstack, nextLayoutId);
        break;
      case UiConstants.ACTION_REPLACE:
        observable = replaceTransaction(context, container, backstack, nextLayoutId);
        break;
      case UiConstants.ACTION_ADD_ON_TOP:
        observable = addOnTopTransaction(context, container, backstack, nextLayoutId, model);
        break;
      case UiConstants.ACTION_BACK:
        observable = goBackTransaction(context, container, backstack, nextLayoutId);
        break;
      default:
        observable = initTransaction(context, container, backstack, nextLayoutId);
        break;
    }
    return observable;
  }

  private static View getLayoutById(Context context, int layoutId, BaseModel model) {
    View v;
    switch (layoutId) {
      case UiConstants.SCHEDULES:
        v = new ScheduleListLayout(context, null);
        break;
      case UiConstants.TRIGGERS:
        v = new TriggerListLayout(context, null);
        break;
      case UiConstants.HISTORY:
        v = new HistoryListLayout(context, null);
        break;
      case UiConstants.HISTORY_DETAILS:
        v = new HistoryDetailLayout(context, model);
        break;
      case UiConstants.ADD_OR_EDIT_SCHEDULE:
        v = new AddOrEditScheduleLayout(context, model);
        break;
      case UiConstants.ADD_OR_EDIT_TRIGGER:
        v = new AddOrEditTriggerLayout(context, model);
        break;
      case UiConstants.SETTINGS:
        v = new SettingsLayout(context, null);
        break;
      default:
        v = null;
        break;
    }
    return v;
  }

  public static Observable<Integer> getLayoutTransactionObservable(Context context,
      ViewGroup container, Backstack backstack, int nextLayoutId, int action, BaseModel model) {
    return getObservableTransactionForAction(context, container, backstack, nextLayoutId, action,
        model);
  }
}
