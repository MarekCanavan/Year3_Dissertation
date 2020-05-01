package com.example.diss_cbt_application.Notifications;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.example.diss_cbt_application.GoalsFeature.GContract;
import com.example.diss_cbt_application.R;

import static com.example.diss_cbt_application.Utils.MyApplication.GOAL_CHANNEL;

/**This class is responsible for handling the Alarm's for the Daily Reminders to Engage Feature.
 * It checks if the goal is meant to repeat and if so generates a new Alarm for the time after that interval.
 * If this class is being called a Notification needs to be generated, so that is the last thing the class does.
 * This notification is created on the DILT defined in the MyApplication Class.*/
public class DailyNotificationReceiver extends BroadcastReceiver {

    public NotificationManagerCompat notificationManager;

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d("Diss", "Daily Notifcation has been triggered");

        String title = intent.getStringExtra(GContract.G_TITLE);
        String description = intent.getStringExtra(GContract.G_DESCRIPTION);

        notificationManager = NotificationManagerCompat.from(context);

        Notification notification =  new NotificationCompat.Builder(context, GOAL_CHANNEL)
                .setSmallIcon(R.drawable.ic_attachment)
                .setContentTitle(title)
                .setContentText(description)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setColor(ContextCompat.getColor(context, R.color.darkBlue))
                .build();

        int iId = 0;

        notificationManager.notify(iId, notification);

    }
}
