package com.example.diss_cbt_application.JournalFeature.JDatabase.JDViewModels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.diss_cbt_application.JournalFeature.JDatabase.JDRepositories.JournalRepository;
import com.example.diss_cbt_application.JournalFeature.JDatabase.JDTables.JournalObject;
import com.example.diss_cbt_application.JournalFeature.JDatabase.JDTables.JournalStructureObject;

import java.util.List;

public class JournalViewModel extends AndroidViewModel {

    private static JournalRepository repository;
    private LiveData<List<JournalObject>>  allJournals;

     public JournalViewModel(@NonNull Application application) {
        super(application);
        repository = new JournalRepository(application);
        allJournals = repository.getAllJournals();
    }

    public static Long insertNotAsync(JournalObject journal){
        return repository.insertNotAsync(journal);
    }

    public void update(JournalObject journal){
         repository.update(journal);
    }

    public void delete(JournalObject journal){
         repository.delete(journal);
    }

    public LiveData<List<JournalObject>> getAllJournals(){
         return allJournals;
    }

}
