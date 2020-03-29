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
 * This a simple Interface that defines all the database operations that will be made on the JournalSingleEntry Database
 * */
@Dao
public interface JournalSingleEntryDao {

    @Insert
    Long insert(JournalSingleEntryObject goal);

    @Update
    void update(JournalSingleEntryObject goal);

    @Delete
    void delete(JournalSingleEntryObject delete);

    @Query("SELECT * FROM single_entry_table ORDER BY dateTime DESC")
    LiveData<List<JournalSingleEntryObject>> getAllEntries();

    @Query(("SELECT * FROM single_entry_table WHERE fk_id =:id ORDER BY dateTime ASC"))
    List<JournalSingleEntryObject> getEntriesWithId(Long id);

    @Query(("SELECT * FROM single_entry_table WHERE id =:id"))
    JournalSingleEntryObject getEntryWithId(Long id);
}
