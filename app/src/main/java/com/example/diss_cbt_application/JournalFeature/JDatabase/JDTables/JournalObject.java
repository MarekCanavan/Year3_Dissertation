package com.example.diss_cbt_application.JournalFeature.JDatabase.JDTables;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**Java class that represents the Journal Table. Creates all the necessary code at compile time
 * to create an SQLite database with these fields.
 * The fields in this table are:
 *      id - unique id to reference each journal
 *      journalName - name given by the user for the journal
 *      journalColour - A colour given to the table by the user*/
@Entity(tableName = "journal_table")
public class JournalObject {

    /*Room automatically creates these fields
     *Table member variables*/
    @PrimaryKey(autoGenerate = true)//id is auto incremented
    private Long id;

    private String journalName;

    private int journalColour;

    /**Constructor where we define the values we want set in the table, when a new Object is called elsewhere in the program
     * these fields need to be parsed for it to be valid
     * We dont want to pass id as it is auto generated*/
    public JournalObject(String journalName, int journalColour) {
        this.journalName = journalName;
        this.journalColour = journalColour;
    }

    /*Room uses this to set the id on the object
     * And also when we update a field in the table*/
    public void setId(Long id) {
        this.id = id;
    }

    /*To persist these fields room needs getter methods for all of the fields*/
    public Long getId() {
        return id;
    }

    public String getJournalName() {
        return journalName;
    }

    public int getJournalColour() {
        return journalColour;
    }
}