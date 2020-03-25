package com.example.diss_cbt_application.GoalsFeature;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.diss_cbt_application.Notifications.AlertReceiver;
import com.example.diss_cbt_application.R;

import java.util.Calendar;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * This class represents a New Goal
 * In this class the user can create a new goal and define:
 *      - Goal Title
 *      - Goal Description
 *      - Goal Time and Date for completion
 **/
public class GNewEditGoal extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    /*Declaring integers needed for the Time and Date picker*/
    private int mYear, mMonth, mDay, mHour, mMinute;

    /*Declaring integers for the functionality of the goals*/
    private int mMarkedComplete;

    /*Declaring the EditText Fields so that the data can be retrieved and persisted to the database*/
    private EditText et_goal_date, et_goal_time, et_description_of_goal, et_title_of_goal;

    /*Declaring Strings that can be used to pass values by intent
    * These are needed so data can be passed back to the MainActivity for persistence to the database*/
    public static final String EXTRA_ID = "EXTRA_ID";
    public static final String EXTRA_TITLE = "EXTRA_TITLE";
    public static final String EXTRA_DESCRIPTION = "EXTRA_DESCRIPTION";
    public static final String EXTRA_DATE = "EXTRA_DATE";
    public static final String EXTRA_TIME = "EXTRA_TIME";
    public static final String EXTRA_MC = "EXTRA_MC";
    public static final String EXTRA_UPDATE = "EXTRA_UPDATE";

    /*Declaring member variables for the RadioGroup which assigned when/if the goal will repeat*/
    RadioGroup radioGroup;
    RadioButton radioButton;

    private NotificationManagerCompat notificationManager;

    private int mGoalId, iId, markedComplete;
    private Long gId;
    private String st_update, title, description, date, time;

    /*Shared execution thread is needed for the database persistence (further explanation above function doThingAThenThingB*/
    private Executor sharedSingleThreadExecutor = Executors.newSingleThreadExecutor();


    /*TODO: Better variable names */
    int ps_dayOfMonth, ps_monthOfYear, ps_year, ps_hour, ps_minute;
    String st_dayOfMonth, st_monthOfYear, st_year, st_hour, st_minute;

    /**
     * onCreate is run when the Activity is first loaded
     * This function is responsible for populating the EditTexts in the Activity
     * with the appropriate information for the user to see*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gnew_goal);

        /*Assigning the EditTexts to their correct fields in the layout.xml file*/
        et_goal_date = (EditText) findViewById(R.id.et_goal_date);
        et_goal_time = (EditText) findViewById(R.id.et_goal_time);
        et_description_of_goal = findViewById(R.id.et_description_of_goal);
        et_title_of_goal = findViewById(R.id.et_title_of_goal);

        /*Assigning the RadioGroup member variables*/
        radioGroup = findViewById(R.id.goal_radioGroup);


        /*TODO: COMMENT */
        notificationManager = NotificationManagerCompat.from(this);

        mMarkedComplete = 0;//Initially every goal is marked incomplete


        st_update = "";
        gId = -1L;
        /*Retrieving the title, description, date and time from the intent
        * Then setting the previously defined EditTexts to these values*/
        Intent intent = getIntent();


        /*TODO: COMMENT */
        if(intent.hasExtra(EXTRA_ID)){
            et_title_of_goal.setText(intent.getStringExtra(EXTRA_TITLE));
            et_description_of_goal.setText(intent.getStringExtra(EXTRA_DESCRIPTION));
            et_goal_date.setText(intent.getStringExtra(EXTRA_DATE));
            et_goal_time.setText(intent.getStringExtra(EXTRA_TIME));
            st_update = intent.getStringExtra(EXTRA_UPDATE);
            gId = intent.getLongExtra(EXTRA_ID, gId);
            Log.d("Diss", "Value of gid in if: " + gId);

        }
    }

    /**
     * This function utilises the DatePickerDialog provided by android to give the user a
     * clean and simple way of choosing the date for the goal to be completed by
     * the integers mYear, mMonth and mDay previously defined are set in this function
     * as is the EditText next to the Date Picker which shows the user the date they picked*/
    public void chooseDateOnClick(View v){


        // Get Current Date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        if(gId != -1L){
            String st_date = et_goal_date.getText().toString();
            String[] dateArray = st_date.split("-");
            st_dayOfMonth = dateArray[0];
            st_monthOfYear = dateArray[1];
            st_year = dateArray[2];

            ps_year = Integer.parseInt(st_year);
            ps_monthOfYear = Integer.parseInt(st_monthOfYear);
            ps_dayOfMonth = Integer.parseInt(st_dayOfMonth);

        }
        else{

            ps_year = c.get(Calendar.YEAR);
            ps_monthOfYear = c.get(Calendar.MONTH);
            ps_dayOfMonth = c.get(Calendar.DAY_OF_MONTH);

        }

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                this,
                ps_year,
                ps_monthOfYear,
                ps_dayOfMonth
        );
        datePickerDialog.show();


    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        mYear = year;
                        mMonth = month;
                        mDay = dayOfMonth;
                        et_goal_date.setText(mDay + "-" + (mMonth + 1) + "-" + mYear);
    }

    /**
     * This function utilises the TimePickerDialog provided by android to give the user a
     * clean and simple way of choosing the time for the goal to be completed by
     * the integers mHour, mMinute previously defined are set in this function
     * as is the EditText next to the Date Picker which shows the user the date they picked*/
    public void chooseTimeOnClick(View v){

        // Get Current Time
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        if(gId != -1L){

            String st_time = et_goal_time.getText().toString();
            String[] dateArray = st_time.split(":");
            st_hour = dateArray[0];
            st_minute = dateArray[1];

            ps_hour = Integer.parseInt(st_hour);
            ps_minute = Integer.parseInt(st_minute);

        }
        else{
            ps_hour = c.get(Calendar.HOUR_OF_DAY);
            ps_minute = c.get(Calendar.MINUTE);
        }


        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(GNewEditGoal.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                mHour = hourOfDay;
                mMinute = minute;
                et_goal_time.setText(hourOfDay + ":" + minute);
            }
        }, ps_hour, ps_minute, true);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();

    }


    /*TODO: COMMENT */
    public void checkButton(View v){
        int radioId = radioGroup.getCheckedRadioButtonId();
        
        radioButton = findViewById(radioId);

        Toast.makeText(this, "Selected Radio Group: " + radioButton.getText(), Toast.LENGTH_SHORT).show();
    }

    /**
     * This function is triggered when the user clicks on the "Save" button in this Activity
     * The title, description, date, time and marked complete value are put into an intent
     * They are sent back to the MainActivity so they can be persisted to the database*/
    public void saveGoalOnClick(View v){


        /*Retrieving the values set by the user from the EditTexts*/
        title = et_title_of_goal.getText().toString();
        description = et_description_of_goal.getText().toString();
        date = et_goal_date.getText().toString();
        time = et_goal_time.getText().toString();
        markedComplete = 0;


        /*Show text if a title and description aren't set*/
        if(title.trim().isEmpty() || description.trim().isEmpty()){
            Toast.makeText(this, "Please insert title and description", Toast.LENGTH_SHORT ).show();
            return;
        }


        /*TODO: COMMENT */
        doThingAThenThingB();










        /*Putting the Goal Information retrieved from the EditTexts into the Intent*/
        Intent data = new Intent();
        data.putExtra(EXTRA_TITLE, title);
        data.putExtra(EXTRA_DESCRIPTION, description);
        data.putExtra(EXTRA_DATE, date);
        data.putExtra(EXTRA_TIME, time);
        data.putExtra(EXTRA_MC, markedComplete);
        data.putExtra(EXTRA_UPDATE, gId);

        int id = getIntent().getIntExtra(EXTRA_ID, -1);//Default value as -1 as this will never be a valid id
        if(id != -1){//Only if this is the case, put the id into the intent
            data.putExtra(EXTRA_ID, id);
        }

        /*Pass back the intent with the RESULT_OK identifier, so MainActivity knows the goal has been successfully made*/
        setResult(RESULT_OK, data);

        finish();
    }

    /*TODO: COMMENT */
    private void doThingAThenThingB(){

        /*TODO: COMMENT */
        sharedSingleThreadExecutor.execute(new Runnable() {
            @Override
            public void run() {

                GoalObject goal = new GoalObject(title, description, date, time, markedComplete);

                if(st_update.equals(EXTRA_UPDATE)){
                    goal.setId(gId);
                    GoalViewModel.update(goal);
                    Log.d("Diss", "Value of gID update: " + gId);
                }
                else{
                    gId = GoalViewModel.insertNotAsync(goal);
                    Log.d("Diss", "Value of gID insert: " + gId);
                }


            }
        });


        /*TODO: COMMENT */
        sharedSingleThreadExecutor.execute(new Runnable() {
            @Override
            public void run() {

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    iId = Math.toIntExact(gId);
                }
                Calendar alarmCal = Calendar.getInstance();

                alarmCal.set(Calendar.YEAR, mYear);
                alarmCal.set(Calendar.MONTH, mMonth);
                alarmCal.set(Calendar.DAY_OF_MONTH, mDay);
                alarmCal.set(Calendar.HOUR_OF_DAY, mHour);
                alarmCal.set(Calendar.MINUTE, mMinute);
                alarmCal.set(Calendar.SECOND, 0);


                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                Intent alarmIntent = new Intent(getApplicationContext(), AlertReceiver.class);
                alarmIntent.putExtra("title", et_title_of_goal.getText().toString());
                alarmIntent.putExtra("description", et_description_of_goal.getText().toString());
                alarmIntent.putExtra("id", iId);

                /*Passing the context, the request code needs to be unique - so pass the id of the goal, the intent and any flags (which is 0)*/
                PendingIntent sender = PendingIntent.getBroadcast(getApplicationContext(), iId, alarmIntent, 0 );

                if (alarmCal.before(Calendar.getInstance())) {
                    alarmCal.add(Calendar.DATE, 1);
                }
                /*SET for for but soon we want to do repeating*/
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

                    String radioString = radioButton.getText().toString();

                    if(radioString.equals("Never")){

                        alarmManager.setExact(AlarmManager.RTC_WAKEUP, alarmCal.getTimeInMillis() , sender);
                        Log.d("Diss", "in radio string equals never");

                    }
                    else if(radioString.equals("Daily")){

                        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, alarmCal.getTimeInMillis(), AlarmManager.INTERVAL_DAY, sender);
                        Log.d("Diss", "in radio string equals daily");
                    }
                    else if(radioString.equals("Weekly")){
                        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, alarmCal.getTimeInMillis(), AlarmManager.INTERVAL_DAY * 7, sender);
                        Log.d("Diss", "in radio string equals weekly");
                    }
                }




            }
        });

    }

}
