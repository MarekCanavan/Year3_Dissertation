package com.example.diss_cbt_application.JournalFeature.JDatabase.JDRepositories;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.diss_cbt_application.JournalFeature.JDatabase.JDDaos.JournalDao;
import com.example.diss_cbt_application.JournalFeature.JDatabase.JDDaos.JournalStructureDao;
import com.example.diss_cbt_application.JournalFeature.JDatabase.JDTables.JournalObject;
import com.example.diss_cbt_application.JournalFeature.JDatabase.JDTables.JournalStructureObject;
import com.example.diss_cbt_application.JournalFeature.JDatabase.JournalDatabase;

import java.util.List;

/**
 * Simple Java Class that provides another abstraction layer between data source and the rest of the app
 * Clean API to the rest of the application to handle calls to the Journals Database
 * Best Practice to create a repository per Table/Dao, so this is the repository for the Journal Structure Table
 * */
public class JournalStructureRepository {

    /*Member Variables*/
    private JournalStructureDao journalStructureDao;

    /**
     * Constructor assigns the member variable for the journalStructureDao
     *
     * @param - application - subclass of context used to create a database instance*/
    public JournalStructureRepository(Application application){
        JournalDatabase database = JournalDatabase.getInstance(application);
        journalStructureDao = database.JournalStructureDao();
    }

    /*Public methods for all database operations*/
    public void insert(JournalStructureObject journalStructure){
        new InsertJournalStructureAsyncTask(journalStructureDao).execute(journalStructure);
    }

    public void update(JournalStructureObject journalStructure){
        new UpdateJournalStructureAsyncTask(journalStructureDao).execute(journalStructure);
    }


    public LiveData<List<JournalStructureObject>> getStructureWithID(Long id){
        return journalStructureDao.getStructureWithID(id);
    }

    public List<JournalStructureObject> getStructureWithIDNotLive(Long id){
        return journalStructureDao.getStructureWithIDNotLive(id);
    }

    /**
     * Database operations need to be conducted on a background thread
     * This Async Task inserts a journal structure off of the main thread so the main thread is not blocked
     * */
    private static class InsertJournalStructureAsyncTask extends AsyncTask<JournalStructureObject, Void, Void>{

        private JournalStructureDao journalStructureDao;

        public InsertJournalStructureAsyncTask(JournalStructureDao journalStructureDao) {
            this.journalStructureDao = journalStructureDao;
        }

        @Override
        protected Void doInBackground(JournalStructureObject... journalStructureObjects) {
            journalStructureDao.insert(journalStructureObjects[0]);
            return null;
        }
    }

    private static class UpdateJournalStructureAsyncTask extends AsyncTask<JournalStructureObject, Void, Void>{

        private JournalStructureDao journalStructureDao;

        public UpdateJournalStructureAsyncTask(JournalStructureDao journalStructureDao) {
            this.journalStructureDao = journalStructureDao;
        }

        @Override
        protected Void doInBackground(JournalStructureObject... journalStructureObjects) {
            journalStructureDao.update(journalStructureObjects[0]);
            return null;
        }
    }

}