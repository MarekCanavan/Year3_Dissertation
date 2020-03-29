package com.example.diss_cbt_application.JournalFeature.JDatabase.JDDaos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.diss_cbt_application.JournalFeature.JDatabase.JDTables.JournalObject;

import java.util.List;

/**
 * This a simple Interface that defines all the database operations that will be made on the Journal Database
 * */
@Dao
public interface JournalDao {

    @Insert
    Long insert(JournalObject goal);

    @Update
    void update(JournalObject goal);

    @Delete
    void delete(JournalObject delete);

    @Query("SELECT * FROM journal_table")
    LiveData<List<JournalObject>> getAllJournals();


}
