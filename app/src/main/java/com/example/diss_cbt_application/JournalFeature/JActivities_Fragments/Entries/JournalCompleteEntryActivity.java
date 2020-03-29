package com.example.diss_cbt_application.JournalFeature.JActivities_Fragments.Entries;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.diss_cbt_application.JournalFeature.JDatabase.JDTables.JournalSingleEntryDataObject;
import com.example.diss_cbt_application.JournalFeature.JDatabase.JDTables.JournalSingleEntryObject;
import com.example.diss_cbt_application.JournalFeature.JDatabase.JDTables.JournalStructureObject;
import com.example.diss_cbt_application.JournalFeature.JDatabase.JDViewModels.JournalSingleEntryDataViewModel;
import com.example.diss_cbt_application.JournalFeature.JDatabase.JDViewModels.JournalSingleEntryViewModel;
import com.example.diss_cbt_application.JournalFeature.JDatabase.JDViewModels.JournalStructureViewModel;
import com.example.diss_cbt_application.JournalFeature.JActivities_Fragments.General.JournalContract;
import com.example.diss_cbt_application.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**This is the Activity that is loaded when the user selects a Journal to complete an entry for
 * This is a skeleton for this as all of the journal structures are different
 * A Query is sent to the database based on the Journal ID an a structure is retrieved from the database
 * This is then generated in a scrol view so they user can complete an entry
 * When the user is happy with their entry they can click the 'Save' button and their entry will be saved to the database*/
public class JournalCompleteEntryActivity extends AppCompatActivity {

    /*Member Variables*/
    LinearLayout scroll;
    Long journalID, entryID;
    int columnCounter, journalColour;
    private ScrollView fieldReGeneration;
    String journalName, st_entry_name, date, time;

    /*ArrayLists are used to store the data and fields for persistence into the database*/
    List<String> columnTypes = new ArrayList<>();
    List<String> columnNames = new ArrayList<>();
    ArrayList<EditText> allEds = new ArrayList<>();

    /*Member Variables needed to reference the ViewModels to insert the entry into the database*/
    JournalStructureObject journalStructureObject;
    JournalStructureViewModel journalStructureViewModel;
    JournalSingleEntryDataViewModel journalSingleEntryDataViewModel;

    /*Shared execution thread is needed for the database persistence (further explanation above function doThingAThenThingB*/
    private Executor sharedSingleThreadExecutor = Executors.newSingleThreadExecutor();


    int ps_dayOfMonth, ps_monthOfYear, ps_year, ps_hour, ps_minute;
    String st_dayOfMonth, st_monthOfYear, st_year, st_hour, st_minute;
    private EditText et_entry_goal_date, et_entry_goal_time;

