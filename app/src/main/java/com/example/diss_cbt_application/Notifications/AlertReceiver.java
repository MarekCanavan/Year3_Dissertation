package com.example.diss_cbt_application.Notifications;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.example.diss_cbt_application.R;

import static com.example.diss_cbt_application.MyApplication.GOAL_CHANNEL;

public class AlertReceiver extends BroadcastReceiver {

    /*This method is called when the alarm is fired*/
    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d("Diss", "In Alert Receiver");

        /*
        Notification notification = new NotificationCompat.Builder(this, GOAL_CHANNEL)
                .setSmallIcon(R.drawable.ic_attachment)
                .setContentTitle("Test Goal Title")
                .setContentText("Test Goal Description")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .build();

        notificationManager.notify(1, notification);*/

        Toast.makeText(context, "Alarm Gone off", Toast.LENGTH_LONG).show();


    }
}
