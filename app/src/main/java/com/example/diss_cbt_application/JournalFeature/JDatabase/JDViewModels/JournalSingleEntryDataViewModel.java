package com.example.diss_cbt_application.JournalFeature.JDatabase.JDViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.diss_cbt_application.JournalFeature.JDatabase.JDRepositories.JournalSingleEntryDataRepository;
import com.example.diss_cbt_application.JournalFeature.JDatabase.JDTables.JournalSingleEntryDataObject;
import com.example.diss_cbt_application.JournalFeature.JDatabase.JDTables.JournalSingleEntryObject;

import java.util.List;

/**This class hold the functions that link the Activity/Fragment to the repository
 * insert, update and get the Entry given a certain id  can be called from the Activity and the corresponding
 * function is called in the repository to handle the request*/
public class JournalSingleEntryDataViewModel extends AndroidViewModel {

    /*Member Variables
     *   repository - needed to access the function in the repository*/
    private static JournalSingleEntryDataRepository repository;

    /**
     * Constructor sets the repository
     *
     * @param - application - subclass of context*/
    public JournalSingleEntryDataViewModel(@NonNull Application application) {
        super(application);
        repository = new JournalSingleEntryDataRepository(application);
    }

    /*Functions that are passed a journalSingleEntryDataObject and the appropriate methods are called in the repository
     * with the journalSingleEntryDataObject passed to them*/
    public static void insert(JournalSingleEntryDataObject journalSingleEntryDataObject){
        repository.insert(journalSingleEntryDataObject);
    }

    public static void update(JournalSingleEntryDataObject journalSingleEntryDataObject){
        repository.update(journalSingleEntryDataObject);
    }

    public LiveData<List<JournalSingleEntryDataObject>> getEntryDataWithId(Long id){
        return repository.getEntryDataWithId(id);
    }

    public List<JournalSingleEntryDataObject> getEntriesWithTIDType(Long id, String columType){
        return repository.getEntriesWithTIDType(id, columType);
    }
}
