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
 * This a simple Interface that defines all the database operations that will be made on the JournalObject Table
 * */
@Dao
public interface JournalDao {


    /**Insert a journalObject
     *
     * @param journalObject - object for insertion
     * */
    @Insert
    Long insert(JournalObject journalObject);

    /**Update a journalObject
     *
     * @param journalObject - object to update
     * */
    @Update
    void update(JournalObject journalObject);

    /**Delete a journalObject
     *
     * @param journalObject - object for deletion
     * */
    @Delete
    void delete(JournalObject journalObject);

    /**Query that gets all of the journals in the table
     *
     * @return LiveData<List<JournalObject>> - List of all journals in table
     * */
    @Query("SELECT * FROM journal_table")
    LiveData<List<JournalObject>> getAllJournals();

    /**Query that gets a journal object given an id
     *
     * @return JournalObject - Journal Object with given id
     * */
    @Query(("SELECT * FROM journal_table WHERE id =:id"))
    JournalObject getEntryWithId(Long id);

}
