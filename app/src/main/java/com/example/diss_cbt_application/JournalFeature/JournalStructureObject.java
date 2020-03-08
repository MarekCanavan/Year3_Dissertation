package com.example.diss_cbt_application.JournalFeature;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

/**indices are for faster searching of the table */
@Entity(tableName = "journal_structure_table", foreignKeys = {
        @ForeignKey(onDelete = CASCADE, entity = JournalObject.class,
        parentColumns = "_id", childColumns = "fk_id")},
        indices = {
                @Index("fk_id"),
        })
public class JournalStructureObject {

    //Room automatically creates these fields

    /*Table member variables*/
    @PrimaryKey(autoGenerate = true)//id is auto incremented
    private int id;

    private String columnName;

    private String columnType;

    private String fk_id;

    public JournalStructureObject(String columnName, String columnType) {
        this.columnName = columnName;
        this.columnType = columnType;
    }

    /*Room uses this to set the id on the object*/
    public void setId(int id) {
        this.id = id;
    }

    /*To persist these fields room needs getter methods for all of the fields*/
    public int getId() {
        return id;
    }

    public String getColumnName() {
        return columnName;
    }

    public String getColumnType() {
        return columnType;
    }

    public String getFk_id() {
        return fk_id;
    }
}
