package com.example.diss_cbt_application.JournalFeature.JDatabase;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.diss_cbt_application.JournalFeature.JDatabase.JDDaos.JournalDao;
import com.example.diss_cbt_application.JournalFeature.JDatabase.JDDaos.JournalSingleEntryDao;
import com.example.diss_cbt_application.JournalFeature.JDatabase.JDDaos.JournalSingleEntryDataDao;
import com.example.diss_cbt_application.JournalFeature.JDatabase.JDDaos.JournalStructureDao;
import com.example.diss_cbt_application.JournalFeature.JDatabase.JDTables.JournalObject;
import com.example.diss_cbt_application.JournalFeature.JDatabase.JDTables.JournalSingleEntryDataObject;
import com.example.diss_cbt_application.JournalFeature.JDatabase.JDTables.JournalSingleEntryObject;
import com.example.diss_cbt_application.JournalFeature.JDatabase.JDTables.JournalStructureObject;

@Database(entities = {JournalObject.class, JournalStructureObject.class,
        JournalSingleEntryObject.class, JournalSingleEntryDataObject.class},
        version = 1, exportSchema = false)
public abstract class JournalDatabase extends RoomDatabase {

    /*We need this variable as we need to turn this class into a singleton
    *We can't create multiple instances of the database, we always use the same instance
    * of the database everywhere, which we access with the static variable*/
    private static JournalDatabase instance;

    /*We use these methods to access our Dao's
    * We don't need to create a body because room takes care of the code*/
    public abstract JournalDao JournalDao();
    public abstract JournalStructureDao JournalStructureDao();
    public abstract JournalSingleEntryDao JournalSingleEntryDao();
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
                    .addCallback(roomCallback)
                    .build();

        }
        return instance;
    }


    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateJournalAsyncTask(instance).execute();
        }
    };

    private static class PopulateJournalAsyncTask extends AsyncTask<Void, Void, Void>{

        private JournalDao journalDao;

        private PopulateJournalAsyncTask(JournalDatabase db){
            journalDao = db.JournalDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            journalDao.insert(new JournalObject("Marek's Journal", -16777216, 0));
            journalDao.insert(new JournalObject("Marek's Journal 2", -16777216, 0));
            journalDao.insert(new JournalObject("Marek's Journal 3", -16777216, 0));
            return null;
        }
    }
}
