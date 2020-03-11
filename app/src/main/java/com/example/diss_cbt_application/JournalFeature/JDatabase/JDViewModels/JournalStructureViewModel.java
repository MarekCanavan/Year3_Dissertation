package com.example.diss_cbt_application.JournalFeature.JDatabase.JDViewModels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.diss_cbt_application.JournalFeature.JDatabase.JDRepositories.JournalStructureRepository;
import com.example.diss_cbt_application.JournalFeature.JDatabase.JDTables.JournalStructureObject;

import java.util.List;

public class JournalStructureViewModel extends AndroidViewModel {

    private static JournalStructureRepository repository;

    public JournalStructureViewModel(@NonNull Application application) {
        super(application);
        repository = new JournalStructureRepository(application);
    }

    public static void insert(JournalStructureObject journalStructure){
        Log.d("Diss", "In JSViewModel, printing JournalStructure: " + journalStructure);
        repository.insert(journalStructure);
    }

    public LiveData<List<JournalStructureObject>> getStructureWithID(Long id){
        Log.d("Diss", "In JSViewModel, printing JournalStructure: " + id);
        return repository.getStructureWithID(id);

    }


}
