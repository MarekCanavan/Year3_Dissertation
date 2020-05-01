package com.example.diss_cbt_application.GoalsFeature;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.diss_cbt_application.GoalsFeature.GDatabase.GoalObject;
import com.example.diss_cbt_application.GoalsFeature.GDatabase.GoalViewModel;
import com.example.diss_cbt_application.Notifications.AlertReceiver;
import com.example.diss_cbt_application.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static com.example.diss_cbt_application.GoalsFeature.GContract.NEVER;

/**
 * This class represents a New Goal
 * In this class the user can create a new goal and define:
 *      - Goal Title
 *      - Goal Description
 *      - Goal Time and Date for completion
 *      - How often (if ever) they want the Goal to repeat
 *
 * This class also utilises the AlarmManager, which sends a PendingIntent (with id of the goal,
 * so that it is unique to each goal). The Pending Intent is received by the AlertReceiver Class
 * Where a Notification is generated for the Alarm at the time the user defines
 **/
public class GNewEditGoal extends AppCompatActivity {

    /*Declaring integers needed for the Time and Date picker*/
    private int mYear, mMonth, mDay, mHour, mMinute;
    int setRequestCodeTime;

    /*Declaring the EditText Fields so that the data can be retrieved and persisted to the database*/
    private TextInputEditText et_description_of_goal, et_title_of_goal;
    private TextView tv_goal_time_entry, tv_goal_date_entry;

    /*Declaring member variables for the RadioGroup which assigned when/if the goal will repeat*/
    RadioGroup radioGroup;
    RadioButton radioButton;

    /*Thees member variables are used for the Alarm Manager implementation*/
    private int alarmRequestCode, markedComplete;
    private Long gId;
    private String st_update, title, description, date, time, radioString, st_repeat;
    int ps_dayOfMonth, ps_monthOfYear, ps_year, ps_hour, ps_minute; //For the setting of the date and time picker
    String st_dayOfMonth, st_monthOfYear, st_year, st_hour, st_minute; //For the setting of the date and time picker

    /*Shared execution thread is needed for the database persistence (further explanation above function doThingAThenThingB*/
    private Executor sharedSingleThreadExecutor = Executors.newSingleThreadExecutor();

    /**
     * onCreate is run when the Activity is first loaded
     * This function is responsible for populating the EditTexts in the Activity
     * with the appropriate information for the user to see
     *
     * @param - savedInstanceState is given to every android activity. Used if restoration of the activity is required.*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gnew_goal);

        setTextFields();
        unpackingIntent();
        setDateTime();

        TextInputLayout test = new TextInputLayout(this);

    }

    /**Assigns the EditText values in this class to their relevant fields in the activity
     * Also sets the radio group used for the goal repeating*/
    public void setTextFields(){
        /*Assigning the EditTexts to their correct fields in the layout.xml file*/
        et_description_of_goal = findViewById(R.id.et_description_of_goal);
        et_title_of_goal = findViewById(R.id.et_title_of_goal);
        tv_goal_time_entry = findViewById(R.id.tv_goal_time_entry);
        tv_goal_date_entry = findViewById(R.id.tv_goal_date_entry);

        et_title_of_goal.setImeOptions(EditorInfo.IME_ACTION_DONE);
        et_title_of_goal.setRawInputType(InputType.TYPE_CLASS_TEXT);
        et_description_of_goal.setImeOptions(EditorInfo.IME_ACTION_DONE);
        et_description_of_goal.setRawInputType(InputType.TYPE_CLASS_TEXT);

        /*Assigning the RadioGroup member variables*/
        radioGroup = findViewById(R.id.goal_radioGroup);
        radioButton = null;

        /*Initialising variables, as checks are done against them later in the class*/
        st_update = "";
        gId = -1L;
        alarmRequestCode = -1;
    }

