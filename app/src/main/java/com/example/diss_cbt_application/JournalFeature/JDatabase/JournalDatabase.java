package com.example.diss_cbt_application.JournalFeature.JDatabase;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {JournalObject.class, JournalStructureObject.class,
        JournalSingleEntryObject.class, JournalSingleEntryDataObject.class},
        version = 1)
public abstract class JournalDatabase extends RoomDatabase {

    /*We need this variable as we need to turn this class into a singleton
    *We can't create multiple instances of the database, we always use the same instance
    * of the database everywhere, which we access with the static variable*/
    private static JournalDatabase instance;

    /*We use these methods to access our Dao's
    * We don't need to create a body because room takes care of the code*/
    public abstract JournalDao JournalDao();
    public abstract JournalStructureDao JournalStructureDao();
    public abstract JournalSignleEntryDao JournalSingleEntryDao();
    public abstract JournalSingleEntryDataDao JournalSingleEntryDataDao();

    /*Create Database
    * In here we create our only database instance, we can get a handle to this instance from outside the database class
    * Syncrhonized means only one method at a time can get access to this database*/
    public static synchronized JournalDatabase getInstance(Context context){

        /*We only want to instantiate this database if we don't have an instance*/
        if(instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    JournalDatabase.class, "journal_database")
                    .fallbackToDestructiveMigration()
                    .build();

        }
        return instance;
    }




}
