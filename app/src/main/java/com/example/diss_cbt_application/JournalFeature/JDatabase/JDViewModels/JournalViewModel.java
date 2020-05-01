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

/**This class holds the functions that link the Activity/Fragment to the repository
 * update, delete and getAllJournals can be called from the Activity and the corresponding
 * function is called in the repository to handle the request*/
public class JournalViewModel extends AndroidViewModel {

    /*Member Variables
     *   repository - needed to access the function in the repository
     *   allJournals - List needed to return back to the Activity*/
    private static JournalRepository repository;
    private LiveData<List<JournalObject>>  allJournals;

    /**
     * Constructor sets the repository and allJournals list
     *
     * @param - application - subclass of context*/
     public JournalViewModel(@NonNull Application application) {
        super(application);
        repository = new JournalRepository(application);
        allJournals = repository.getAllJournals();
    }


    /*Functions that are passed a journal and the appropriate methods are called in the repository
     * with the journal passed to them*/

    /**This function utilises the repository instance set in the constructor to insert the journal synchronously
     * and retrieve the id of this insertion
     *
     * @param journal - journal object for insertion
     * @return id - id of the insertion  */
    public static Long insertNotAsync(JournalObject journal){
        return repository.insertNotAsync(journal);
    }

    /**This function utilises the repository instance set in the constructor to update the journal object
     *
     * @param journal - journal object to update*/
    public void update(JournalObject journal){
        repository.update(journal);
    }

    /**This function utilises the repository instance set in the constructor to delete the journal object
     *
     * @param journal - journal object to delete*/
    public void delete(JournalObject journal){
        repository.delete(journal);
    }

    /**This function retrieves a JournalObject given an id by utilising the repository object set in the constructor.
     *
     * @param id - id of the JournalObject we want to retrieve
     * @return JournalObject - Object retrieved given the id */
    public JournalObject getEntryWithId(Long id){
        return repository.getEntryWithId(id);
    }

    /** Function returns the list of LiveData Objects set in the ViewModel constructor.
     *
     * @return LiveData<List<JournalObject>> - List of LiveData Objects  */
    public LiveData<List<JournalObject>> getAllJournals(){
         return allJournals;
    }
}
