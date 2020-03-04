package com.example.diss_cbt_application;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import java.util.Calendar;

public class GNewGoal extends AppCompatActivity {

    private int mYear, mMonth, mDay, mHour, mMinute, mMarkedComplete;
    EditText et_goal_date, et_goal_time, et_description_of_goal, et_title_of_goal;

    /*Database Definitions for the Activity*/
    private DatabaseHelper dbHelper = null; //reference to db helper for insertion
    private SQLiteDatabase db_write, db_read;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gnew_goal);

        et_goal_date = (EditText) findViewById(R.id.et_goal_date);
        et_goal_time = (EditText) findViewById(R.id.et_goal_time);
        et_description_of_goal = findViewById(R.id.et_description_of_goal);
        et_title_of_goal = findViewById(R.id.et_title_of_goal);

        dbHelper = new DatabaseHelper(this);
        db_write = dbHelper.getWritableDatabase();
        db_read = dbHelper.getReadableDatabase();

        mMarkedComplete = 0;//Initially every goal is marked incomplete

    }

    public void chooseDateOnClick(View v){

        // Get Current Date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        et_goal_date.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();

    }

    /**Function allows the user to choose the time they want their reminder to go off */
    public void chooseTimeOnClick(View v){

        // Get Current Time
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {

                        et_goal_time.setText(hourOfDay + ":" + minute);
                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();

    }

    public void saveGoalOnClick(View v){


        /*Saving data to SEntry table*/
        ContentValues gt_values = new ContentValues();

        gt_values.put(GContract.G_TITLE, et_title_of_goal.getText().toString() );
        gt_values.put(GContract.G_DESCRIPTION, et_description_of_goal.getText().toString() );
        gt_values.put(GContract.G_DATE, et_goal_date.getText().toString());
        gt_values.put(GContract.G_TIME, et_goal_time.getText().toString());
        gt_values.put(GContract.G_MARKED_COMPLETE, mMarkedComplete);

        db_write.insert(GContract.G_TABLE, null, gt_values);


        finish();
    }
}
