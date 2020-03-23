package com.example.diss_cbt_application.GoalsFeature;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

/**
 * This a simple Interface that defines all the database operations that will be made on the Goal Database
 * */
@Dao
public interface GoalDao {

    @Insert
    Long insert(GoalObject goal);

    @Update
    void update(GoalObject goal);

    @Delete
    void delete(GoalObject delete);

    @Query("SELECT * FROM goals_table")
    LiveData<List<GoalObject>> getAllGoals();


}
