package co.vgetto.har.ui;

import android.app.FragmentManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import co.vgetto.har.MyApplication;
import co.vgetto.har.R;
import co.vgetto.har.db.Db;
import co.vgetto.har.db.entities.SavedFile;
import co.vgetto.har.ui.backstack.SavedBackstackFragment;
import co.vgetto.har.ui.backstack.Backstack;
import co.vgetto.har.ui.base.BaseModel;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import rx.Observable;
import rx.Subscription;
import rx.functions.Action0;
import rx.subscriptions.Subscriptions;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity
    implements MainActivityController.ITalkToMainActivity {
  @Inject MainActivityController controller;

  @Inject Backstack backstack;

  @Bind(R.id.drawerLayout) DrawerLayout drawerLayout;

  @Bind(R.id.toolbar) Toolbar toolbar;

  @Bind(R.id.rootLayout) CoordinatorLayout rootLayout;

  @Bind(R.id.navigation) NavigationView navigationView;

  @Bind(R.id.container) LinearLayout container;

  private ActionBarDrawerToggle drawerToggle;

  private Subscription inflateSubscription;

  private SavedBackstackFragment savedBackstackFragment;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);

    setSupportActionBar(toolbar);

    MyApplication.get(this).createMainActivityComponent(this).inject(this);

    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    getSupportActionBar().setDisplayShowHomeEnabled(true);

    drawerToggle = new ActionBarDrawerToggle(MainActivity.this, drawerLayout, R.string.hello_world,
        R.string.hello_world);

    drawerLayout.setDrawerListener(drawerToggle);

    // find the retained fragment on activity restarts

    FragmentManager fm = getFragmentManager();

    savedBackstackFragment = (SavedBackstackFragment) fm.findFragmentByTag("backstack");

    // create the fragment and data the first time
    if (savedBackstackFragment == null) {
      // add the fragment
      savedBackstackFragment = new SavedBackstackFragment();
      fm.beginTransaction().add(savedBackstackFragment, "backstack").commit();
    } else {
      backstack.restoreInstanceState(this, savedBackstackFragment.getSavedBackstackItems());
    }

    controller.init(null);

    navigationView.setNavigationItemSelectedListener(
        new NavigationView.OnNavigationItemSelectedListener() {
          @Override public boolean onNavigationItemSelected(MenuItem menuItem) {
            controller.onNavigationItemSelected(menuItem.getItemId());
            closeDrawer();
            return false;
          }
        });

    Typeface roboto = Typeface.createFromAsset(this.getAssets(), "fonts/Roboto-Light.ttf"); //use this.getAssets if you are calling from an Activity
    Field f = null;
    try {
      f = toolbar.getClass().getDeclaredField("mTitleTextView");
      f.setAccessible(true);
      TextView toolbarTitle = (TextView) f.get(toolbar);
      toolbarTitle.setTypeface(roboto);
    } catch (NoSuchFieldException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    }
  }

  @Override public void onPostCreate(Bundle savedInstanceState) {
    super.onPostCreate(savedInstanceState);
    drawerToggle.syncState();
  }

  public void showArrowInToolbar(boolean b) {
    drawerToggle.setDrawerIndicatorEnabled(!b);
  }

  public Integer closeDrawer() {
    drawerLayout.closeDrawer(GravityCompat.START);
    return 0;
  }

  @Override protected void onResume() {
    super.onResume();
  }

  @Override protected void onRestoreInstanceState(Bundle savedInstanceState) {
    super.onRestoreInstanceState(savedInstanceState);
  }

  @Override protected void onPause() {
    if (inflateSubscription != null) {
      inflateSubscription.unsubscribe();
      inflateSubscription = null;
    }
    super.onPause();
  }

  @Override protected void onDestroy() {
    MyApplication.get(this).releaseMainActivityComponent();
    super.onDestroy();
  }

  @Override public void onConfigurationChanged(Configuration newConfig) {
    super.onConfigurationChanged(newConfig);
    drawerToggle.onConfigurationChanged(newConfig);
  }

  /*
  @Override public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return true;
  }
  */

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.

    if (drawerToggle.onOptionsItemSelected(item)) return true;

    int id = item.getItemId();

    return super.onOptionsItemSelected(item);
  }

  @Override public boolean onSupportNavigateUp() {
    //return presenter.onBackPressed(getCurrentViewId());
    return controller.onSupportNavigateUp();
  }

  @Override public void onBackPressed() {
    if (backstack.getSize() > 1) {
      if (!controller.onBackPressed(backstack.getCurrentViewId(), backstack.getCurrentView())) {
        // main controller didn't process onBackPressed, let android handle it
        super.onBackPressed();
      }
    } else {
      // you can't go back, backstack contains only 1 item, let android handle it
      super.onBackPressed();
    }
  }


  @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    controller.onActivityResult(requestCode, resultCode, data);
    super.onActivityResult(requestCode, resultCode, data);
  }

  public Backstack getBackstack() {
    return backstack;
  }

  @Override protected void onSaveInstanceState(Bundle outState) {
    // save backstack items to fragment as an List<SavedBackStackItem>
    savedBackstackFragment.saveBackstackItems(backstack.onSaveInstanceState());
    super.onSaveInstanceState(controller.onSaveInstanceState(
        outState)); // probably unecesarry call to save instance state
  }

  // ITalkToMainActivity interface method implementation

  @Override public void showLayout(int nextLayoutId, int action, BaseModel model) {
    if (inflateSubscription != null) {
      if (!inflateSubscription.isUnsubscribed()) {
        inflateSubscription.unsubscribe();
      }
      inflateSubscription = null;
    }

    if (action == UiConstants.ACTION_REPLACE) {
      if (backstack.getCurrentViewId() == UiConstants.HISTORY_DETAILS) {
        backstack.pop();
      }
    }

    // init transaction
    inflateSubscription =
        MainActivityTransactions.getLayoutTransactionObservable(this, container, getBackstack(),
            nextLayoutId, action, model).doOnCompleted(() -> {
          // check if current view is scheduleConfiguration
          // if it is set arrow, if not remove arrow from toolbar
          // todo call to controler / presenter !
          if ((backstack.getCurrentViewId() == UiConstants.HISTORY_DETAILS) || (backstack.getCurrentViewId() == UiConstants.ADD_OR_EDIT_SCHEDULE) || (backstack.getCurrentViewId() == UiConstants.ADD_OR_EDIT_TRIGGER)) {
            showArrowInToolbar(true);
          } else {
            showArrowInToolbar(false);
          }
        }).subscribe();
  }

  @Override public void showSnackbar(String snackbarText) {
    Snackbar.make(rootLayout, snackbarText, Snackbar.LENGTH_LONG).show();
  }

  @Override public void setTitle(String title) {
    if (title != null) {
      toolbar.setTitle(title);
    } else {
      toolbar.setTitle("Har");
    }
  }
}