    /*Declaring integers needed for the Time and Date picker*/
    private int mYear, mMonth, mDay, mHour, mMinute;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal_complete_entry);

        /*Initialise Values*/
        initialiseScrollView();
        columnCounter = 0;

        /*Assigning the EditTexts to their correct fields in the layout.xml file*/
        et_entry_goal_date = (EditText) findViewById(R.id.et_entry_date);
        et_entry_goal_time = (EditText) findViewById(R.id.et_entry_time);

        /*Retrieve bundle and store the Journal ID, Colour and Name and store in member variables*/
        Bundle journalBundle = getIntent().getExtras();
        journalID = journalBundle.getLong(JournalContract.ID);
        journalColour = journalBundle.getInt(JournalContract.JOURNAL_COLOUR);
        journalName = journalBundle.getString(JournalContract.JOURNAL_NAME);

        /*Use the journalID we just retrieved from the database to query the database to get the Journal Structure*/
        journalStructureViewModel = ViewModelProviders.of(JournalCompleteEntryActivity.this)
                .get(JournalStructureViewModel.class);
        journalStructureViewModel.getStructureWithID(journalID).observe(this, new Observer<List<JournalStructureObject>>() {
            @Override
            public void onChanged(List<JournalStructureObject> journalStructureObjects) {
                createForm(journalStructureObjects);
            }
        });

        Toast.makeText(this, "Name of Journal and id: " + journalName + " " + journalID , Toast.LENGTH_SHORT).show();

    }

    /** Defines the ScrollView and removes views
     * Then defines the LinearLayout 'scroll' to put the TextViews and EditTexts on
     * Set the Orientation Vertical and add to the scrollView*/
    private void initialiseScrollView(){
        fieldReGeneration = (ScrollView) findViewById(R.id.sv_field_regeneration);
        fieldReGeneration.removeAllViews();
        scroll = new LinearLayout(this);
        scroll.setOrientation(LinearLayout.VERTICAL);
        fieldReGeneration.addView(scroll);
    }

    /**This function takes the list of objects that contain the structure of the Journal the user is to complete
     * and then creates EditText fields where the user can complete an entry
     * The function also sets the names of the columns (which is in the list of objects) as well as parameters for the Edit Text boxes
     *
    * @param - journalStructureObjects - list of Objects that contain the structure of the Journal the user will complete */
    private void createForm(List<JournalStructureObject> journalStructureObjects){

        /*For loop iterates through the list of objects to generate the Edit Texts for the user to fill in*/
        for (int i = 0 ; i < journalStructureObjects.size() ; i++){


            /*Take values from the Objects and store in variables for manipulation*/
            journalStructureObject = journalStructureObjects.get(i);
            Long _id = journalStructureObject.getId();
            String columnName = journalStructureObject.getColumnName();
            String columnType = journalStructureObject.getColumnType();
            Long tableID = journalStructureObject.getFk_id();


            /*Create TextView for the name of the entry field*/
            TextView columnText = new TextView(JournalCompleteEntryActivity.this);

            columnText.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));

            /*Set the name and styling of the TextView*/
            columnText.setText(columnName);
            columnText.setPadding(0,20,0,20);
            columnText.setTextSize(20);
            columnText.setTextColor(Color.BLACK);

            scroll.addView(columnText); //Add the new TextView just created to the layout

            /*If statement checks if the EditText that needs to be generated is for a Column or a Percentage*/
            if(columnType.equals(JournalContract.COLUMN)){

                //CreateEditText Field for the entry data
                EditText newColumn = new EditText(JournalCompleteEntryActivity.this);
                newColumn.setGravity(0);

                newColumn.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        200));

                allEds.add(newColumn);//Add the EditText to an ArrayList for later persistence to the database
                scroll.addView(newColumn);//Add the new EditText just created to the layout
            }
            else if(columnType.equals(JournalContract.PERCENTAGE)){

                //Create EditText Field for the entry data
                EditText newColumn = new EditText(JournalCompleteEntryActivity.this);
                newColumn.setInputType(InputType.TYPE_CLASS_NUMBER);//Set the input type so that the field only accepts integers

                newColumn.setLayoutParams(new LinearLayout.LayoutParams(
                        100,
                        LinearLayout.LayoutParams.MATCH_PARENT));

                allEds.add(newColumn);//Add the Edittext to an ArrayList for later persistence to the database
                scroll.addView(newColumn);//Add the new EditText just created to the layout

            }
            else{

                Log.d("Diss", "Not going in either if/else " );

            }

            //Adding variables to array for later persistance
            columnTypes.add(columnType);
            columnNames.add(columnName);
        }
    }

    /**When the user has completed their entry and chooses to click the 'Save' button, this function is called
     * Some Data is retrieved from the EditTexts and stored in member variables - EntryName, Date and Time of Entry
     * Other data like the EditText fields is already stored in ArrayLists
     * A separate function is called to store all and this is done on a separate thread, as to not block the main thread*/
    public void saveEntryOnClick(View v){

        /*Collecting Strings from edit texts for persistance and to check the fields have the correct data in them
        * The string for date and time is needed for correct persistence to the database, so a check is needed*/
        EditText ed_entry_name = findViewById(R.id.et_name_of_entry);
        st_entry_name = ed_entry_name.getText().toString();

        EditText ed_entry_date = findViewById(R.id.et_entry_date);
        String st_ed_entry_date = ed_entry_date.getText().toString();

        EditText ed_entry_time = findViewById(R.id.et_entry_time);
        String st_ed_entry_time = ed_entry_time.getText().toString();

        /*If date and tie havent been selected dont let them save*/
        if(st_ed_entry_date.trim().isEmpty() || st_ed_entry_time.trim().isEmpty()){
            Toast.makeText(this, "Please enter a Date and Time for your Entry", Toast.LENGTH_SHORT).show();
        }
        else{
            doThingAThenThingB();
            finish();
        }
    }


    /**This function is very important for the persisting of data to the database
     * Room will only do database operations on a background thread as to not block the main thread
     * In order to insert the EntryData the id of the Entry needs to be inserted with the Object as a foriegn key
     * This requires retrieving the id from insertion that has just happened
     * A sharedSingleThreadExecutor needs to be setup so that the insertion can be completed, the id saved in entryID
     * and then that entryID can be used in the next thread to insert the EntryData Object */
    private void doThingAThenThingB(){

        sharedSingleThreadExecutor.execute(new Runnable() {
            @Override
            public void run() {

                JournalSingleEntryObject journalSingleEntryObject = new JournalSingleEntryObject(st_entry_name,
                        date, time, journalName, journalColour, journalID);

                JournalSingleEntryViewModel journalSingleEntryViewModel = ViewModelProviders.of(JournalCompleteEntryActivity.this)
                        .get(JournalSingleEntryViewModel.class);

                Long id = JournalSingleEntryViewModel.InsertNotAsync(journalSingleEntryObject);

                entryID = id;

            }
        });

        sharedSingleThreadExecutor.execute(new Runnable() {
            @Override
            public void run() {

                for(int i = 0 ; i < allEds.size() ; i++){

                    JournalSingleEntryDataObject journalSingleEntryDataObject = new JournalSingleEntryDataObject(
                            columnNames.get(i), columnTypes.get(i), allEds.get(i).getText().toString(),
                             entryID, journalID);

                    journalSingleEntryDataViewModel = ViewModelProviders.of(JournalCompleteEntryActivity.this)
                            .get(JournalSingleEntryDataViewModel.class);

                    journalSingleEntryDataViewModel.insert(journalSingleEntryDataObject);

                }
            }
        });

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

        /*TODO: COMMENT*/
        DatePickerDialog mDatePicker;
        mDatePicker = new DatePickerDialog(JournalCompleteEntryActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                mYear = year;
                mMonth = month;
                mDay = dayOfMonth;
                SimpleDateFormat inFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());

                try {
                    Date myDate = inFormat.parse(mDay+"-"+mMonth+"-"+mYear);
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("E, F MMM", Locale.getDefault());
                    if (myDate != null) {
                        date = inFormat.format(myDate);
                    }
                    //date =simpleDateFormat.format(myDate);

                    et_entry_goal_date.setText(date);


                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        }, mYear, mMonth, mDay);//Yes 24 hour time
        mDatePicker.setTitle("Select Date");
        mDatePicker.show();

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

        /*TODO: COMMENT*/
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(JournalCompleteEntryActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                mHour = hourOfDay;
                mMinute = minute;;
                //Getting current time to save in database
                try {
                    SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
                    Date myTime = timeFormat.parse(hourOfDay + ":" + minute);
                    time= timeFormat.format(myTime);

                    et_entry_goal_time.setText(time);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        }, mHour, mMinute, true);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();

    }

}