package com.example.diss_cbt_application.JournalFeature.JDatabase.JDRepositories;

import android.app.Application;
import android.database.sqlite.SQLiteConstraintException;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.diss_cbt_application.JournalFeature.JDatabase.JDDaos.JournalDao;
import com.example.diss_cbt_application.JournalFeature.JDatabase.JDDaos.JournalSingleEntryDao;
import com.example.diss_cbt_application.JournalFeature.JDatabase.JDTables.JournalObject;
import com.example.diss_cbt_application.JournalFeature.JDatabase.JDTables.JournalSingleEntryObject;
import com.example.diss_cbt_application.JournalFeature.JDatabase.JDTables.JournalStructureObject;
import com.example.diss_cbt_application.JournalFeature.JDatabase.JournalDatabase;

import java.util.List;

/**
 * Simple Java Class that provides another abstraction layer between data source and the rest of the app
 * Clean API to the rest of the application to handle calls to the Journals Database
 * Best Practice to create a repository per Table/Dao, so this is the repository for the JournalSingleEntry Table
 * */
public class JournalSingleEntryRepository {

    /*Member Variables*/
    private JournalSingleEntryDao journalSingleEntryDao;
    private LiveData<List<JournalSingleEntryObject>> allEntries;
    Long id;

    /**
     * Constructor assigns the member variable for the singleEntryDataDao and LiveData for allEntries
     *
     * @param - application - subclass of context used to create a database instance*/
    public JournalSingleEntryRepository(Application application){
        JournalDatabase database = JournalDatabase.getInstance(application);
        journalSingleEntryDao = database.JournalSingleEntryDao();
        allEntries = journalSingleEntryDao.getAllEntries();
    }

    /*Public methods for all database operations*/

    /**This function utilises the dao instance set in the constructor to insert the journalSingleEntryObject synchronously
     *
     * @param singleEntryObject - journalSingleEntryObject object for insertion
     * @return id - id of the insertion  */
    public Long insertNotAsync(JournalSingleEntryObject singleEntryObject){
        id = journalSingleEntryDao.insert(singleEntryObject);
        return id;
    }

    /**Function creates an instance of the local UpdateJournalAsyncTask, passes the Dao instance
     * and the journal object to execute the update.
     *
     * @param singleEntryObject - journal object to update*/
    public void update (JournalSingleEntryObject singleEntryObject){
        new UpdateJournalAsyncTask(journalSingleEntryDao).execute(singleEntryObject);
    }

    /**Function creates an instance of the local DeleteJournalAsyncTask, passes the Dao instance
     * and the journal object to execute the delete.
     *
     * @param singleEntryObject - journal object to delete*/
    public void delete (JournalSingleEntryObject singleEntryObject){
        new DeleteJournalAsyncTask(journalSingleEntryDao).execute(singleEntryObject);
    }

    /**This function retrieves a JournalSingleEntryObject given an id by utilising the dao object set in the constructor.
     *
     * @param id - id of the JournalSingleEntryObject we want to retrieve
     * @return JournalSingleEntryObject - Object retrieved given the id */
    public JournalSingleEntryObject getEntryWithId(Long id){
        return journalSingleEntryDao.getEntryWithId(id);
    }

    /**This function retrieves a normal List of JournalSingleEntryObject given an id by utilising the dao object set in the constructor.
     *
     * @param id - id of the JournalSingleEntryObjects we want to retrieve
     * @return List<JournalSingleEntryObject> - normal list of the JournalSingleEntryObject we retrieved */
    public List<JournalSingleEntryObject> getEntriesWithId(Long id){
        return journalSingleEntryDao.getEntriesWithId(id);
    }

    /**This function retrieves a normal List of JournalSingleEntryObject given an id by utilising the dao object set in the constructor.
     *
     * @return LiveData<List<JournalSingleEntryObject>> - LiveData list of all of the JournalSingleEntryObjects in the table */
    public LiveData<List<JournalSingleEntryObject>> getAllEntries(){
        return allEntries;
    }

    /**
     * Database operations need to be conducted on a background thread
     * This Async Task updates a journalSingleEntry object off of the main thread so the main thread is not blocked.
     * */
    private static class UpdateJournalAsyncTask extends AsyncTask<JournalSingleEntryObject, Void, Void> {

        private JournalSingleEntryDao journalSingleEntryDao;

        /**Constructor sets the dao instance for background update*/
        private UpdateJournalAsyncTask(JournalSingleEntryDao journalSingleEntryDao){
            this.journalSingleEntryDao = journalSingleEntryDao;
        }

        /**Updates the journalSingleEntry object on a background thread*/
        @Override
        protected Void doInBackground(JournalSingleEntryObject... journalSingleEntryObjects) {
            journalSingleEntryDao.update(journalSingleEntryObjects[0]);
            return null;
        }
    }


    /**
     * Database operations need to be conducted on a background thread
     * This Async Task deletes a journalSingleEntry object off of the main thread so the main thread is not blocked.
     * */
    private static class DeleteJournalAsyncTask extends AsyncTask<JournalSingleEntryObject, Void, Void> {

        private JournalSingleEntryDao journalSingleEntryDao;

        /**Constructor sets the dao instance for background delete*/
        private DeleteJournalAsyncTask(JournalSingleEntryDao journalSingleEntryDao){
            this.journalSingleEntryDao = journalSingleEntryDao;
        }


        /**Deletes the journalSingleEntry object on a background thread*/
        @Override
        protected Void doInBackground(JournalSingleEntryObject... journalSingleEntryObjects) {
            journalSingleEntryDao.delete(journalSingleEntryObjects[0]);
            return null;
        }
    }
}
