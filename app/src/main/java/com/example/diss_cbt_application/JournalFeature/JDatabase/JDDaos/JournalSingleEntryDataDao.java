package com.example.diss_cbt_application.JournalFeature.JDatabase.JDDaos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.diss_cbt_application.JournalFeature.JDatabase.JDTables.JournalSingleEntryDataObject;
import com.example.diss_cbt_application.JournalFeature.JDatabase.JDTables.JournalStructureObject;

import java.util.List;

/**
 * This a simple Interface that defines all the database operations that will be made on the JournalSingleEntryData Database
 * */
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

    @Query("SELECT * FROM single_entry_data_table WHERE fk_eid =:id")
    LiveData<List<JournalSingleEntryDataObject>> getEntriesWithID(Long id);

    @Query("SELECT * FROM single_entry_data_table WHERE fk_tid =:tid AND columnType =:columnType")
    List<JournalSingleEntryDataObject> getEntriesWithTIDType(Long tid, String columnType);
}
