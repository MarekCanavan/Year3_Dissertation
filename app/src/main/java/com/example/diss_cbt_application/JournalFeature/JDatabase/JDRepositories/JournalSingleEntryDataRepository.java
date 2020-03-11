package com.example.diss_cbt_application.JournalFeature.JDatabase.JDRepositories;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.diss_cbt_application.JournalFeature.JDatabase.JDDaos.JournalSingleEntryDataDao;
import com.example.diss_cbt_application.JournalFeature.JDatabase.JDTables.JournalSingleEntryDataObject;
import com.example.diss_cbt_application.JournalFeature.JDatabase.JournalDatabase;

import java.util.List;

public class JournalSingleEntryDataRepository {

    private JournalSingleEntryDataDao singleEntryDataDao;

    public JournalSingleEntryDataRepository(Application application){
        JournalDatabase database = JournalDatabase.getInstance(application);
        singleEntryDataDao = database.JournalSingleEntryDataDao();
    }

    public void insert(JournalSingleEntryDataObject journalSingleEntryDataObject){
        new InsertJournalSEDAsyncTask(singleEntryDataDao).execute(journalSingleEntryDataObject);
    }

    /*public LiveData<List<JournalSingleEntryDataObject>> getEntryDataWithId(Long id){
        return singleEntryDataDao.getAllDataEntries(id);

    }*/

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

}
