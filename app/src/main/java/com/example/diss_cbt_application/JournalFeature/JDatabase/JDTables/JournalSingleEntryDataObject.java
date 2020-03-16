package com.example.diss_cbt_application.JournalFeature.JDatabase.JDTables;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

/**Java class that represents the Journal Single Entry Data Table. Creates all the necessary code at compile time
 * to create an SQLite database with these fields.
 * The fields in this table are:
 *      columnName - the name of the column the data is assigned to
 *      columnType - the type of data the column accepts e.g. normal or percentage
 *      entryData - the data the user assigned to the entry
 *      entryDate - date the user made the entry
 *      entryTime - time the user made the entry
 *      fk_id - the foreign assigned to the specific entry created
 *
 * indices are for faster searching of the table */
@Entity(tableName = "single_entry_data_table", foreignKeys = {
        @ForeignKey(onDelete = CASCADE, entity = JournalSingleEntryObject.class,
                parentColumns = "id", childColumns = "fk_id")},
        indices = {
                @Index("fk_id"),
        })
public class JournalSingleEntryDataObject {

    /*Room automatically creates these fields
     *Table member variables*/
    @PrimaryKey(autoGenerate = true)//id is auto incremented
    private Long id;

    private String columnName;

    private String columnType;

    private String entryData;

    private String entryDate;

    private String entryTime;

    private Long fk_id;

    /**Constructor where we define the values we want set in the table, when a new Object is called elsewhere in the program
     * these fields need to be parsed for it to be valid
     * We dont want to pass id as it is auto generated*/
    public JournalSingleEntryDataObject(String columnName, String columnType, String entryData, String entryDate, String entryTime, Long fk_id) {
        this.columnName = columnName;
        this.columnType = columnType;
        this.entryData = entryData;
        this.entryDate = entryDate;
        this.entryTime = entryTime;
        this.fk_id = fk_id;
    }

    /*Room uses this to set the id on the object*/
    public void setId(Long id) {
        this.id = id;
    }

    public void setFk_id(Long fk_id) {
        this.fk_id = fk_id;
    }

    /*To persist these fields room needs getter methods for all of the fields*/

    public Long getId() {
        return id;
    }

    public Long getFk_id() {
        return fk_id;
    }

    public String getColumnName() {
        return columnName;
    }

    public String getColumnType() {
        return columnType;
    }

    public String getEntryData() {
        return entryData;
    }

    public String getEntryDate() {
        return entryDate;
    }

    public String getEntryTime() {
        return entryTime;
    }
}
