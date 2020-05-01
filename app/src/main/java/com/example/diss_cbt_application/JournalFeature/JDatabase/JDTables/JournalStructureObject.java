package com.example.diss_cbt_application.JournalFeature.JDatabase.JDTables;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.example.diss_cbt_application.JournalFeature.JDatabase.JDTables.JournalObject;

import static androidx.room.ForeignKey.CASCADE;

/**Java class that represents the Journal Structure Table. Creates all the necessary code at compile time
 * to create an SQLite database with these fields.
 * The fields in this table are:
 *      columnName - name of the column the user has defined
 *      columnType - the type of data the column accepts e.g. normal, percentage
 *      fk_id - this is a foreign key to the name of the journal and id of the journal this is assigned to
 *
 * indices are for faster searching of the table. Foreign Key relationship with onDelete Cascade is also defined between this table
 * and the JournalObject, so if an entry from JournalObject is deleted its associated structure is also deleted.*/
@Entity(tableName = "journal_structure_table", foreignKeys = {
        @ForeignKey(onDelete = CASCADE, entity = JournalObject.class,
        parentColumns = "id", childColumns = "fk_id")},
        indices = {
                @Index("fk_id"),
        })
public class JournalStructureObject {

    /*Room automatically creates these fields
     *Table member variables*/
    @PrimaryKey(autoGenerate = true)//id is auto incremented
    private Long id;

    private String columnName;

    private String columnType;

    private Long fk_id;

    /**Constructor where we define the values we want set in the table, when a new Object is called elsewhere in the program
     * these fields need to be parsed for it to be valid
     * We dont want to pass id as it is auto generated*/
    public JournalStructureObject(String columnName, String columnType, Long fk_id) {
        this.columnName = columnName;
        this.columnType = columnType;
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

    public String getColumnName() {
        return columnName;
    }

    public String getColumnType() {
        return columnType;
    }

    public Long getFk_id() {
        return fk_id;
    }
}
