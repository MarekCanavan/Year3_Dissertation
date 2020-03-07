package com.example.diss_cbt_application.GoalsFeature;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class GoalRepository {

    private GoalDao goalDao;
    private LiveData<List<GoalObject>> allGoals;

    public GoalRepository(Application application){
        GoalDatabase database = GoalDatabase.getInstance(application);
        goalDao = database.goalDao();
        allGoals = goalDao.getAllGoals();
    }

    public void insert(GoalObject goal){
        new InsertGoalAsyncTask(goalDao).execute(goal);
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
