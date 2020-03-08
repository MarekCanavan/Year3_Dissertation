package com.example.diss_cbt_application.JournalFeature.JDatabase;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface JournalSingleEntryDataDao {

    @Insert
    void insert(JournalSingleEntryDataObject goal);

    @Update
    void update(JournalSingleEntryDataObject goal);

    @Delete
    void delete(JournalSingleEntryDataObject delete);

    @Query("SELECT * FROM journal_table")
    LiveData<List<JournalSingleEntryDataObject>> getAllDataEntries();
}
