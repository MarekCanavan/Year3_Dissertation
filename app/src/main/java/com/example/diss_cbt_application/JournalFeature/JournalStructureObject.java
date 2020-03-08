package com.example.diss_cbt_application.JournalFeature;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

/**Java class that represents the Journal Structure Table. Creates all the necessary code at compile time
 * to create an SQLite database with these fields.
 * The fields in this table are:
 *      columnName - name of the column the user has defined
 *      columnType - the type of data the column accepts e.g. normal, percentage
 *      fk_id - this is a foreign key to the name of the journal and id of the journal this is assigned to
 *
 * indices are for faster searching of the table */
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

    private int fk_id;

    public JournalStructureObject(String columnName, String columnType) {
        this.columnName = columnName;
        this.columnType = columnType;
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

    public String getColumnName() {
        return columnName;
    }

    public String getColumnType() {
        return columnType;
    }

    public int getFk_id() {
        return fk_id;
    }
}
