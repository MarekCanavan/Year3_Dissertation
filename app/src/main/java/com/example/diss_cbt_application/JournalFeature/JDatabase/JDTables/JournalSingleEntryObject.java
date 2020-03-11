package com.example.diss_cbt_application.JournalFeature.JDatabase.JDTables;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.example.diss_cbt_application.JournalFeature.JDatabase.JDTables.JournalObject;

import static androidx.room.ForeignKey.CASCADE;

/**Java class that represents the Journal Single Entry Table. Creates all the necessary code at compile time
 * to create an SQLite database with these fields.
 * The fields in this table are:
 *      entryName - name of the entry the user has assigned
 *      entryDate - date the entry was made
 *      entryTime - time the entry was made
 *      journalType - the type of journal the entry is assigned to
 *      journalColour - colour of the journal this entry is assigned to
 *      fk_id - references the table the entry object is assigned to *
 *
 * indices are for faster searching of the table */
@Entity(tableName = "single_entry_table", foreignKeys = {
        @ForeignKey(onDelete = CASCADE, entity = JournalObject.class,
                parentColumns = "id", childColumns = "fk_id")},
        indices = {
                @Index("fk_id"),
        })
public class JournalSingleEntryObject {

    //Room automatically creates these fields

    /*Table member variables*/
    @PrimaryKey(autoGenerate = true)//id is auto incremented
    private int id;

    private String entryName;

    private String entryDate;

    private String entryTime;

    private String journalType;

    private int journalColour;

    private Long fk_id;

    public JournalSingleEntryObject(String entryName, String entryDate, String entryTime, String journalType, int journalColour, Long fk_id) {
        this.entryName = entryName;
        this.entryDate = entryDate;
        this.entryTime = entryTime;
        this.journalType = journalType;
        this.journalColour = journalColour;
        this.fk_id = fk_id;
    }

    /*Room uses this to set the id on the object*/
    public void setId(int id) {
        this.id = id;
    }

    public void setFk_id(Long fk_id) {
        this.fk_id = fk_id;
    }

    /*To persist these fields room needs getter methods for all of the fields*/
    public int getId() {
        return id;
    }

    public String getEntryName() {
        return entryName;
    }

    public String getEntryDate() {
        return entryDate;
    }

    public String getEntryTime() {
        return entryTime;
    }

    public String getJournalType() {
        return journalType;
    }

    public int getJournalColour() {
        return journalColour;
    }

    public Long getFk_id() {
        return fk_id;
    }
}
