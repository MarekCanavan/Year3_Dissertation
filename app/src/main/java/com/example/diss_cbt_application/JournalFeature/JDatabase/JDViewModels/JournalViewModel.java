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

/**This class hold the functions that link the Activity/Fragment to the repository
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
