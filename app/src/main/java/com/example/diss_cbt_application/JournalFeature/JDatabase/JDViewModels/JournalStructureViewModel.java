package com.example.diss_cbt_application.JournalFeature.JDatabase.JDViewModels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.diss_cbt_application.JournalFeature.JDatabase.JDRepositories.JournalStructureRepository;
import com.example.diss_cbt_application.JournalFeature.JDatabase.JDTables.JournalStructureObject;

import java.util.List;

/**This class hold the functions that link the Activity/Fragment to the repository
 * insert, and get the Journal Structure given a certain id  can be called from the Activity and the corresponding
 * function is called in the repository to handle the request*/
public class JournalStructureViewModel extends AndroidViewModel {

    /*Member Variables
     *   repository - needed to access the function in the repository*/
    private static JournalStructureRepository repository;

    /**
     * Constructor sets the repository
     *
     * @param - application - subclass of context*/
    public JournalStructureViewModel(@NonNull Application application) {
        super(application);
        repository = new JournalStructureRepository(application);
    }

    /*Functions that are passed a JournalStructureObject and the appropriate methods are called in the repository
     * with the JournalStructureObject passed to them*/

    /**Function creates an instance of the repository instance set in the constructor
     * calls the insert method and passes the journalStructure Object so it can be inserted
     *
     * @param journalStructure - journal object to insert*/
    public static void insert(JournalStructureObject journalStructure){
        repository.insert(journalStructure);
    }

    /**Function creates an instance of the repository instance set in the constructor
     * calls the update method and passes the journalStructure Object so it can be updated
     *
     * @param journalStructure - journal object to update*/
    public static void update(JournalStructureObject journalStructure){
        repository.update(journalStructure);
    }

    /**Function creates an instance of the repository instance set in the constructor
     * calls the getStructureWithID method and passes the id of the object.
     *
     * @param id - id of the objects we want to retrieve
     * @reutrn LiveData<List<JournalStructureObject>> - LiveData list of all of the JournalStructureObjects with the given id*/
    public LiveData<List<JournalStructureObject>> getStructureWithID(Long id){
        return repository.getStructureWithID(id);
    }

    /**Function creates an instance of the repository instance set in the constructor
     * calls the getStructureWithIDNotLive method and passes the id of the object.
     *
     * @param id - id of the objects we want to retrieve
     * @reutrn List<JournalStructureObject> - normal list of all JournalStructure Objects with the given id */
    public List<JournalStructureObject> getStructureWithIDNotLive(Long id){
        return repository.getStructureWithIDNotLive(id);
    }


}
