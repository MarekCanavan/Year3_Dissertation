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


/**This class is responsible for handling the Alarm's for the Goals Feature
 * It checks if the goal is meant to repeat and if so generates a new Alarm for the time after that interval
 * If this class is being called a Notification needs to be generated, so that is the last thing the class does*/
public class AlertReceiver extends BroadcastReceiver {

    /*Defining member variables used throughout the AlertReceiver*/
    public NotificationManagerCompat notificationManager;
    String description, title, repeat;
    int requestCodeTimeID, markedComplete;
    Long id;

    /*This method is called when the alarm is fired*/
    @Override
    public void onReceive(Context context, Intent intent) {

        notificationManager = NotificationManagerCompat.from(context);
        id = 0L;

        /*Unpack the intent that was passed
        * Initially the intent is parsed from the GNewEditGoal activity but everytime after
        * the intent is sent from within this class itself*/
        title = intent.getStringExtra(GContract.G_TITLE);
        description = intent.getStringExtra(GContract.G_DESCRIPTION);
        id = intent.getLongExtra(GContract._ID, id);
        markedComplete = intent.getIntExtra(GContract.G_MARKED_COMPLETE, markedComplete);
        repeat = intent.getStringExtra(GContract.G_REPEAT);
        long notificationTime = intent.getLongExtra(GContract.G_TIME, 0);

        /*Check how often the user has selected the want the interval between goals to be
        * setAlarm is then called depending on this value*/
        if(repeat.equals(GNewEditGoal.HOURLY)){

            long repeatInterval = 60 * 60 * 1000; //Equals one hour
            notificationTime += repeatInterval; //Add this interval onto the current alarm time

            setAlarm(context, id, title, notificationTime, repeat, description);
        }

        else if(repeat.equals(GNewEditGoal.DAILY)){

            long repeatInterval = 86400 * 1000; //Eqauals one day
            notificationTime += repeatInterval; //Add this interval onto the current alarm time

            setAlarm(context, id, title, notificationTime, repeat, description);
        }
        else if(repeat.equals(GNewEditGoal.WEEKLY)){

            long repeatInterval = 86400 * 7 * 1000; //Equals one week
            notificationTime += repeatInterval; //Add this interval onto the current alarm time

            setAlarm(context, id, title, notificationTime, repeat, description);
        }

        createNotification(context);

    }

    /**
     * This function is called to set the next Alarm for the goal at the interval defined by the user
     * This method is needed due to the the nature of the feature, An reminder needs to be accurate to the minute
     * Since API 19, setRepeating is no longer exact (it can be inexact up to one full interval, so potentially one full week)
     * So each time a Reminder/Alarm is triggered a new one is made in its place with the correct interval using this function
     *
     * @param - Context - context the goal is coming from, needed to set the intent
     * @param - notificationTime - the new time you want the goal to be, set in the previous if/else statement*/
    private void setAlarm(Context context, Long id, String title, long notificationTime, String repeat, String description) {

        /*Loading the alarmIntent with the values needed for the next alarm*/
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent alarmIntent = new Intent(context.getApplicationContext(), AlertReceiver.class);
        alarmIntent.putExtra(GContract.G_TITLE, title); //Title of goal
        alarmIntent.putExtra(GContract.G_DESCRIPTION, description); //description of goal
        alarmIntent.putExtra(GContract._ID, id); //id of goal (very important to keep the same)
        alarmIntent.putExtra(GContract.G_REPEAT, repeat); // How often the goal repeats (also very important to keep the same)
        alarmIntent.putExtra(GContract.G_TIME, notificationTime); //Time the notificaiton is to be triggered) - used to calculate the next Alarm

        /*Request code generated against time so that it is random*/
        requestCodeTimeID = (int) (System.currentTimeMillis() /1000);

        /*Passing the context, the request code needs to be unique - so it is generated using the time, the intent and any flags (which is 0)*/
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

    /**This function updates the goal that has just been triggered if it is repeating
     * When the repeated goal is triggered and a new Goal/Alarm is set, these value need to be updated in the database
     * The new date and time is updated so it is properly presented on the recycler view card
     * The requestCodeID (created from time generation) is also stored, so that the alarm can be deleted if the goal is deleted*/
    public void updateGoal(long notificationTime){

        /*Format the date and time from the new notificationTime (in milliseconds)*/
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(notificationTime);

        String date = dateFormat.format(calendar.getTime());
        String time = timeFormat.format(calendar.getTime());

        Long requestCodeTimeIDLong = (long) requestCodeTimeID;

        GoalObject goal = new GoalObject(title, description, date, time, repeat, markedComplete, requestCodeTimeID);
        goal.setId(id);
        GoalViewModel.update(goal);

    }


    /**This function generates the notification when the AlarmReceiver is called
     *
     * @param - Context - context the goal is coming from, needed to set the notification*/
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

    }

}
