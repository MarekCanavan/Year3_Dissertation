package com.example.diss_cbt_application.JournalFeature.JDatabase.JDDaos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.diss_cbt_application.JournalFeature.JDatabase.JDTables.JournalObject;

import java.util.List;

@Dao
public interface JournalDao {

    @Insert
    void insert(JournalObject goal);

    @Update
    void update(JournalObject goal);

    @Delete
    void delete(JournalObject delete);

    @Query("SELECT * FROM journal_table")
    LiveData<List<JournalObject>> getAllJournals();
}
