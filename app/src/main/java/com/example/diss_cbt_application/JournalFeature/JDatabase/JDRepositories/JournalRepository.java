package com.example.diss_cbt_application.JournalFeature.JDatabase.JDRepositories;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Delete;
import androidx.room.Update;

import com.example.diss_cbt_application.JournalFeature.JDatabase.JDDaos.JournalDao;
import com.example.diss_cbt_application.JournalFeature.JDatabase.JDTables.JournalObject;
import com.example.diss_cbt_application.JournalFeature.JDatabase.JDViewModels.JournalViewModel;
import com.example.diss_cbt_application.JournalFeature.JDatabase.JournalDatabase;

import java.util.List;

/**
 * Simple Java Class that provides another abstraction layer between data source and the rest of the app.
 * Clean API to the rest of the application to handle calls to the Journals Database.
 * Best Practice to create a repository per Table/Dao, so this is the repository for the Journal Table.
 * */
public class JournalRepository {

    /*Member Variables*/
    private JournalDao journalDao;
    private LiveData<List<JournalObject>> allJournals;
    Long id;

    /**
     * Constructor assigns the member variables of Dao and LiveData for allJournals
     *
     * @param - application - subclass of context used to create a database instance*/
    public JournalRepository(Application application){
        JournalDatabase database = JournalDatabase.getInstance(application);
        journalDao = database.JournalDao();
        allJournals = journalDao.getAllJournals();
    }
    /*Public methods for all database operations*/

    /**This function utilises the dao instance set in the constructor to insert the journal synchronously
     *
     * @param journal - journal object for insertion
     * @return id - id of the insertion  */
    public Long insertNotAsync(JournalObject journal){
        id = journalDao.insert(journal);
        return id;
    }

    /**Function creates an instance of the local UpdateJournalAsyncTask, passes the Dao instance
     * and the journal object to execute the update.
     *
     * @param journal - journal object to update*/
    public void update(JournalObject journal){
        new UpdateJournalAsyncTask(journalDao).execute(journal);
    }

    /**Function creates an instance of the local DeleteJournalAsyncTask, passes the Dao instance
     * and the journal object to execute the delete.
     *
     * @param journal - journal object to delete*/
    public void delete(JournalObject journal){
        new DeleteJournalAsyncTask(journalDao).execute(journal);
    }

    /** Function returns the list of LiveData Objects set in the repository constructor.
     *
     * @return LiveData<List<JournalObject>> - List of LiveData Objects  */
    public LiveData<List<JournalObject>> getAllJournals() {
        return allJournals;
    }

    /**This function retrieves a JournalObject given an id by utilising the dao object set in the constructor.
     *
     * @param id - id of the JournalObject we want to retrieve
     * @return JournalObject - Object retrieved given the id */
    public JournalObject getEntryWithId(Long id){
        return journalDao.getEntryWithId(id);
    }

    /**
     * Database operations need to be conducted on a background thread
     * This Async Task updates a journal off of the main thread so the main thread is not blocked
     * */
    private static class UpdateJournalAsyncTask extends AsyncTask<JournalObject, Void, Void> {

        private JournalDao journalDao;

        /**Constructor sets the dao instance for background update*/
        private UpdateJournalAsyncTask(JournalDao journalDao){
            this.journalDao = journalDao;
        }

        /**Updates the journal object on a background thread*/
        @Override
        protected Void doInBackground(JournalObject... journalObjects) {
            journalDao.update(journalObjects[0]);
            return null;
        }
    }

    /**
     * Database operations need to be conducted on a background thread
     * This Async Task deletes a journal off of the main thread so the main thread is not blocked
     * */
    private static class DeleteJournalAsyncTask extends AsyncTask<JournalObject, Void, Void> {

        private JournalDao journalDao;

        /**Constructor sets the dao instance for background delete*/
        private DeleteJournalAsyncTask(JournalDao journalDao){
            this.journalDao = journalDao;
        }

        /**Deletes the journal object on a background thread*/
        @Override
        protected Void doInBackground(JournalObject... journalObjects) {
            journalDao.delete(journalObjects[0]);
            return null;
        }
    }

}
