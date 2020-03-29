package com.example.diss_cbt_application.JournalFeature.JDatabase.JDRepositories;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.diss_cbt_application.JournalFeature.JDatabase.JDDaos.JournalSingleEntryDataDao;
import com.example.diss_cbt_application.JournalFeature.JDatabase.JDTables.JournalSingleEntryDataObject;
import com.example.diss_cbt_application.JournalFeature.JDatabase.JDViewModels.JournalSingleEntryDataViewModel;
import com.example.diss_cbt_application.JournalFeature.JDatabase.JournalDatabase;

import java.util.List;

/**
 * Simple Java Class that provides another abstraction layer between data source and the rest of the app
 * Clean API to the rest of the application to handle calls to the Journals Database
 * Best Practice to create a repository per Table/Dao, so this is the repository for the Journal Single Entry Data Table
 * */
public class JournalSingleEntryDataRepository {

    /*Member Variables*/
    private JournalSingleEntryDataDao singleEntryDataDao;

    /**
     * Constructor assigns the member variable for the singleEntryDataDao
     *
     * @param - application - subclass of context used to create a database instance*/
    public JournalSingleEntryDataRepository(Application application){
        JournalDatabase database = JournalDatabase.getInstance(application);
        singleEntryDataDao = database.JournalSingleEntryDataDao();
    }

    /*Public methods for all database operations*/
    public void insert(JournalSingleEntryDataObject journalSingleEntryDataObject){
        new InsertJournalSEDAsyncTask(singleEntryDataDao).execute(journalSingleEntryDataObject);
    }

    public void update (JournalSingleEntryDataObject journalSingleEntryDataObject){
        new UpdateSEDAsyncTask(singleEntryDataDao).execute(journalSingleEntryDataObject);
    }

    public LiveData<List<JournalSingleEntryDataObject>> getEntryDataWithId(Long id){
        return singleEntryDataDao.getEntriesWithID(id);

    }

    public List<JournalSingleEntryDataObject> getEntriesWithTIDType(Long id, String columnType){
        return singleEntryDataDao.getEntriesWithTIDType(id, columnType);

    }

    /**
     * Database operations need to be conducted on a background thread
     * This Async Task inserts single entry data off of the main thread so the main thread is not blocked
     * */
    private static class InsertJournalSEDAsyncTask extends AsyncTask<JournalSingleEntryDataObject, Void, Void>{

        private JournalSingleEntryDataDao journalSingleEntryDataDao;

        public InsertJournalSEDAsyncTask(JournalSingleEntryDataDao journalSingleEntryDataDao){
            this.journalSingleEntryDataDao = journalSingleEntryDataDao;
        }

        @Override
        protected Void doInBackground(JournalSingleEntryDataObject... journalSingleEntryDataObjects) {
           journalSingleEntryDataDao.insert(journalSingleEntryDataObjects[0]);
            return null;
        }
    }

    /**
     * Database operations need to be conducted on a background thread
     * This Async Task updates single entry data off of the main thread so the main thread is not blocked
     * */
    private static class UpdateSEDAsyncTask extends AsyncTask<JournalSingleEntryDataObject, Void, Void>{

        private JournalSingleEntryDataDao journalSingleEntryDataDao;

        public UpdateSEDAsyncTask(JournalSingleEntryDataDao journalSingleEntryDataDao){
            this.journalSingleEntryDataDao = journalSingleEntryDataDao;
        }

        @Override
        protected Void doInBackground(JournalSingleEntryDataObject... journalSingleEntryDataObjects) {
            journalSingleEntryDataDao.update(journalSingleEntryDataObjects[0]);
            return null;
        }
    }




}
