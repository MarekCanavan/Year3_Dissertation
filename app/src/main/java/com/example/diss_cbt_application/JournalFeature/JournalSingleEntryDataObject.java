package com.example.diss_cbt_application.JournalFeature;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

/**indices are for faster searching of the table */
@Entity(tableName = "single_entry_data_table", foreignKeys = {
        @ForeignKey(onDelete = CASCADE, entity = JournalSingleEntryObject.class,
                parentColumns = "_id", childColumns = "fk_id")},
        indices = {
                @Index("fk_id"),
        })
public class JournalSingleEntryDataObject {

    //Room automatically creates these fields

    /*Table member variables*/
    @PrimaryKey(autoGenerate = true)//id is auto incremented
    private int id;

    private String columnName;

    private String columnType;

    private String entryData;

    private String entryDate;

    private String entryTime;

    private int fk_id;

    public JournalSingleEntryDataObject(String columnName, String columnType, String entryData, String entryDate, String entryTime) {
        this.columnName = columnName;
        this.columnType = columnType;
        this.entryData = entryData;
        this.entryDate = entryDate;
        this.entryTime = entryTime;
    }

    /*Room uses this to set the id on the object*/
    public void setId(int id) {
        this.id = id;
    }

    public void setFk_id(int fk_id) {
        this.fk_id = fk_id;
    }



    /*To persist these fields room needs getter methods for all of the fields*/
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
