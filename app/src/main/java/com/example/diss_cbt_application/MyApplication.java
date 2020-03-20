package com.example.diss_cbt_application;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import com.facebook.stetho.Stetho;

/**This Class allows me to use the Stetho Library to view the live database on the device the application is running on
 *
 * github source - https://github.com/facebook/stetho
 * chrome://inspect/#devices - this is the link to view the databases
 *
 * In addition it is key for the Notification functionality and Notification Channels
 * Create Notification Channels when the application is launched to reduce the number of times the code runs
 * */
public class MyApplication extends Application {

    /*Definition of the channel name*/
    public static final String GOAL_CHANNEL = "GOAL_CHANNEL";

    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);

        createNotificationChannels();
    }

    /**TODO: JavaDoc*/
    private void createNotificationChannels(){

        /*Check the build version is above version 26 (Oreo)*/
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){

            /*Create Notification channel and define the settings for it*/
            NotificationChannel goalChannel = new NotificationChannel(
                    GOAL_CHANNEL,
                    "Don't forget to complete your goal",
                    NotificationManager.IMPORTANCE_HIGH //How important is the notification (changes what happens to the users phone
            );
            goalChannel.setDescription("This is a Reminder to complete your goal");

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(goalChannel);
        }

    }

}
