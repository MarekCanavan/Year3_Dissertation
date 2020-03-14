package com.example.diss_cbt_application.GoalsFeature;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.diss_cbt_application.DatabaseHelper;
import com.example.diss_cbt_application.R;

import java.util.Calendar;

public class GNewEditGoal extends AppCompatActivity {

    private int mYear, mMonth, mDay, mHour, mMinute, mMarkedComplete;
    EditText et_goal_date, et_goal_time, et_description_of_goal, et_title_of_goal;

    public static final String EXTRA_ID = "EXTRA_ID";
    public static final String EXTRA_TITLE = "EXTRA_TITLE";
    public static final String EXTRA_DESCRIPTION = "EXTRA_DESCRIPTION";
    public static final String EXTRA_DATE = "EXTRA_DATE";
    public static final String EXTRA_TIME = "EXTRA_TIME";
    public static final String EXTRA_MC = "EXTRA_MC";

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

        Intent intent = getIntent();

        if(intent.hasExtra(EXTRA_ID)){
            et_title_of_goal.setText(intent.getStringExtra(EXTRA_TITLE));
            et_description_of_goal.setText(intent.getStringExtra(EXTRA_DESCRIPTION));
            et_goal_date.setText(intent.getStringExtra(EXTRA_DATE));
            et_goal_time.setText(intent.getStringExtra(EXTRA_TIME));
        }
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

        String title = et_title_of_goal.getText().toString();
        String description = et_description_of_goal.getText().toString();
        String date = et_goal_date.getText().toString();
        String time = et_goal_time.getText().toString();
        int marketComplete = 0;

        /*Show text if a title and descirption arent set */
        if(title.trim().isEmpty() || description.trim().isEmpty()){
            Toast.makeText(this, "Please insert title and description", Toast.LENGTH_SHORT ).show();
            return;
        }

        Intent data = new Intent();
        data.putExtra(EXTRA_TITLE, title);
        data.putExtra(EXTRA_DESCRIPTION, description);
        data.putExtra(EXTRA_DATE, date);
        data.putExtra(EXTRA_TIME, time);
        data.putExtra(EXTRA_MC, marketComplete);

        int id = getIntent().getIntExtra(EXTRA_ID, -1);
        if(id != -1){
            data.putExtra(EXTRA_ID, id);
        }

        setResult(RESULT_OK, data);

        finish();
    }
}
