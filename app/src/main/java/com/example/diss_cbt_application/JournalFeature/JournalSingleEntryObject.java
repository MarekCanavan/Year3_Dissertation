package com.example.diss_cbt_application.JournalFeature;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

/**indices are for faster searching of the table */
@Entity(tableName = "single_entry_table", foreignKeys = {
        @ForeignKey(onDelete = CASCADE, entity = JournalObject.class,
                parentColumns = "_id", childColumns = "fk_id")},
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

    private int fk_id;

    public JournalSingleEntryObject(String entryName, String entryDate, String entryTime, String journalType, int journalColour) {
        this.entryName = entryName;
        this.entryDate = entryDate;
        this.entryTime = entryTime;
        this.journalType = journalType;
        this.journalColour = journalColour;
    }

    /*Room uses this to set the id on the object*/
    public void setId(int id) {
        this.id = id;
    }

    public void setFk_id(int fk_id) {
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

    public int getFk_id() {
        return fk_id;
    }
}
