package co.vgetto.har;

import android.net.Uri;

/**
 * Created by Kovje on 17.8.2015..
 */
public class Constants {

  public static final long HOUR_IN_MILLIS = 60000 * 60;
  // dropbox
  public static final String APP_KEY = "spa4gp48wdo9cr9";
  public static final String APP_SECRET = "lrxuuw4m0ydfrql";

  public static final String AUTHORITY = "co.vgetto.har.syncadapter.provider";
  // An account type, in the form of a domain name
  public static final String ACCOUNT_TYPE = "co.vgetto.har.syncadapter";
  // The account name
  public static final String ACCOUNT = "dummyaccount";

  public static final Uri BASE_URI = Uri.parse("content://" + AUTHORITY);

  public static final Uri BASE_SCHEDULE_URI = Uri.parse("content://" + AUTHORITY + "/schedule");
  public static final Uri BASE_TRIGGER_URI = Uri.parse("content://" + AUTHORITY + "/trigger");
  public static final Uri BASE_HISTORY_URI = Uri.parse("content://" + AUTHORITY + "/history");
  public static final Uri BASE_USER_URI = Uri.parse("content://" + AUTHORITY + "/user");

}
