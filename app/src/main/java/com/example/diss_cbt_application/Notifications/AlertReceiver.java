package com.example.diss_cbt_application.Notifications;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.diss_cbt_application.R;

import static com.example.diss_cbt_application.MyApplication.GOAL_CHANNEL;

public class AlertReceiver extends BroadcastReceiver {

    public NotificationManagerCompat notificationManager;

    /*This method is called when the alarm is fired*/
    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d("Diss", "In Alert Receiver");
        notificationManager = NotificationManagerCompat.from(context);
        int id = 0;

        String title = intent.getStringExtra("title");
        String description = intent.getStringExtra("description");
        id = intent.getIntExtra("id", id);


        Notification notification =  new NotificationCompat.Builder(context, GOAL_CHANNEL)
                .setSmallIcon(R.drawable.ic_attachment)
                .setContentTitle(title)
                .setContentText(description)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .build();

        notificationManager.notify(id, notification);

        Toast.makeText(context, "Alarm Gone off", Toast.LENGTH_LONG).show();


    }
}
