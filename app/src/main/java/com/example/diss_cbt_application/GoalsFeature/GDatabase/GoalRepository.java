package com.example.diss_cbt_application.GoalsFeature.GDatabase;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

/**
 * Simple Java Class that provides another abstraction layer between data source and the rest of the app
 * Clean API to the rest of the application to handle calls to the Goals Database
 * Best Practice to create a repository per Table/Dao
 * */
public class GoalRepository {

    /*Member Variables*/
    private GoalDao goalDao;
    private LiveData<List<GoalObject>> allGoals;

    /**
     * Constructor assigns the member variables of Dao and LiveData for allGoals
     *
     * @param - application - subclass of context used to create a database instance*/
    public GoalRepository(Application application){
        GoalDatabase database = GoalDatabase.getInstance(application);
        goalDao = database.goalDao();
        allGoals = goalDao.getAllGoals();
    }

    /*Public methods for all database operations*/

    public void insert(GoalObject goal){
        new InsertGoalAsyncTask(goalDao).execute(goal);
    }


    public Long insertNotAsync(GoalObject goal){
        return goalDao.insert(goal);
    }

    public void update(GoalObject goal){
        new UpdateGoalAsyncTask(goalDao).execute(goal);
    }

    public void delete(GoalObject goal){
        new DeleteGoalAsyncTask(goalDao).execute(goal);
    }

    public LiveData<List<GoalObject>> getAllGoals(){
        return allGoals;
    }


    /**
     * Database operations need to be conducted on a background thread
     * This Async Task inserts a goal off of the main thread so the main thread is not blocked
     * */
    private static class InsertGoalAsyncTask extends AsyncTask<GoalObject, Void, Void>{

        private GoalDao goalDao;

        private InsertGoalAsyncTask(GoalDao goalDao){
            this.goalDao = goalDao;
        }

        @Override
        protected Void doInBackground(GoalObject... goalObjects) {
            goalDao.insert(goalObjects[0]);
            return null;
        }
    }

    /**
     * Database operations need to be conducted on a background thread
     * This Async Task updates a goal off of the main thread so the main thread is not blocked
     * */
    private static class UpdateGoalAsyncTask extends AsyncTask<GoalObject, Void, Void>{

        private GoalDao goalDao;

        private UpdateGoalAsyncTask(GoalDao goalDao){
            this.goalDao = goalDao;
        }

        @Override
        protected Void doInBackground(GoalObject... goalObjects) {
            goalDao.update(goalObjects[0]);
            return null;
        }
    }

    /**
     * Database operations need to be conducted on a background thread
     * This Async Task deletes a goal off of the main thread so the main thread is not blocked
     * */
    private static class DeleteGoalAsyncTask extends AsyncTask<GoalObject, Void, Void>{

        private GoalDao goalDao;

        private DeleteGoalAsyncTask(GoalDao goalDao){
            this.goalDao = goalDao;
        }

        @Override
        protected Void doInBackground(GoalObject... goalObjects) {
            goalDao.delete(goalObjects[0]);
            return null;
        }
    }

}
