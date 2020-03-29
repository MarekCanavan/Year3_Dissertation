package com.example.diss_cbt_application.GoalsFeature;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;

import com.example.diss_cbt_application.Notifications.AlertReceiver;

import java.util.Calendar;

public class GoalsService extends IntentService {

    int id;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public GoalsService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@NonNull Intent intent) {


        Log.d("Diss", "In Goals Repeating Service");
        id = -1;
        id = intent.getIntExtra("id", id );
        Calendar alarmCal = Calendar.getInstance();

        alarmCal.set(Calendar.DAY_OF_MONTH, +1);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent alarmIntent = new Intent(getApplicationContext(), AlertReceiver.class);

        /*Passing the context, the request code needs to be unique - so pass the id of the goal, the intent and any flags (which is 0)*/
        PendingIntent sender = PendingIntent.getBroadcast(getApplicationContext(), id, alarmIntent, 0 );

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, alarmCal.getTimeInMillis() , sender);
        }
    }
}
