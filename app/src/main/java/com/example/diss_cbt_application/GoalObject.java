package com.example.diss_cbt_application;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

//Room annotation, at compile time it will make al the necessary code to turn this into an SQLite table
//To persist we need getter methods for the fields
@Entity(tableName = "goals_table")
public class GoalObject {

    //Room automatically creates these fields

    @PrimaryKey(autoGenerate = true)//id is auto incremented
    private int id;

    private String title;

    private String description;

    private String date;

    private String time;

    private int markedComplete;


    /*We dont want to pass id as it is auto generated*/
    public GoalObject(String title, String description, String date, String time, int markedComplete) {
        this.title = title;
        this.description = description;
        this.date = date;
        this.time = time;
        this.markedComplete = markedComplete;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public int getMarkedComplete() {
        return markedComplete;
    }



}
