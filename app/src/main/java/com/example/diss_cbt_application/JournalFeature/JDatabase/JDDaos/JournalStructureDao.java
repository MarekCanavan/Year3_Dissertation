package com.example.diss_cbt_application.JournalFeature.JDatabase.JDDaos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.diss_cbt_application.JournalFeature.JDatabase.JDTables.JournalStructureObject;

import java.util.List;

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
}
