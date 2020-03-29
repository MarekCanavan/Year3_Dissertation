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
 * This a simple Interface that defines all the database operations that will be made on the JournalStructure Database
 * */
@Dao
public interface JournalStructureDao {

    @Insert
    void insert(JournalStructureObject goal);

    @Update
    void update(JournalStructureObject goal);

    @Delete
    void delete(JournalStructureObject delete);

    @Query("SELECT * FROM journal_structure_table")
    LiveData<List<JournalStructureObject>> getAllJournalStructures();

    @Query(("SELECT * FROM journal_structure_table WHERE fk_id =:id"))
    LiveData<List<JournalStructureObject>> getStructureWithID(Long id);


    @Query(("SELECT * FROM journal_structure_table WHERE fk_id =:id"))
    List<JournalStructureObject> getStructureWithIDNotLive(Long id);


}
