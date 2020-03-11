package com.example.diss_cbt_application.JournalFeature.JDatabase.JDViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.diss_cbt_application.JournalFeature.JDatabase.JDRepositories.JournalSingleEntryRepository;
import com.example.diss_cbt_application.JournalFeature.JDatabase.JDTables.JournalSingleEntryObject;

import java.util.List;

public class JournalSingleEntryViewModel extends AndroidViewModel {

    private static JournalSingleEntryRepository repository;
    private LiveData<List<JournalSingleEntryObject>> allEntries;

    public JournalSingleEntryViewModel(@NonNull Application application) {
        super(application);
        repository = new JournalSingleEntryRepository(application);
        allEntries = repository.getAllEntries();
    }

    public static Long InsertNotAsync(JournalSingleEntryObject journalSingleEntryObject){
        return repository.insertNotAsync(journalSingleEntryObject);
    }

    public LiveData<List<JournalSingleEntryObject>> getAllEntries(){
        return allEntries;
    }
}
