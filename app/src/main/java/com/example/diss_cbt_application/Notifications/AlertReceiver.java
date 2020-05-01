package com.example.diss_cbt_application.Notifications;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.example.diss_cbt_application.GoalsFeature.GContract;
import com.example.diss_cbt_application.GoalsFeature.GDatabase.GoalObject;
import com.example.diss_cbt_application.GoalsFeature.GDatabase.GoalViewModel;
import com.example.diss_cbt_application.R;
import com.example.diss_cbt_application.Utils.MainActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import static com.example.diss_cbt_application.Utils.MyApplication.GOAL_CHANNEL;


/**This class is responsible for handling the Alarm's for the Goals Feature.
 * It checks if the goal is meant to repeat and if so generates a new Alarm for the time after that interval.
 * If this class is being called a Notification needs to be generated, so that is the last thing the class does.
 * This notification is created on the GOAL_CHANNEL defined in the MyApplication Class.*/
public class AlertReceiver extends BroadcastReceiver {

    /*Defining member variables used throughout the AlertReceiver*/
    public NotificationManagerCompat notificationManager;
    String description, title, repeat;
    int requestCodeTimeID, markedComplete;
    Long id;

    /**This method is called when the alarm is fired. It is responsible for unpacking the intent
     * Checking and handling if the alarm repeats and calling the appropriate functions to set alarms and notifications*/
    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d("AlertReciever", "In onReceive");

        notificationManager = NotificationManagerCompat.from(context);//Define notification manager
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
        if(repeat.equals(GContract.HOURLY)){

            long repeatInterval = 60 * 60 * 1000; //Equals one hour
            notificationTime += repeatInterval; //Add this interval onto the current alarm time

            setAlarm(context, id, title, notificationTime, repeat, description);
        }

        else if(repeat.equals(GContract.DAILY)){

            long repeatInterval = 86400 * 1000; //Equals one day
            notificationTime += repeatInterval; //Add this interval onto the current alarm time

            setAlarm(context, id, title, notificationTime, repeat, description);
        }
        else if(repeat.equals(GContract.WEEKLY)){

            long repeatInterval = 86400 * 7 * 1000; //Equals one week
            notificationTime += repeatInterval; //Add this interval onto the current alarm time

            setAlarm(context, id, title, notificationTime, repeat, description);
        }

        /*Create the notification, this must be called every time the alert receiver is called
        * so id not bound up in any form of logic*/
        createNotification(context);

    }

    /**
     * This function is called to set the next Alarm for the goal at the interval defined by the user
     * This method is needed due to the the nature of the feature. A reminder needs to be accurate to the minute
     * Since API 19, setRepeating is no longer exact (it can be inexact up to one full interval, so potentially one full week)
     * So each time a Reminder/Alarm is triggered a new one is made in its place with the correct interval using this function
     *
     * @param - Context - context the goal is coming from, needed to set the intent
     * @param - id - id of the goal, sent in the intent
     * @param - title - title of the goal, sent in the intent
     * @param - notificationTime - the new time you want the goal to be, set in the previous if/else statement
     * @param - repeat - if the alarm is to repeat again, sent in intent
     * @param - description - description of the goal, sent in intent*/
    private void setAlarm(Context context, Long id, String title, long notificationTime, String repeat, String description) {

        Log.d("AlertReciever", "In setAlarm");

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

        /*Check the SDK of the OS calling this, and that the alarmManager is not null, if both pass then reset the alarm*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (alarmManager != null) {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, notificationTime , sender);
            }
        }

        /*This updated Alarm must be reflected in the database*/
        updateGoal(notificationTime);

    }

    /**This function updates the goal that has just been triggered if it is repeating.
     * When the repeated goal is triggered and a new Goal/Alarm is set, these values need to be updated in the database.
     * The new date and time is updated so it is properly presented on the recycler view card.
     * The requestCodeID (created from time generation) is also stored, so that the alarm can be deleted if the goal is deleted.
     *
     * @param - notificationTime - a long integer that reflects the time in Millis the next time is to go off, used for database storage*/
    public void updateGoal(long notificationTime){

        Log.d("AlertReciever", "In update notification ");
        /*Format the date and time from the new notificationTime (in milliseconds)*/
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(notificationTime);

        /*Format the date and time for database storage*/
        String date = dateFormat.format(calendar.getTime());
        String time = timeFormat.format(calendar.getTime());

        Long requestCodeTimeIDLong = (long) requestCodeTimeID;

        GoalObject goal = new GoalObject(title, description, date, time, calendar.getTimeInMillis(), repeat, markedComplete, requestCodeTimeID);//create object and set values
        goal.setId(id);//set id to the id of this goal so it can be updated
        GoalViewModel.update(goal);//update the object in the database
    }


    /**This function generates the notification when the AlarmReceiver is called.
     * Utilises the GOAL_Channel created in the MyApplication Class. Also sets an intent so when the notification is generated
     * the user can interact with it and be sent to the GoalsFragment.
     *
     * @param - Context - context the goal is coming from, needed to set the notification*/
    public void createNotification(Context context){

        Log.d("AlertReciever", "In create notification ");

        /*Set intent so the user can interact with the notification
        * Logic to open the GoalsFragment is handled in MainActivity onCreate*/
        Intent activityIntent = new Intent(context, MainActivity.class);
        activityIntent.putExtra("notificationLaunch", "goalsFragment");
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, activityIntent, 0);


        /*Create notification using Goals Channel*/
        Notification notification =  new NotificationCompat.Builder(context, GOAL_CHANNEL)
                .setSmallIcon(R.drawable.ic_access_time_black_24dp)
                .setContentTitle(title)
                .setContentText(description)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setColor(ContextCompat.getColor(context, R.color.darkBlue))
                .setContentIntent(contentIntent)
                .build();

        int iId = 0;

        /*Notify the notificationManager of this new notification*/
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            iId = Math.toIntExact(id);
        }
        notificationManager.notify(iId, notification);
    }
}
