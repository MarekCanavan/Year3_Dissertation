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

    /**Function creates an instance of the repository instance set in the constructor
     * calls the insertNotAsync method and passes the journalSingleEntryObject so it can be inserted synchronously
     *
     * @param journalSingleEntryObject - journal object to insert*/
    public static Long InsertNotAsync(JournalSingleEntryObject journalSingleEntryObject){
        return repository.insertNotAsync(journalSingleEntryObject);
    }

    /**Function creates an instance of the repository instance set in the constructor
     * calls the update method and passes the singleEntryObject so it can be updated
     *
     * @param singleEntryObject - journal object to update*/
    public void update (JournalSingleEntryObject singleEntryObject){
        repository.update(singleEntryObject);
    }

    /**Function creates an instance of the repository instance set in the constructor
     * calls the delete method and passes the singleEntryObject so it can be deleted
     *
     * @param singleEntryObject - journal object to delete*/
    public void delete (JournalSingleEntryObject singleEntryObject){
        repository.delete(singleEntryObject);
    }

    /**Function creates an instance of the repository instance set in the constructor
     * calls the getEntryWithId method and passes the id of the object we want to retrieve.
     *
     * @param id - id of the object we want to retrieve
     * @reutrn JournalSingleEntryObject - return the object associated to this id*/
    public JournalSingleEntryObject getEntryWithId(Long id){
        return repository.getEntryWithId(id);
    }

    /**Function creates an instance of the repository instance set in the constructor
     * calls the getEntriesWithId method and passes the id of the object.
     *
     * @param id - id of the objects we want to retrieve
     * @reutrn List<JournalSingleEntryObject> - normal list of all JournalStructure Objects with the given id */
    public List<JournalSingleEntryObject> getEntriesWithId(Long id){
       return repository.getEntriesWithId(id);
    }

    /**Function creates an instance of the repository instance set in the constructor
     * calls the getAllEntries to retrieve all of the entries in the table.
     *
     * @reutrn LiveData<List<JournalSingleEntryObject>> - LiveData list of all of all of the entries in the table*/
    public LiveData<List<JournalSingleEntryObject>> getAllEntries(){
        return allEntries;
    }
}
