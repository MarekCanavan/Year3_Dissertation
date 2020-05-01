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

    /**Function creates an instance of the local InsertJournalSEDAsyncTask, passes the Dao instance
     * and the journalSingleEntryDataObject object to execute the insert.
     *
     * @param journalSingleEntryDataObject - journalSingleEntryDataObject to insert*/
    public void insert(JournalSingleEntryDataObject journalSingleEntryDataObject){
        new InsertJournalSEDAsyncTask(singleEntryDataDao).execute(journalSingleEntryDataObject);
    }

    /**Function creates an instance of the local UpdateSEDAsyncTask, passes the Dao instance
     * and the journalSingleEntryDataObject object to execute the update.
     *
     * @param journalSingleEntryDataObject - journalSingleEntryDataObject to insert*/
    public void update (JournalSingleEntryDataObject journalSingleEntryDataObject){
        new UpdateSEDAsyncTask(singleEntryDataDao).execute(journalSingleEntryDataObject);
    }

    /**Function utilises the dao object set in the constructor to get a LiveData list of all of the
     * JournalSingleEntryDataObjects given an id.
     *
     * @param id - id of the objects we want to retrieve
     * @reutrn LiveData<List<JournalSingleEntryDataObject>> - LiveData list of all of the JournalSingleEntryDataObject with the given id*/
    public LiveData<List<JournalSingleEntryDataObject>> getEntryDataWithId(Long id){
        return singleEntryDataDao.getEntriesWithID(id);
    }

    /**Function utilises the dao object set in the constructor to get a normal list of all of the
     * JournalSingleEntryDataObject given an id.
     *
     * @param id - id of the objects we want to retrieve
     * @reutrn List<JournalSingleEntryDataObject> - normal list of all JournalSingleEntryDataObjects with the given id */
    public List<JournalSingleEntryDataObject> getEntriesWithIDNotLive(Long id){
        return singleEntryDataDao.getEntriesWithIDNotLive(id);
    }

    /**Function utilises the dao object set in the constructor to get a normal list of all of the
     * JournalSingleEntryDataObject given an id and column type.
     *
     * @param id - id of the objects we want to retrieve
     * @param columnType - column type of the entry we want to retrieve
     * @reutrn List<JournalSingleEntryDataObject> - normal list of all JournalSingleEntryDataObjects with the given id and column type*/
    public List<JournalSingleEntryDataObject> getEntriesWithTIDType(Long id, String columnType){
        return singleEntryDataDao.getEntriesWithTIDType(id, columnType);
    }

    /**
     * Database operations need to be conducted on a background thread
     * This Async Task inserts single entry data off of the main thread so the main thread is not blocked
     * */
    private static class InsertJournalSEDAsyncTask extends AsyncTask<JournalSingleEntryDataObject, Void, Void>{

        private JournalSingleEntryDataDao journalSingleEntryDataDao;

        /**Constructor sets the dao instance for background insert*/
        public InsertJournalSEDAsyncTask(JournalSingleEntryDataDao journalSingleEntryDataDao){
            this.journalSingleEntryDataDao = journalSingleEntryDataDao;
        }

        /**Inserts the journalSingleEntryData object on a background thread*/
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

        /**Constructor sets the dao instance for background update*/
        public UpdateSEDAsyncTask(JournalSingleEntryDataDao journalSingleEntryDataDao){
            this.journalSingleEntryDataDao = journalSingleEntryDataDao;
        }

        /**Updates the journalSingleEntryData object on a background thread*/
        @Override
        protected Void doInBackground(JournalSingleEntryDataObject... journalSingleEntryDataObjects) {
            journalSingleEntryDataDao.update(journalSingleEntryDataObjects[0]);
            return null;
        }
    }
}
