package com.example.diss_cbt_application;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DatabaseHelper extends SQLiteOpenHelper {

    public DatabaseHelper(Context context){
        super(context, "RTDB", null, 1);
    }

    /*Database tables
    *
    * Journals - 2 tables
    *   - 1 is the container: name, colour, id (JournalID)
    *   - 1 is the structure: columnName, columnType
    *   - FK required to identify which journal each entry in the table is for
    *
    *
    * Entries - 2 tables
    *
    *   - 1 is the container: name, journal, entryTime, journalID, mainEntryID
    *   - 1 is the data: columnName, columnType, Data, entryTime*/

    public void onCreate(SQLiteDatabase db){


        /*Table Name/id Number
         * Column Name
         * Type of column - edit text, number, rating  */
        db.execSQL("CREATE TABLE JournalNames (" +
                "_id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                "journalName VARCHAR(128) NOT NULL," +
                "journalColour INTEGER," +
                "archived INTEGER);"
        );

        /*Table Name/id Number
        * Column Name
        * Type of column - edit text, number, rating  */
        db.execSQL("CREATE TABLE JournalStructure (" +
                "_id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                "columnName VARCHAR(128) NOT NULL," +
                "columnType VARCHAR(128) NOT NULL," +
                "tableID INTEGER," +
                "CONSTRAINT fk1 FOREIGN KEY (tableID) REFERENCES JournalNames (_id)" +
                "ON DELETE CASCADE);"
        );


        db.execSQL("CREATE TABLE SEntry (" +
                "_id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                "entryName VARCHAR(128) NOT NULL," +
                "entryDate INTEGER," +
                "entryTime INTEGER," +
                "entryJournalType VARCHAR(128) NOT NULL," +
                "journalColour INTEGER," +
                "tableID INTEGER," +
                "CONSTRAINT fk1 FOREIGN KEY (tableID) REFERENCES JournalNames (tableID)" +
                "ON DELETE CASCADE);"
        );

        /*Entry id
        * Table ID FOREIGN KEY
        * Column Name
        * Type
        * Date */
        db.execSQL("CREATE TABLE SEntryData (" +
                "_id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                "columnName VARCHAR(128) NOT NULL," +
                "columnType VARCHAR(128) NOT NULL," +
                "entryData VARCHAR(128) NOT NULL," +
                "entryID INTEGER," +
                "entryDate INTEGER," +
                "entryTime INTEGER," +
                "CONSTRAINT fk1 FOREIGN KEY (entryID) REFERENCES SEntry (_id)" +
                "ON DELETE CASCADE);"
        );



        /*Goals Database, Fields Required
        * _id
        * gTitle
        * gDescription
        * gDate
        * gTime
        * markedComplete*/
        db.execSQL("CREATE TABLE " + GContract.G_TABLE + " (" +
                GContract._ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                GContract.G_TITLE + " VARCHAR(128) NOT NULL," +
                GContract.G_DESCRIPTION + " VARCHAR(128) NOT NULL," +
                GContract.G_DATE + " VARCHAR(128) NOT NULL," +
                GContract.G_TIME + " VARCHAR(128) NOT NULL," +
                GContract.G_MARKED_COMPLETE + " INTEGER);"
        );


    }

    /*
    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            // Enable foreign key constraints
            db.execSQL("PRAGMA foreign_keys=ON;");
        }
    }*/


    /*On upgrade will be called if the verison number, is incremeneted from the previous version*/
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){

    }
}