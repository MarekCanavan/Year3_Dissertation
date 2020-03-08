package com.example.diss_cbt_application.GoalsFeature;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {GoalObject.class}, version = 1, exportSchema = false )
public abstract class GoalDatabase extends RoomDatabase {

    private static GoalDatabase instance;

    public abstract GoalDao goalDao();

    public static synchronized GoalDatabase getInstance(Context context){
        if(instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    GoalDatabase.class, "goal_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }


    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void>{

        private GoalDao goalDao;

        private PopulateDbAsyncTask(GoalDatabase db){
            goalDao = db.goalDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            goalDao.insert((new GoalObject("Title1", "dywi", "02-03-2020", "00:03", 0)));
            goalDao.insert((new GoalObject("Title2", "dywi", "02-03-2020", "00:03", 0)));
            goalDao.insert((new GoalObject("Title3", "dywi", "02-03-2020", "00:03", 0)));
            return null;
        }
    }

}
