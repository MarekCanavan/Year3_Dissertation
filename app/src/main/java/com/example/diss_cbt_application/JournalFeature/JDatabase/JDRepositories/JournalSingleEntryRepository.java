package com.example.diss_cbt_application.JournalFeature.JDatabase.JDRepositories;

import android.app.Application;
import android.database.sqlite.SQLiteConstraintException;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.diss_cbt_application.JournalFeature.JDatabase.JDDaos.JournalSingleEntryDao;
import com.example.diss_cbt_application.JournalFeature.JDatabase.JDTables.JournalSingleEntryObject;
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

    public LiveData<List<JournalSingleEntryObject>> getAllEntries(){
        return allEntries;
    }


}
