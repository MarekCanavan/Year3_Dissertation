package com.example.diss_cbt_application.JournalFeature.JDatabase.JDDaos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.diss_cbt_application.JournalFeature.JDatabase.JDTables.JournalSingleEntryObject;
import com.example.diss_cbt_application.JournalFeature.JDatabase.JDTables.JournalStructureObject;

import java.util.List;

/**
 * This a simple Interface that defines all the database operations that will be made on the JournalSingleEntry Table
 * */
@Dao
public interface JournalSingleEntryDao {

    /**Insert a journalSingleEntryObject
     *
     * @param journalSingleEntryObject - object for insertion
     * */
    @Insert
    Long insert(JournalSingleEntryObject journalSingleEntryObject);

    /**Update a journalSingleEntryObject
     *
     * @param journalSingleEntryObject - object to update
     * */
    @Update
    void update(JournalSingleEntryObject journalSingleEntryObject);

    /**Delete a journalSingleEntryObject
     *
     * @param journalSingleEntryObject - object for deletion
     * */
    @Delete
    void delete(JournalSingleEntryObject journalSingleEntryObject);

    /**Query that retrieves all of the journalSingleEntryObjects in the table and orders them by dateTime descending as LiveData
     *
     * @return LiveData<List<JournalSingleEntryObject>> - List of all journalSingleEntryObjects in table ordered descending as LiveData
     * */
    @Query("SELECT * FROM single_entry_table ORDER BY dateTime DESC")
    LiveData<List<JournalSingleEntryObject>> getAllEntries();

    /**Query that retrieves all of the journalSingleEntryObjects in the table given an id and orders them by dateTime ascending as a normal list
     *
     * @return List<JournalSingleEntryObject> - List of all journalSingleEntryObjects in table given an id ordered ascending as a normal list
     * */
    @Query(("SELECT * FROM single_entry_table WHERE fk_id =:id ORDER BY dateTime ASC"))
    List<JournalSingleEntryObject> getEntriesWithId(Long id);

    /**Query that retrieves the journalSingleEntryObject in the table with the given id
     *
     * @return JournalSingleEntryObject - journalSingleEntryObject with the given id
     * */
    @Query(("SELECT * FROM single_entry_table WHERE id =:id"))
    JournalSingleEntryObject getEntryWithId(Long id);
}
