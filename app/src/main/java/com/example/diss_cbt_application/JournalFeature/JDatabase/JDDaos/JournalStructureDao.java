package com.example.diss_cbt_application.JournalFeature.JDatabase.JDDaos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.diss_cbt_application.JournalFeature.JDatabase.JDTables.JournalObject;
import com.example.diss_cbt_application.JournalFeature.JDatabase.JDTables.JournalStructureObject;

import java.util.List;

/**
 * This a simple Interface that defines all the database operations that will be made on the JournalStructure Table
 * */
@Dao
public interface JournalStructureDao {

    /**Insert a journalStructureObject
     *
     * @param journalStructureObject - object for insertion
     * */
    @Insert
    void insert(JournalStructureObject journalStructureObject);

    /**Update a journalStructureObject
     *
     * @param journalStructureObject - object to update
     * */
    @Update
    void update(JournalStructureObject journalStructureObject);

    /**Delete a journalStructureObject
     *
     * @param journalStructureObject - object for deleting
     * */
    @Delete
    void delete(JournalStructureObject journalStructureObject);

    /**Retrieve all of the journalStructureObjects given an id as LiveData
     *
     * @return LiveData<List<JournalStructureObject>> - all journalStructure Objects given id as LiveData
     * */
    @Query(("SELECT * FROM journal_structure_table WHERE fk_id =:id"))
    LiveData<List<JournalStructureObject>> getStructureWithID(Long id);

    /**Retrieve all of the journalStructureObjects given an id as a normal list
     *
     * @return List<JournalStructureObject>- all journalStructure Objects given id as a normal list
     * */
    @Query(("SELECT * FROM journal_structure_table WHERE fk_id =:id"))
    List<JournalStructureObject> getStructureWithIDNotLive(Long id);

}
