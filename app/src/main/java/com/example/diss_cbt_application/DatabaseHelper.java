package com.example.diss_cbt_application;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DatabaseHelper extends SQLiteOpenHelper {

    public DatabaseHelper(Context context){
        super(context, "RTDB", null, 1);
    }

    public void onCreate(SQLiteDatabase db){



        /*Table Name/id Number
        * Column Name
        * Type of column - edit text, number, rating  */
        db.execSQL("CREATE TABLE Journals (" +
                "_id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                "columnName VARCHAR(128) NOT NULL," +
                "columnType VARCHAR(128) NOT NULL," +
                "tableID INTEGER);"
        );

        /*Entry id
        * Table ID FOREIGN KEY
        * Column Name
        * Type
        * Date */
        db.execSQL("CREATE TABLE SingleEntry (" +
                "_id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                "entryName VARCHAR(128) NOT NULL," + //Each entry will have a title
                "columnName VARCHAR(128) NOT NULL," +
                "columnType VARCHAR(128) NOT NULL," +
                "entryData VARCHAR(128) NOT NULL," +
                "tableID INTEGER," +
                "CONSTRAINT fk1 FOREIGN KEY (tableID) REFERENCES Journals (tableID)" +
                "ON DELETE CASCADE);"
        );
    }

    /*On upgrade will be called if the verison number, is incremeneted from the previous version*/
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){

    }
}