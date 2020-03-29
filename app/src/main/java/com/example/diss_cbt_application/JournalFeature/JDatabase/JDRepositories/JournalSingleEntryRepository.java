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
    public Long insertNotAsync(JournalSingleEntryObject singleEntryObject){
        id = journalSingleEntryDao.insert(singleEntryObject);
        return id;
    }

    public List<JournalSingleEntryObject> getEntriesWithId(Long id){
        return journalSingleEntryDao.getEntriesWithId(id);
    }

    public void update (JournalSingleEntryObject singleEntryObject){
        new UpdateJournalAsyncTask(journalSingleEntryDao).execute(singleEntryObject);
    }

    private static class UpdateJournalAsyncTask extends AsyncTask<JournalSingleEntryObject, Void, Void> {

        private JournalSingleEntryDao journalSingleEntryDao;

        private UpdateJournalAsyncTask(JournalSingleEntryDao journalSingleEntryDao){
            this.journalSingleEntryDao = journalSingleEntryDao;
        }

        @Override
        protected Void doInBackground(JournalSingleEntryObject... journalSingleEntryObjects) {
            journalSingleEntryDao.update(journalSingleEntryObjects[0]);
            return null;
        }
    }

    public LiveData<List<JournalSingleEntryObject>> getAllEntries(){
        return allEntries;
    }


}
