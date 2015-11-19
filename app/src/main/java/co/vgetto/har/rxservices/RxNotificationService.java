package co.vgetto.har.rxservices;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import co.vgetto.har.db.entities.History;
import co.vgetto.har.ui.MainActivity;

/**
 * Created by Kovje on 8.11.2015..
 */
public class RxNotificationService {
  private final Context context;
  private final NotificationManager notificationManager;

  public RxNotificationService(Context context, NotificationManager notificationManager) {
    this.context = context;
    this.notificationManager = notificationManager;
  }

  private NotificationCompat.Builder notificationBuilder() {
    return new NotificationCompat.Builder(context);
  }

  private PendingIntent getPendingIntent(int id) {
    Intent i = new Intent(context, MainActivity.class);
    return PendingIntent.getActivity(context, id, i, PendingIntent.FLAG_UPDATE_CURRENT);
  }

  // todo handle stack
  public void newNotification(int historyId, String notificationTitle, String notificationText) {
    Notification notification = notificationBuilder().setSmallIcon(
        android.support.design.R.drawable.notification_template_icon_bg)
        .setContentTitle(notificationTitle)
        .setContentText(notificationText)
//        .setContentIntent(getPendingIntent(historyId))
        .build();

    notificationManager.notify(historyId, notification);
  }
}
