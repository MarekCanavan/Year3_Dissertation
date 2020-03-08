package com.example.diss_cbt_application.JournalFeature.JDatabase.JDDaos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.diss_cbt_application.JournalFeature.JDatabase.JDTables.JournalSingleEntryDataObject;

import java.util.List;

@Dao
public interface JournalSingleEntryDataDao {

    @Insert
    void insert(JournalSingleEntryDataObject goal);

    @Update
    void update(JournalSingleEntryDataObject goal);

    @Delete
    void delete(JournalSingleEntryDataObject delete);

    @Query("SELECT * FROM single_entry_data_table")
    LiveData<List<JournalSingleEntryDataObject>> getAllDataEntries();
}
