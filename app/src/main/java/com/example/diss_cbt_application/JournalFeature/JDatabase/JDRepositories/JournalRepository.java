package com.example.diss_cbt_application.JournalFeature.JDatabase.JDRepositories;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.room.Delete;
import androidx.room.Update;

import com.example.diss_cbt_application.JournalFeature.JDatabase.JDDaos.JournalDao;
import com.example.diss_cbt_application.JournalFeature.JDatabase.JDTables.JournalObject;
import com.example.diss_cbt_application.JournalFeature.JDatabase.JournalDatabase;

import java.util.List;

public class JournalRepository {

    private JournalDao journalDao;
    private LiveData<List<JournalObject>> allJournals;

    public JournalRepository(Application application){
        JournalDatabase database = JournalDatabase.getInstance(application);
        journalDao = database.JournalDao();
        allJournals = journalDao.getAllJournals();
    }

    public void insert(JournalObject journal){
        new InsertJournalAsyncTask(journalDao).execute(journal);
    }

    public void update(JournalObject journal){
        new UpdateJournalAsyncTask(journalDao).execute(journal);
    }

    public void delete(JournalObject journal){
        new DeleteJournalAsyncTask(journalDao).execute(journal);
    }

    public LiveData<List<JournalObject>> getAllJournals() {
        return allJournals;
    }

    private static class InsertJournalAsyncTask extends AsyncTask<JournalObject, Void, Void> {

        private JournalDao journalDao;

        private InsertJournalAsyncTask(JournalDao journalDao){
            this.journalDao = journalDao;
        }

        @Override
        protected Void doInBackground(JournalObject... journalObjects) {
            journalDao.insert(journalObjects[0]);
            return null;
        }
    }

    private static class UpdateJournalAsyncTask extends AsyncTask<JournalObject, Void, Void> {

        private JournalDao journalDao;

        private UpdateJournalAsyncTask(JournalDao journalDao){
            this.journalDao = journalDao;
        }

        @Override
        protected Void doInBackground(JournalObject... journalObjects) {
            journalDao.update(journalObjects[0]);
            return null;
        }
    }

    private static class DeleteJournalAsyncTask extends AsyncTask<JournalObject, Void, Void> {

        private JournalDao journalDao;

        private DeleteJournalAsyncTask(JournalDao journalDao){
            this.journalDao = journalDao;
        }

        @Override
        protected Void doInBackground(JournalObject... journalObjects) {
            journalDao.delete(journalObjects[0]);
            return null;
        }
    }

}
