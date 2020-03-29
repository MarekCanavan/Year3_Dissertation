package com.example.diss_cbt_application.GoalsFeature.GDatabase;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

/**This class hold the functions that link the Activity/Fragment to the repository
 * insert, update, delete and getAllGoals can be called from the Activity and the corresponding
 * function is called in the repository to handle the request*/
public class GoalViewModel extends AndroidViewModel {

    /*Member Variables
    *   repository - needed to access the function in the repository
    *   allGoals - List needed to return back to the Activity*/
    private static GoalRepository repository;
    private LiveData<List<GoalObject>> allGoals;

    /**
     * Constructor sets the repository and allGoals list
     *
     * @param - application - subclass of context*/
    public GoalViewModel(@NonNull Application application) {
        super(application);
        repository = new GoalRepository(application);
        allGoals = repository.getAllGoals();
    }

    /*Functions that are passed a goal and the appropriate methods are called in the repository
    * with the goal passed to them*/
    public static void insert(GoalObject goal){
        repository.insert(goal);
    }

    public static Long insertNotAsync(GoalObject goal){
        return repository.insertNotAsync(goal);
    }

    public static void update(GoalObject goal){
        repository.update(goal);
    }

    public void delete (GoalObject goal){
        repository.delete(goal);
    }

    public LiveData<List<GoalObject>> getAllGoals(){
        return allGoals;
    }

}
