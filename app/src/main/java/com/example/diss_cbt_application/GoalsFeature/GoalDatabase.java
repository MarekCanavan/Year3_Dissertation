package com.example.diss_cbt_application.GoalsFeature;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

/**
 * *This class defines the database for the Goals Feature, the entities are the tables it contain:
 *      - GoalObject
 * */
@Database(entities = {GoalObject.class}, version = 1, exportSchema = false )
public abstract class GoalDatabase extends RoomDatabase {

    /*We need this variable as we need to turn this class into a singleton
     *We can't create multiple instances of the database, we always use the same instance
     *of the database everywhere, which we access with the static variable*/
    private static GoalDatabase instance;

    /*We use this method to access our Dao's functions
     *We don't need to create a body because room takes care of the code*/
    public abstract GoalDao goalDao();


    /**Create Database
     * In here we create our only database instance, we can get a handle to this instance from outside the database class
     * Synchronized means only one method at a time can get access to this database
     * */
    public static synchronized GoalDatabase getInstance(Context context){
        if(instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    GoalDatabase.class, "goal_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }

}
