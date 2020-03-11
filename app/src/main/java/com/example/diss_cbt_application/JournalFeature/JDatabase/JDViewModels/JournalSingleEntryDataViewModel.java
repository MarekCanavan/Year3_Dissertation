package com.example.diss_cbt_application.JournalFeature.JDatabase.JDViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.diss_cbt_application.JournalFeature.JDatabase.JDRepositories.JournalSingleEntryDataRepository;
import com.example.diss_cbt_application.JournalFeature.JDatabase.JDTables.JournalSingleEntryDataObject;
import com.example.diss_cbt_application.JournalFeature.JDatabase.JDTables.JournalSingleEntryObject;

import java.util.List;

public class JournalSingleEntryDataViewModel extends AndroidViewModel {

    private static JournalSingleEntryDataRepository repository;

    public JournalSingleEntryDataViewModel(@NonNull Application application) {
        super(application);
        repository = new JournalSingleEntryDataRepository(application);
    }

    public static void insert(JournalSingleEntryDataObject journalSingleEntryDataObject){
        repository.insert(journalSingleEntryDataObject);
    }

    /*public LiveData<List<JournalSingleEntryDataObject>> getEntryDataWithId(Long id){
        return getEntriesWithItList = repository.getEntryDataWithId(id);
    }*/
}
