package com.example.diss_cbt_application.GoalsFeature.GDatabase;

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

    /**Insert a goal
     *
     * @param goal - goal for insertion
     * */
    @Insert
    Long insert(GoalObject goal);

    /**Update a goal
     *
     * @param goal - goal for updating
     * */
    @Update
    void update(GoalObject goal);

    /**Delete a goal
     *
     * @param goal - goal for deletion
     * */
    @Delete
    void delete(GoalObject goal);

    /**Select all of the goals currently in the database and in ascending order
     *
     * @return   LiveData<List<GoalObject>>  - list of GoalObjects */
    @Query("SELECT * FROM goals_table ORDER BY dateTime ASC ")
    LiveData<List<GoalObject>> getAllGoals();

}
