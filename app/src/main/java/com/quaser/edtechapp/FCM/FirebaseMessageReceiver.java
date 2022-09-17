package com.quaser.edtechapp.FCM;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.quaser.edtechapp.R;
import com.quaser.edtechapp.SplashActivity;

public class FirebaseMessageReceiver extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        Log.i("Notification", "Message received");
        if (message.getData().size() > 0){
            //todo: how to send and receive other payload to launch custom activities with data from notification
            //how those remote changes were made if intern deploys some random changes
            showNotification(message.getData().get("title"),
                    message.getData().get("message"), "AS");
        }

        if (message.getNotification() != null){
            showNotification(
                    message.getNotification().getTitle(),
                    message.getNotification().getBody(), "CS");
        }
    }

    private RemoteViews getCustomDesign(String title,
                                        String message) {
        RemoteViews remoteViews = new RemoteViews(
                getApplicationContext().getPackageName(),
                R.layout.notification);
        remoteViews.setTextViewText(R.id.title, title);
        remoteViews.setTextViewText(R.id.message, message);
        return remoteViews;
    }

    public void showNotification(String title, String message, String img){

        String CHANNEL_ID = "messages-id";
        String CHANNEL_NAME = "messages";
        int FOREGROUND_ID = 1339;

        Log.i("Notification", title);
        Intent intent
                = new Intent(this, SplashActivity.class);
        String channel_id = "general_channel";
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent
                = PendingIntent.getActivity(
                this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder
                = new NotificationCompat
                .Builder(getApplicationContext(),
                channel_id)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setAutoCancel(true)
                .setVibrate(new long[]{1000, 1000, 1000,
                        1000, 1000})
                .setOnlyAlertOnce(true)
                .setContentIntent(pendingIntent);

//        if (Build.VERSION.SDK_INT
//                >= Build.VERSION_CODES.JELLY_BEAN) {
//            builder = builder.setContent(
//                    getCustomDesign(title, message));
//        } else {
//
//            builder.setContentTitle(title)
//                    .setContentText(message + " " + img)
//                    .setSmallIcon(R.mipmap.ic_launcher_round);
//        }
        NotificationManager notificationManager
                = (NotificationManager) getSystemService(
                Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT
                >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel
                    = new NotificationChannel(
                    channel_id, "web_app",
                    NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(
                    notificationChannel);
        }

        notificationManager.notify(0, builder.build());

        Log.i("Notification", "We got one!");

    }
}
