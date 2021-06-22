package com.tfd.ffcsez;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class FCMService extends FirebaseMessagingService {

    // Override onMessageReceived() method to extract the
    // title and
    // body from the message passed in FCM
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // First case when notifications are received via
        // data event
        // Here, 'title' and 'message' are the assumed names
        // of JSON
        // attributes. Since here we do not have any data
        // payload, This section is commented out. It is
        // here only for reference purposes.
		/*if(remoteMessage.getData().size()>0){
			showNotification(remoteMessage.getData().get("title"),
						remoteMessage.getData().get("message"));
		}*/

        // Second case when notification payload is
        // received.
        if (remoteMessage.getNotification() != null) {
            // Since the notification is received directly from
            // FCM, the title and the body can be fetched
            // directly as below.
            showNotification(
                    remoteMessage.getNotification().getTitle(),
                    remoteMessage.getNotification().getBody());
        }
    }

    // Method to get the custom Design for the display of
    // notification.
    private RemoteViews getCustomDesign(String title,
                                        String message) {
        RemoteViews remoteViews = new RemoteViews(
                getApplicationContext().getPackageName(),
                R.layout.notification);
        remoteViews.setTextViewText(R.id.title, title);
        remoteViews.setTextViewText(R.id.message, message);
        remoteViews.setImageViewResource(R.id.icon, R.drawable.delete);
        return remoteViews;
    }

    // Method to display the notifications
    public void showNotification(String title, String message) {
        // Pass the intent to switch to the MainActivity
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("refreshNotif", true);
        // Here FLAG_ACTIVITY_CLEAR_TOP flag is set to clear
        // the activities present in the activity stack,
        // on the top of the Activity that is to be launched
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        // Assign channel ID
        String channel_id = "ffcsez_notif_channel";
        // Pass the intent to PendingIntent to start the
        // next Activity
        PendingIntent pendingIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(),
                intent, PendingIntent.FLAG_ONE_SHOT);

        // Create a Builder object using NotificationCompat
        // class. This will allow control over all the flags
        NotificationCompat.Builder builder = new NotificationCompat
                .Builder(getApplicationContext(), channel_id)
                .setSmallIcon(R.drawable.ic_done)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setGroup("FFCSeZ")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setOnlyAlertOnce(true)
                .setContentIntent(pendingIntent);

        // A customized design for the notification can be
        // set only for Android versions 4.1 and above. Thus
        // condition for the same is checked here.
        //builder = builder.setContent(
        //       getCustomDesign(title, message));
        // Create an object of NotificationManager class to
        // notify the
        // user of events that happen in the background.

        NotificationCompat.Builder groupNotifications = new NotificationCompat.Builder(getApplicationContext(), channel_id)
                .setContentTitle("Get the latest updates")
                .setContentText("Refresh your app or tap on this notification")
                .setSmallIcon(R.drawable.ic_info)
                .setStyle(new NotificationCompat.InboxStyle()
                        .setBigContentTitle("Don't miss out on updates")
                        .addLine("Refresh your app")
                        .setSummaryText("Latest updates"))
                .setGroup("FFCSeZ")
                .setGroupSummary(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        // Check if the Android Version is greater than Oreo
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(channel_id, "updates",
                    NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        notificationManager.notify((int) System.currentTimeMillis(), builder.build());
        notificationManager.notify(0, groupNotifications.build());
    }
}
