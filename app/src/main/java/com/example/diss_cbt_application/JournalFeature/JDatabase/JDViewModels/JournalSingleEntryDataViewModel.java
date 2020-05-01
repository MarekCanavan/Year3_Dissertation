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

    /**Function creates an instance of the repository instance set in the constructor
     * calls the insert method and passes the journalSingleEntryDataObject so it can be inserted
     *
     * @param journalSingleEntryDataObject - journal object to update*/
    public static void insert(JournalSingleEntryDataObject journalSingleEntryDataObject){
        repository.insert(journalSingleEntryDataObject);
    }

    /**Function creates an instance of the repository instance set in the constructor
     * calls the update method and passes the journalSingleEntryDataObject so it can be updated
     *
     * @param journalSingleEntryDataObject - journal object to update*/
    public static void update(JournalSingleEntryDataObject journalSingleEntryDataObject){
        repository.update(journalSingleEntryDataObject);
    }

    /**Function creates an instance of the repository instance set in the constructor
     * calls the getEntryDataWithId method and passes the id of the object.
     *
     * @param id - id of the objects we want to retrieve
     * @reutrn LiveData<List<JournalSingleEntryDataObject>> - LiveData list of all JournalSingleEntryDataObject with the given id */
    public LiveData<List<JournalSingleEntryDataObject>> getEntryDataWithId(Long id){
        return repository.getEntryDataWithId(id);
    }

    /**Function creates an instance of the repository instance set in the constructor
     * calls the getEntriesWithIDNotLive method and passes the id of the object.
     *
     * @param id - id of the objects we want to retrieve
     * @reutrn List<JournalSingleEntryObject> - normal list of all JournalSingleEntryDataObjects with the given id */
    public List<JournalSingleEntryDataObject> getEntriesWithIDNotLive(Long id){
        return repository.getEntriesWithIDNotLive(id);
    }

    /**Function creates an instance of the repository instance set in the constructor
     * calls the getEntriesWithTIDType method and passes the id of the object and type of the column.
     *
     * @param id - id of the objects we want to retrieve
     * @param columnType - column type of the entry we want to retrieve
     * @reutrn List<JournalSingleEntryDataObject> - normal list of all JournalSingleEntryDataObjects with the given id and column type*/
    public List<JournalSingleEntryDataObject> getEntriesWithTIDType(Long id, String columnType){
        return repository.getEntriesWithTIDType(id, columnType);
    }
}
