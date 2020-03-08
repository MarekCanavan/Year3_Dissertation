package com.example.diss_cbt_application.JournalFeature.JDatabase;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface JournalSignleEntryDao {

    @Insert
    void insert(JournalSingleEntryObject goal);

    @Update
    void update(JournalSingleEntryObject goal);

    @Delete
    void delete(JournalSingleEntryObject delete);

    @Query("SELECT * FROM journal_table")
    LiveData<List<JournalSingleEntryObject>> getAllEntries();
}
