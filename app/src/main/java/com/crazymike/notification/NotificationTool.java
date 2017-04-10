package com.crazymike.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;

import com.crazymike.R;

public class NotificationTool {

    public static void show(Context context, int id, String title, String message, Bitmap bitmap, PendingIntent pendingIntent) {

        final NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        final Notification notification;

        if (null != bitmap) {

            final Notification.BigPictureStyle notificationStyle = new Notification.BigPictureStyle();
            notificationStyle.setBigContentTitle(message)
                    .bigPicture(bitmap)
                    .bigLargeIcon(bitmap)
                    .setBigContentTitle(title)
                    .setSummaryText(message);

            notification = new Notification.Builder(context.getApplicationContext())
                    .setSmallIcon(R.mipmap.ic_notification_small)
                    .setLargeIcon(bitmap)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setStyle(notificationStyle)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    .setPriority(Notification.PRIORITY_MAX)
                    .build();

        } else {

            notification = new Notification.Builder(context.getApplicationContext())
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_big_notification))
                    .setSmallIcon(R.mipmap.ic_notification_small)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    .setPriority(Notification.PRIORITY_MAX)
                    .build();
        }

        notificationManager.notify(id, notification);
    }
}
