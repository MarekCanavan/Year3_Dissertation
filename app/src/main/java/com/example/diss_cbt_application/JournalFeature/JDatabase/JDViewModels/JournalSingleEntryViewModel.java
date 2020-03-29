package com.example.diss_cbt_application.JournalFeature.JDatabase.JDViewModels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.diss_cbt_application.JournalFeature.JDatabase.JDRepositories.JournalSingleEntryRepository;
import com.example.diss_cbt_application.JournalFeature.JDatabase.JDTables.JournalSingleEntryObject;
import com.example.diss_cbt_application.JournalFeature.JDatabase.JDTables.JournalStructureObject;

import java.util.List;

/**This class hold the functions that link the Activity/Fragment to the repository
 * insert and getAllEntries can be called from the Activity and the corresponding
 * function is called in the repository to handle the request*/
public class JournalSingleEntryViewModel extends AndroidViewModel {

    /*Member Variables
     *   repository - needed to access the function in the repository
     *   allEntries - List needed to return back to the Activity*/
    private static JournalSingleEntryRepository repository;
    private LiveData<List<JournalSingleEntryObject>> allEntries;

    /**
     * Constructor sets the repository and allEntries list
     *
     * @param - application - subclass of context*/
    public JournalSingleEntryViewModel(@NonNull Application application) {
        super(application);
        repository = new JournalSingleEntryRepository(application);
        allEntries = repository.getAllEntries();
    }

    /*Functions that are passed a JournalSingleEntryObject and the appropriate methods are called in the repository
     * with the JournalSingleEntryObject passed to them*/
    public static Long InsertNotAsync(JournalSingleEntryObject journalSingleEntryObject){
        return repository.insertNotAsync(journalSingleEntryObject);
    }

    public void update (JournalSingleEntryObject singleEntryObject){
        repository.update(singleEntryObject);
    }

    public List<JournalSingleEntryObject> getEntriesWithId(Long id){
       return repository.getEntriesWithId(id);

    }

    public LiveData<List<JournalSingleEntryObject>> getAllEntries(){
        return allEntries;
    }
}
