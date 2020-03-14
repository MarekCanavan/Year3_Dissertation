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

public class JournalStructureRepository {

    private JournalStructureDao journalStructureDao;

    public JournalStructureRepository(Application application){
        JournalDatabase database = JournalDatabase.getInstance(application);
        journalStructureDao = database.JournalStructureDao();
    }

    public void insert(JournalStructureObject journalStructure){
        new InsertJournalStructureAsyncTask(journalStructureDao).execute(journalStructure);
    }

    public LiveData<List<JournalStructureObject>> getStructureWithID(Long id){
        return journalStructureDao.getStructureWithID(id);
    }


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

}