package com.example.diss_cbt_application;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface GoalDao {

    @Insert
    void insert(GoalObject goal);

    @Update
    void update(GoalObject goal);

    @Delete
    void delete(GoalObject delete);

    @Query("SELECT * FROM goals_table")
    LiveData<List<GoalObject>> getAllGoals();


}
