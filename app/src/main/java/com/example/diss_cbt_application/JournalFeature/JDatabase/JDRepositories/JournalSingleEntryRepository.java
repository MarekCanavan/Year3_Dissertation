package com.example.diss_cbt_application.JournalFeature.JDatabase.JDRepositories;

import android.app.Application;
import android.database.sqlite.SQLiteConstraintException;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.diss_cbt_application.JournalFeature.JDatabase.JDDaos.JournalSingleEntryDao;
import com.example.diss_cbt_application.JournalFeature.JDatabase.JDTables.JournalSingleEntryObject;
import com.example.diss_cbt_application.JournalFeature.JDatabase.JournalDatabase;

import java.util.List;

public class JournalSingleEntryRepository {

    private JournalSingleEntryDao journalSingleEntryDao;
    private LiveData<List<JournalSingleEntryObject>> allEntries;
    Long id;

    public JournalSingleEntryRepository(Application application){
        JournalDatabase database = JournalDatabase.getInstance(application);
        journalSingleEntryDao = database.JournalSingleEntryDao();
        allEntries = journalSingleEntryDao.getAllEntries();
    }

    public Long insertNotAsync(JournalSingleEntryObject singleEntryObject){
        id = journalSingleEntryDao.insert(singleEntryObject);
        return id;
    }

    public LiveData<List<JournalSingleEntryObject>> getAllEntries(){
        return allEntries;
    }


}
