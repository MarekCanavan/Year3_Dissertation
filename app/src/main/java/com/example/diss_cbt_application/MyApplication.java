package com.example.diss_cbt_application;

import android.app.Application;

import com.facebook.stetho.Stetho;

/**This Class allows me to use the Stetho Library to view the live database on the device the application is running on
 *
 * github source - https://github.com/facebook/stetho
 * chrome://inspect/#devices - this is the link to view the databases*/
public class MyApplication extends Application {

    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
    }
}
