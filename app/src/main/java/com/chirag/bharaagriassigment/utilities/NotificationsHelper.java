package com.chirag.bharaagriassigment.utilities;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.chirag.bharaagriassigment.R;
import com.chirag.bharaagriassigment.view.home.MainActivity;

public class NotificationsHelper {

    private static final String CHANNEL_ID = "Movies Channel Id";
    private static final int NOTIFICATION_ID = 123;

    private static NotificationsHelper instance;
    private final Context context;

    private NotificationsHelper(Context context) {
        this.context = context;
    }

    public static NotificationsHelper getInstance(Context context) {
        if (instance == null) {
            instance = new NotificationsHelper(context);
        }
        return instance;
    }

    public void createNotification() {
        createNotificationChannel();


        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        Bitmap icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_vector_movie);

        Notification notification = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_vector_movie)
                .setLargeIcon(icon)
                .setContentTitle("Movies Retrieved")
                .setContentText("The movies information has been retrieved")
                .setStyle(
                        new NotificationCompat.BigPictureStyle()
                                .bigPicture(icon)
                                .bigLargeIcon(null)
                )
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .build();

        NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, notification);
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String name = CHANNEL_ID;
            String description = "Movies retrieved notifications channel";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
