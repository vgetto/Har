package co.vgetto.har.ui.animation;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import co.vgetto.har.R;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by Kovje on 9.10.2015..
 */
// remove onsubscribe implementation ?
public class BaseAnimationObservable {
  public static final int MOVE_TO_LEFT = 100;
  public static final int MOVE_FROM_LEFT = 101;
  public static final int MOVE_TO_RIGHT = 102;
  public static final int MOVE_FROM_RIGHT = 103;
  public static final int SCALE_FROM_BOTTOM_RIGHT = 105;
  public static final int SCALE_TO_BOTTOM_RIGHT = 106;
  public static final int FADE_IN = 107;
  public static final int FADE_OUT = 108;
  public static final int MOVE_DOWN = 109;
  public static final int MOVE_UP = 110;
  public static final int MOVE_FROM_DOWN = 111;

  public static final int ANIMATION_START = 1;
  public static final int ANIMATION_END = 2;


  protected final View view;
  protected final Animation animation;

  private Animation load(Context context, int id) {
    return AnimationUtils.loadAnimation(context, id);
  }

  protected Animation loadAnimation(int animationType) {
    Animation animation;
    switch (animationType) {
      case MOVE_TO_LEFT:
        animation = load(view.getContext(), R.anim.move_to_left);
        break;
      case MOVE_FROM_LEFT:
        animation = load(view.getContext(), R.anim.move_from_left);
        break;
      case MOVE_TO_RIGHT:
        animation = load(view.getContext(), R.anim.move_to_right);
        break;
      case MOVE_FROM_RIGHT:
        animation = load(view.getContext(), R.anim.move_from_right);
        break;
      case MOVE_DOWN:
        animation = load(view.getContext(), R.anim.move_down);
        break;
      case MOVE_UP:
        animation = load(view.getContext(), R.anim.move_up);
        break;
      case FADE_IN:
        animation = load(view.getContext(), R.anim.fade_in);
        break;
      case FADE_OUT:
        animation = load(view.getContext(), R.anim.fade_out);
        break;
      case MOVE_FROM_DOWN:
        animation = load(view.getContext(), R.anim.move_from_down);
        break;
      case SCALE_FROM_BOTTOM_RIGHT:
        animation = new ScaleAnimation(0f, 1f, 0f, 1f, Animation.RELATIVE_TO_SELF, 0.9f, Animation.RELATIVE_TO_SELF, 0.9f);
        animation.setDuration(200);
        animation.setFillAfter(true);
        break;
      case SCALE_TO_BOTTOM_RIGHT:
        animation = new ScaleAnimation(1f, 0f, 1f, 0f, Animation.RELATIVE_TO_SELF, 0.9f, Animation.RELATIVE_TO_SELF, 0.9f);
        animation.setDuration(200);
        animation.setFillBefore(true);
        animation.setFillAfter(false);
        break;
      default:
        animation = load(view.getContext(), R.anim.move_from_right);
    }
    return animation;
  }

  public BaseAnimationObservable(View view, int animation) {
    this.view = view;
    this.animation = loadAnimation(animation);
  }

}
