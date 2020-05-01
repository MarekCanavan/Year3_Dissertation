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

    /**Insert a journalSingleEntryDataObject
     *
     * @param journalSingleEntryDataObject - object for insertion
     * */
    @Insert
    void insert(JournalSingleEntryDataObject journalSingleEntryDataObject);

    /**Update a journalSingleEntryDataObject
     *
     * @param journalSingleEntryDataObject - object to update
     * */
    @Update
    void update(JournalSingleEntryDataObject journalSingleEntryDataObject);

    /**Delete a journalSingleEntryDataObject
     *
     * @param journalSingleEntryDataObject - object for deletion
     * */
    @Delete
    void delete(JournalSingleEntryDataObject journalSingleEntryDataObject);

    /**Query that retrieves all of the JournalSingleEntryDataObject in the table given an fk_eid (foreign key entry id) as LiveData
     *
     * @return LiveData<List<JournalSingleEntryDataObject>> - List of all JournalSingleEntryDataObjects in table given an fk_eid as LiveData
     * */
    @Query("SELECT * FROM single_entry_data_table WHERE fk_eid =:id")
    LiveData<List<JournalSingleEntryDataObject>> getEntriesWithID(Long id);

    /**Query that retrieves all of the JournalSingleEntryDataObjects in the table given an fk_tid (foreign key table id) as LiveData
     *
     * @return LiveData<List<JournalSingleEntryDataObject>> - List of all JournalSingleEntryDataObjects in table given an fk_tid as LiveData
     * */
    @Query("SELECT * FROM single_entry_data_table WHERE fk_tid =:id")
    List<JournalSingleEntryDataObject> getEntriesWithIDNotLive(Long id);

    /**Query that retrieves all of the JournalSingleEntryDataObjects in the table given an fk_tid (foreign key table id) and a given column type a as a normal list
     *
     * @return List<JournalSingleEntryDataObject> - List of all JournalSingleEntryDataObjects in table given an fk_tid and a given column type as a normal list
     * */
    @Query("SELECT * FROM single_entry_data_table WHERE fk_tid =:tid AND columnType =:columnType")
    List<JournalSingleEntryDataObject> getEntriesWithTIDType(Long tid, String columnType);
}
