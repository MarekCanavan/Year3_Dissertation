package com.example.diss_cbt_application.GoalsFeature;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**Java class that represents the Goals Table. Room creates all the necessary code at compile time
 * to create an SQLite database with these fields.
 * The fields in this table are:
 *      id - unique id to reference each goal
 *      title - title of the goal
 *      description - description of what the goal entails
 *      date - date the goal must be completed by
 *      time - time on date set the goal must be completed by
 *      marked complete - user can assign if they have successfully completed the goal*/
@Entity(tableName = "goals_table")
public class GoalObject {


    /*Room automatically creates these fields
     *Table member variables*/

    @PrimaryKey(autoGenerate = true)//id is auto incremented
    private Long id;

    private String title;

    private String description;

    private String date;

    private String time;

    private String repeat;

    private int markedComplete;

    private int alarmRequestCode;


     /**Constructor where we define the values we want set in the table, when a new Object is called elsewhere in the program
      * these fields need to be parsed for it to be valid
      * We dont want to pass id as it is auto generated*/
    public GoalObject(String title, String description, String date, String time, String repeat, int markedComplete, int alarmRequestCode) {
        this.title = title;
        this.description = description;
        this.date = date;
        this.time = time;
        this.repeat = repeat;
        this.markedComplete = markedComplete;
        this.alarmRequestCode = alarmRequestCode;
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

    public String getRepeat() {return repeat;}

    public int getMarkedComplete() {
        return markedComplete;
    }

    public int getAlarmRequestCode() {
        return alarmRequestCode;
    }

}
