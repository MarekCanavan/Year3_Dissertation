package com.example.diss_cbt_application.JournalFeature.JDatabase.JDDaos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.diss_cbt_application.JournalFeature.JDatabase.JDTables.JournalSingleEntryObject;

import java.util.List;

@Dao
public interface JournalSingleEntryDao {

    @Insert
    Long insert(JournalSingleEntryObject goal);

    @Update
    void update(JournalSingleEntryObject goal);

    @Delete
    void delete(JournalSingleEntryObject delete);

    @Query("SELECT * FROM single_entry_table")
    LiveData<List<JournalSingleEntryObject>> getAllEntries();
}