    /**Checks if this class is being called to Edit a previously made goal. If this is the case the page would be loaded from the user
     * selecting an item on the recyclerview. The Goal information is packaged in an intent, so this function unpacks
     * this intent and sets the views on the page with the information from the intent*/
    public void unpackingIntent(){

        /*Retrieving the title, description, date and time from the intent
         * Then setting the previously defined EditTexts to these values*/
        Intent intent = getIntent();

        /*Checks the intent has an id and then retrieves the data from the intent.
        * Also checks the state of the repeat string and sets the radio group accordingly.*/
        if(intent.hasExtra(GContract.EXTRA_ID)){
            et_title_of_goal.setText(intent.getStringExtra(GContract.EXTRA_TITLE));
            et_description_of_goal.setText(intent.getStringExtra(GContract.EXTRA_DESCRIPTION));
            tv_goal_date_entry.setText(intent.getStringExtra(GContract.EXTRA_DATE));
            tv_goal_time_entry.setText(intent.getStringExtra(GContract.EXTRA_TIME));
            st_update = intent.getStringExtra(GContract.EXTRA_UPDATE);
            st_repeat = intent.getStringExtra(GContract.EXTRA_REPEAT);
            gId = intent.getLongExtra(GContract.EXTRA_ID, gId);
            alarmRequestCode = intent.getIntExtra(GContract.EXTRA_REQUEST_CODE, alarmRequestCode);

            /*Checks the state of the radio string so the radio group can be properly set*/
            if(st_repeat.equals(NEVER)){
                RadioButton buttonToSet = findViewById(R.id.radio_never);
                buttonToSet.setChecked(true);
            }
            else if(st_repeat.equals(GContract.HOURLY)){
                RadioButton buttonToSet = findViewById(R.id.radio_hourly);
                buttonToSet.setChecked(true);
            }
            else if(st_repeat.equals(GContract.DAILY)){
                RadioButton buttonToSet = findViewById(R.id.radio_daily);
                buttonToSet.setChecked(true);
            }
            else if(st_repeat.equals(GContract.WEEKLY)){
                RadioButton buttonToSet = findViewById(R.id.radio_weekly);
                buttonToSet.setChecked(true);
            }
        }
    }

    /**This function handles when the user changes when they want the goal to repeat on the RadioGroup
     *
     * @param v - the function is parsed the view */
    public void checkButton(View v){
        int radioId = radioGroup.getCheckedRadioButtonId();
        radioButton = findViewById(radioId);
        //Toast.makeText(this, "Selected Radio Group: " + radioButton.getText(), Toast.LENGTH_SHORT).show();
    }

    /**
     * This function is triggered when the user clicks on the "Save" button in this Activity.
     * The values that are to be saved are retrieved from their fields in the text
     * and checks are done on these to ensure the correct information has been set for the goal.
     *
     * @param v - the function is parsed the view */
    public void saveGoalOnClick(View v){

        /*This is a check to see if the user has set a time they want the goal to repeat*/
        if(radioButton == null){
            radioString = NEVER;
        }
        else{
            radioString = radioButton.getText().toString();
        }

        /*Retrieving the values set by the user from the EditTexts*/
        title = et_title_of_goal.getText().toString();
        description = et_description_of_goal.getText().toString();
        date = tv_goal_date_entry.getText().toString();
        time = tv_goal_time_entry.getText().toString();
        markedComplete = 0;


        /*Show text if a title and description aren't set*/
        if(title.trim().isEmpty() || description.trim().isEmpty()){
            Toast.makeText(this, "Please insert title and description", Toast.LENGTH_SHORT ).show();
            return;
        }

        /*Call to the function that handles the saving of the goal in a seperate thread of execution*/
        doThingAThenThingB();

        finish();
    }

