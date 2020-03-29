package com.example.diss_cbt_application.Notifications;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.diss_cbt_application.GoalsFeature.GContract;
import com.example.diss_cbt_application.GoalsFeature.GNewEditGoal;
import com.example.diss_cbt_application.GoalsFeature.GoalObject;
import com.example.diss_cbt_application.GoalsFeature.GoalViewModel;
import com.example.diss_cbt_application.GoalsFeature.GoalsService;
import com.example.diss_cbt_application.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;

import static com.example.diss_cbt_application.MyApplication.GOAL_CHANNEL;

public class AlertReceiver extends BroadcastReceiver {

    public NotificationManagerCompat notificationManager;
    String description, title, repeat;
    int requestCodeTimeID, markedComplete;
    Long id;

    /*This method is called when the alarm is fired*/
    @Override
    public void onReceive(Context context, Intent intent) {

        notificationManager = NotificationManagerCompat.from(context);
        id = 0L;

        title = intent.getStringExtra(GContract.G_TITLE);
        description = intent.getStringExtra(GContract.G_DESCRIPTION);
        id = intent.getLongExtra(GContract._ID, id);
        markedComplete = intent.getIntExtra(GContract.G_MARKED_COMPLETE, markedComplete);
        repeat = intent.getStringExtra(GContract.G_REPEAT);
        long notificationTime = intent.getLongExtra(GContract.G_TIME, 0);

        Log.d("Diss", "Value of notificationTime before if: " + notificationTime );



        if(repeat.equals(GNewEditGoal.DAILY)){

            long repeatInterval = 86400 * 1000;
            notificationTime += repeatInterval;

            setAlarm(context, id, title, notificationTime, repeat, description);


        }
        else if(repeat.equals(GNewEditGoal.WEEKLY)){
            long repeatInterval = 86400 * 7 * 1000;
            notificationTime += repeatInterval;

            setAlarm(context, id, title, notificationTime, repeat, description);


        }

        createNotification(context);


    }

    private void setAlarm(Context context, Long id, String title, long notificationTime, String repeat, String description) {

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent alarmIntent = new Intent(context.getApplicationContext(), AlertReceiver.class);
        alarmIntent.putExtra(GContract.G_TITLE, title);
        alarmIntent.putExtra(GContract.G_DESCRIPTION, description);
        alarmIntent.putExtra(GContract._ID, id);
        alarmIntent.putExtra(GContract.G_REPEAT, repeat);
        alarmIntent.putExtra(GContract.G_TIME, notificationTime);

        /*Request code generated against time so that it is random*/
        requestCodeTimeID = (int) (System.currentTimeMillis() /1000);

        /*Passing the context, the request code needs to be unique - so pass the id of the goal, the intent and any flags (which is 0)*/
        PendingIntent sender = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            sender = PendingIntent.getBroadcast(context.getApplicationContext(), requestCodeTimeID, alarmIntent, 0 );
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (alarmManager != null) {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, notificationTime , sender);
            }
        }

        updateGoal(notificationTime);

    }

    public void updateGoal(long notificationTime){

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(notificationTime);

        String date = dateFormat.format(calendar.getTime());
        String time = timeFormat.format(calendar.getTime());

        Log.d("Diss", "Value of date in updateGoal: " + date);
        Log.d("Diss", "Value of time in updateGoal: " + time);

        Long requestCodeTimeIDLong = (long) requestCodeTimeID;

        GoalObject goal = new GoalObject(title, description, date, time, repeat, markedComplete, requestCodeTimeID);
        goal.setId(id);
        GoalViewModel.update(goal);

    }


    public void createNotification(Context context){

        Notification notification =  new NotificationCompat.Builder(context, GOAL_CHANNEL)
                .setSmallIcon(R.drawable.ic_attachment)
                .setContentTitle(title)
                .setContentText(description)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .build();

        int iId = 0;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            iId = Math.toIntExact(id);
        }
        notificationManager.notify(iId, notification);

        Toast.makeText(context, "Alarm Gone off", Toast.LENGTH_LONG).show();

    }

}
