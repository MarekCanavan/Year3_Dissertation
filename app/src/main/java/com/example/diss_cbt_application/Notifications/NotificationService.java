package com.example.diss_cbt_application.Notifications;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.diss_cbt_application.GoalsFeature.GDatabase.GoalObject;
import com.example.diss_cbt_application.GoalsFeature.GDatabase.GoalViewModel;

import java.util.Calendar;
import java.util.List;

public class NotificationService extends Service {

    /*Member variable for the goalViewModel*/
    private GoalViewModel goalViewModel;

    public NotificationService(){

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate(){
        super.onCreate();

        Log.d("Diss", "In Notification Service");

        //Morning Notification
        startAlarm(9, 0);

        //Evening Notification
        startAlarm(17, 0);

        resetGoalAlarms();

    }

    public void resetGoalAlarms(){

        Log.d("Diss", "In reset goals");
        /* Get reference to the Goal View Model - member variable
         * Then observe the changes to the database, specifically the 'getAllGoals' Query*/
        goalViewModel = ViewModelProviders.of((FragmentActivity) getApplicationContext()).get(GoalViewModel.class);
        goalViewModel.getAllGoals().observe((LifecycleOwner) getApplicationContext(), new Observer<List<GoalObject>>() {
            @Override
            public void onChanged(List<GoalObject> goalObjects) {
                for (int i = 0 ; i < goalObjects.size() ; i++){
                    Log.d("ResetNotif", "In reset goals, goal time: " + goalObjects.get(i).getDateTime());
                }
            }
        });

    }

    public int onStartCommand(Intent intent, int flags, int startId){
        return START_NOT_STICKY;
    }

    private void startAlarm(int hour, int minute){


        Log.d("ResetNotif", "In start alarm");

        AlarmManager manager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);


        Intent dailyIntent = new Intent (this, DailyNotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, dailyIntent, 0);

        manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);


    }

}
