package com.example.diss_cbt_application.Notifications;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**This class is a BroadcastReceiver and listens to the OS for when the phone reboots
 * This is needed to reset the Alarms and Notifications for the goals and daily reminders
 * as wehen the phone shuts down the alarms are deleted. It starts a service in which to reset these alarms.*/
public class BootReceiver extends BroadcastReceiver {

    /**This function is called when the reboot Broadcast is received. Is simply responsible for starting
     * a service that will handle the resetting of the alarms*/
    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d("Diss" ,"IN BOOT RECEIVER");
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            Log.d("Diss" ,"IN BOOT RECEIVER IF");
            /*Start Service with Intent*/
            Intent i = new Intent (context, NotificationService.class);
            context.startService(i);
        }
    }
}
