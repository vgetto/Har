package co.vgetto.har.ui.settings;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import co.vgetto.har.R;

/**
 * Created by Kovje on 11.10.2015..
 */
public class SettingsLayout extends LinearLayout {
  public SettingsLayout(Context context, AttributeSet attrs) {
    super(context, attrs);
    LayoutInflater.from(context).inflate(R.layout.settings_layout, this, true);
  }

  public SettingsLayout(Context context) {
    super(context);
    LayoutInflater.from(context).inflate(R.layout.settings_layout, this, true);
  }

  public SettingsLayout(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }
}
