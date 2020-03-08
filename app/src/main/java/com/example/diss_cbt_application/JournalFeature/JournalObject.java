package com.example.diss_cbt_application.JournalFeature;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**Java class that represents the Journal Table. Creates all the necessary code at compile time
 * to create an SQLite database with these fields.
 * The fields in this table are:
 *      id - unique id to reference each journal
 *      journalName - name given by the user for the journal
 *      journalColour - A colour given to the table by the user
 *      archived - handles if the journal has been archived by the user or not
 *      the user can choose if they want a journal to be taken away from their vie wby archived*/
@Entity(tableName = "journal_table")
public class JournalObject {

    //Room automatically creates these fields

    /*Table member variables*/
    @PrimaryKey(autoGenerate = true)//id is auto incremented
    private int id;

    private String journalName;

    private int journalColour;

    private int archived;

    public JournalObject(String journalName, int journalColour, int archived) {
        this.journalName = journalName;
        this.journalColour = journalColour;
        this.archived = archived;
    }

    /*Room uses this to set the id on the object*/
    public void setId(int id) {
        this.id = id;
    }

    /*To persist these fields room needs getter methods for all of the fields*/
    public int getId() {
        return id;
    }

    public String getJournalName() {
        return journalName;
    }

    public int getJournalColour() {
        return journalColour;
    }

    public int getArchived() {
        return archived;
    }
}