    /**This function is very important for the persisting of data to the database.
     * Room will only do database operations on a background thread as to not block the main thread.
     * So we need to insert the goal and retrieve the id from this insertion and then give
     * that id to the AlarmManager, this can only be done one the first execution thread is finished.
     * In addition, we need to give each Alarm Manager a unique ID that related to the goal the Alarm is for the id of the
     * goal is needed.*/
    private void doThingAThenThingB(){

        /*Thread 1: Inserts or Updates the goal, depending on what state the activity is in*/
        sharedSingleThreadExecutor.execute(new Runnable() {
            @Override
            public void run() {

                /*Request code generated against time so that it is random*/
                setRequestCodeTime = (int) (System.currentTimeMillis() /1000);
                Calendar alarmCal = getAlarmCalender();

                GoalObject goal = new GoalObject(title, description, date, time, alarmCal.getTimeInMillis(), radioString , markedComplete, setRequestCodeTime);

                if(st_update.equals(GContract.EXTRA_UPDATE)){

                    /*First wee need to cancel the previous Alarm Manager*/
                    AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                    Intent alarmIntent = new Intent(GNewEditGoal.this, AlertReceiver.class);

                    /*Passing the context, the request code needs to be unique - so pass the id of the goal, the intent and any flags (which is 0)*/
                    PendingIntent sender = PendingIntent.getBroadcast(GNewEditGoal.this, alarmRequestCode, alarmIntent, 0 );

                    alarmManager.cancel(sender);

                    goal.setId(gId);
                    GoalViewModel.update(goal);
                }
                else{
                    /*Insert the goal synchronously so the id of the insert can be retrieved and used in the next thread of execution*/
                    gId = GoalViewModel.insertNotAsync(goal);
                }
            }
        });


        /*Thread 2: Handles setting the AlarmManger now it has the id of the Goal just inserted*/
        sharedSingleThreadExecutor.execute(new Runnable() {
            @Override
            public void run() {

                /*If the date has not been set in the case of updating goal*/
                if(mYear == 0 && mMonth == 0 && mDay == 0){
                    mYear = ps_year;
                    mMonth = ps_monthOfYear;
                    mDay = ps_dayOfMonth;
                }

                /*If the time has not been set in the case of updating goal*/
                if(mHour == 0 && mMinute == 0 ){
                    mHour = ps_hour;
                    mMinute = ps_minute;
                }

                /*Set the initial time the alarm is meant to go off in a single Calender instance
                * which can be sent to the AlertReceiver*/
                Calendar alarmCal = getAlarmCalender();

                /*Packaging an intent with the information needed to trigger the Notification and reset the alarm
                * at the desired interval if necessary */
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                Intent alarmIntent = new Intent(getApplicationContext(), AlertReceiver.class);
                alarmIntent.putExtra(GContract.G_TITLE, et_title_of_goal.getText().toString());
                alarmIntent.putExtra(GContract.G_DESCRIPTION, et_description_of_goal.getText().toString());
                alarmIntent.putExtra(GContract._ID, gId);
                alarmIntent.putExtra(GContract.G_REPEAT, radioString);
                alarmIntent.putExtra(GContract.G_TIME, alarmCal.getTimeInMillis());
                alarmIntent.putExtra(GContract.G_MARKED_COMPLETE, markedComplete);


                /*Passing the context, the request code needs to be unique - so pass the id of the goal, the intent and any flags (which is 0)*/
                PendingIntent sender = PendingIntent.getBroadcast(getApplicationContext(), setRequestCodeTime, alarmIntent, 0 );

                if (alarmCal.before(Calendar.getInstance())) {
                    alarmCal.add(Calendar.DATE, 1);
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, alarmCal.getTimeInMillis() , sender);

                }
            }
        });
    }

    /**Function that returns an instance of alarm calender set to the time of variables in the class
     *
     * @reutrn - instance of the alarm calender, set to the time and date of the variables set in this
     * class.*/
    public Calendar getAlarmCalender(){
        Calendar alarmCal = Calendar.getInstance();

        alarmCal.set(Calendar.YEAR, mYear);
        alarmCal.set(Calendar.MONTH, mMonth);
        alarmCal.set(Calendar.DAY_OF_MONTH, mDay);
        alarmCal.set(Calendar.HOUR_OF_DAY, mHour);
        alarmCal.set(Calendar.MINUTE, mMinute);
        alarmCal.set(Calendar.SECOND, 0);
        return alarmCal;
    }

    /**
     * This function utilises the DatePickerDialog provided by android to give the user a
     * clean and simple way of choosing the date for the goal to be completed by
     * the integers mYear, mMonth and mDay previously defined are set in this function
     * as is the EditText next to the Date Picker which shows the user the date they picked
     *
     * @param v - the function is parsed the view */
    public void chooseDateOnClick(View v){

        // Get Current Date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog mDatePicker;
        mDatePicker = new DatePickerDialog(GNewEditGoal.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                mYear = year;
                mMonth = month;
                mDay = dayOfMonth;
                tv_goal_date_entry.setText(mDay + "-" + (mMonth + 1) + "-" + mYear);
            }
        }, ps_year, ps_monthOfYear, ps_dayOfMonth);
        mDatePicker.setTitle("Set Date");
        mDatePicker.show();
    }

    /**
     * This function utilises the TimePickerDialog provided by android to give the user a
     * clean and simple way of choosing the time for the goal to be completed by
     * the integers mHour, mMinute previously defined are set in this function
     * as is the EditText next to the Date Picker which shows the user the date they picked
     *
     * @param v - the function is parsed the view */
    public void chooseTimeOnClick(View v){

        // Get Current Time
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        /*When the user sets a new Time the member variables are set and the time is presented to the user*/
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(GNewEditGoal.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                mHour = hourOfDay;
                mMinute = minute;
                tv_goal_time_entry.setText(hourOfDay + ":" + minute);
            }
        }, ps_hour, ps_minute, true);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();

    }

    /**Handles the setting of the DatePicker. this will be different if the user is loading the activity when
     * creating a new goal or updating one. If new, the date of today is to be set. If updating a goal the date picker
     * will be set to the date the goal has been set to be complete*/
    public void setDateTime(){

        // Get Current Time
        final Calendar c = Calendar.getInstance();

        /*This checks if gId (GlobalId) has been set
         * The same Activity is used when loaded from a card on the recycler view or just a new goal
         * The time that is parsed to the Time Fragment depends on this */
        if(gId != -1L){

            String st_time = tv_goal_time_entry.getText().toString();
            String[] dateArray = st_time.split(":");

            Log.d("BusGoalCrash", "Valeue of string: " + st_time);
            st_hour = dateArray[0];
            st_minute = dateArray[1];

            ps_hour = Integer.parseInt(st_hour);
            ps_minute = Integer.parseInt(st_minute);

        }
        else{
            ps_hour = c.get(Calendar.HOUR_OF_DAY);
            ps_minute = c.get(Calendar.MINUTE);
        }

        /*This checks if gId (GlobalId) has been set
         * The same Activity is used when loaded from a card on the recycler view or just a new goal
         * The time that is parsed to the Date Fragment depends on this */
        if(gId != -1L){//If globalId has been overwritten, then we have come from the recyclerView
            String st_date = tv_goal_date_entry.getText().toString();
            String[] dateArray = st_date.split("-");
            st_dayOfMonth = dateArray[0];
            st_monthOfYear = dateArray[1];
            st_year = dateArray[2];

            ps_year = Integer.parseInt(st_year);
            ps_monthOfYear = Integer.parseInt(st_monthOfYear);
            ps_dayOfMonth = Integer.parseInt(st_dayOfMonth);

        }
        else{//GlobalId not overwritten
            ps_year = c.get(Calendar.YEAR);
            ps_monthOfYear = c.get(Calendar.MONTH);
            ps_dayOfMonth = c.get(Calendar.DAY_OF_MONTH);
        }
    }
}
