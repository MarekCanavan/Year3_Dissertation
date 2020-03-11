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

public class JournalRepository {

    private JournalDao journalDao;
    private LiveData<List<JournalObject>> allJournals;
    public final MutableLiveData<Long> dbInsertId = new MutableLiveData<>();
    Long id;

    public JournalRepository(Application application){
        JournalDatabase database = JournalDatabase.getInstance(application);
        journalDao = database.JournalDao();
        allJournals = journalDao.getAllJournals();
    }

    public MutableLiveData<Long> insert(JournalObject journal){
        final MutableLiveData<Long> id = new MutableLiveData<>();
        new InsertJournalAsyncTask(journalDao, id).execute(journal);
        return id;
    }

    public Long insertNotAsync(JournalObject journal){
        id = journalDao.insert(journal);
        return id;
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



    private class InsertJournalAsyncTask extends AsyncTask <JournalObject, Void, Long> {

        private JournalDao journalDao;
        private MutableLiveData<Long> id;

        public InsertJournalAsyncTask(JournalDao journalDao, MutableLiveData<Long> id){
            this.journalDao = journalDao;
            this.id = id;
        }

        @Override
        protected Long doInBackground(JournalObject... journalObjects) {
            long sqId = journalDao.insert(journalObjects[0]);
            return sqId;
        }

        @Override
        protected void onPostExecute(Long sqId){
            id.setValue(sqId);
            Log.d("Diss", "OnPostExecuteAsyng: " + sqId);
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
