package com.example.diss_cbt_application;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class GoalViewModel extends AndroidViewModel {

    private static GoalRepository repository;
    private LiveData<List<GoalObject>> allGoals;

    public GoalViewModel(@NonNull Application application) {
        super(application);
        repository = new GoalRepository(application);
        allGoals = repository.getAllGoals();
    }

    public static void insert(GoalObject goal){
        repository.insert(goal);
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
