package com.example.diss_cbt_application.JournalFeature.JActivities_Fragments.Entries;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.diss_cbt_application.JournalFeature.JDatabase.JDTables.JournalSingleEntryDataObject;
import com.example.diss_cbt_application.JournalFeature.JDatabase.JDTables.JournalSingleEntryObject;
import com.example.diss_cbt_application.JournalFeature.JDatabase.JDViewModels.JournalSingleEntryDataViewModel;
import com.example.diss_cbt_application.JournalFeature.JDatabase.JDViewModels.JournalSingleEntryViewModel;
import com.example.diss_cbt_application.JournalFeature.JActivities_Fragments.General.JournalContract;
import com.example.diss_cbt_application.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * The user is shown this Activity are choosing to View a specific entry they have made
 * This Activity is a skeleton to represent any entries data to the user
 * The user has the option of editing/updating their entry as well as deleting it
 * Both of these options will be sent to the database which will be updated appropriately*/
public class JournalEntryData extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    /*Member Variables fo the text views in the Activity*/
    TextView tv_entry_name, tv_journal_name, tv_journal_date, tv_journal_time;
    EditText et_entry_name;

    /*Setting strings*/
    private static final String TEXT_VIEW = "TextView";
    private static final String EDIT_VIEW = "EditView";
    String gDataRepresentation, date, time, mEntryName, mJournalName, mEntryTime, mEntryDate;
    String st_dayOfMonth, st_monthOfYear, st_year, st_hour, st_minute;

    /*Member Variables*/
    Long mainEntryID = 0L;
    Long fk_id;
    int mJournalColour, mYear, mMonth, mDay, mHour, mMinute,
                    ps_year, ps_monthOfYear, ps_dayOfMonth, ps_hour, ps_minute;
    boolean edit = true;
    private JournalSingleEntryDataViewModel journalSingleEntryDataViewModel;
    LinearLayout scroll;

    /*Defining Arraylists used throughout the class*/
    List<String> columnNames =new ArrayList<>();
    List<String> entryDataList =new ArrayList<>();
    List<String> columnTypes =new ArrayList<>();
    ArrayList<EditText> allEds = new ArrayList<>();
    List<Long> uniqueEntryIDs = new ArrayList<>();
    List<Long> fk_eids = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal_entry_data);

        /*Clearing the arrays as a precaution, to ensure there is no data remaining in them
        * They are very important for persisting the entries into the database, so indexing needs to be correct*/
        uniqueEntryIDs.clear();
        fk_eids.clear();


        /*Initialising values in onCreate as they are used in checks later in the class*/
        time = "";
        date = "";
        mainEntryID = 0L;

        unpackingBundle();//unpack bundle and populate Text Fields

        createEntryData(mainEntryID, TEXT_VIEW);//initially the data just needs to be presented to the user in a TextView
    }

    /*This function simply extracts away the unpacking of the bundle
    and the initial representation of the data for the entry*/
    public void unpackingBundle(){

        /*Unpacking the bundle and placing values in variables*/
        Bundle entryBundle = getIntent().getExtras();
        mainEntryID = entryBundle.getLong(JournalContract._ID);
        mEntryName = entryBundle.getString(JournalContract.ENTRY_NAME);
        mJournalName = entryBundle.getString(JournalContract.JOURNAL_NAME);
        mEntryTime = entryBundle.getString(JournalContract.ENTRY_TIME);
        mEntryDate = entryBundle.getString(JournalContract.ENTRY_DATE);
        mJournalColour = entryBundle.getInt(JournalContract.JOURNAL_COLOUR);
        fk_id = entryBundle.getLong(JournalContract.FK_ID);

        /*Getting reference to the TextViews in the Activity*/
        tv_entry_name = findViewById(R.id.tv_entry_name);
        tv_journal_name = findViewById(R.id.tv_journal_name);
        tv_journal_date = findViewById(R.id.tv_entry_data_date);
        tv_journal_time = findViewById(R.id.tv_entry_data_time);

        /*Setting the TextViews with the data retrieved from the bundle*/
        tv_entry_name.setText(mEntryName);
        tv_journal_name.setText(mJournalName);
        tv_journal_name.setTextColor(mJournalColour);
        tv_journal_date.setText(mEntryDate);
        tv_journal_time.setText(mEntryTime);

    }

    /** This function queries the database for the rest of the data assigned to the entry with the uniqueID we parse
     *If the user wants to Edit any of the fields they have the option to click the 'Edit' button
     *If this is the case the data needs to be regenerated onto EditTexts
     *
     *@param - entryID - the ID of the entry for which we want to see the data, this is needed to query the database
     *@param - dataRepresentation - depending on which string is parsed depends on if the data is put into a TextView or an EditText*/
    public void createEntryData(final Long entryID, String dataRepresentation ){

        /*We need a global string value for the datarepresentation so it can be accessed from the oberver onChanged function*/
        gDataRepresentation = dataRepresentation;

        /*Observe the database, sending a query for data entries that have the foreign key id we pass (which is the mainID of the entry*/
        journalSingleEntryDataViewModel = ViewModelProviders.of(JournalEntryData.this)
                .get(JournalSingleEntryDataViewModel.class);
        journalSingleEntryDataViewModel.getEntryDataWithId(mainEntryID).observe(this, new Observer<List<JournalSingleEntryDataObject>>() {
            @Override
            public void onChanged(List<JournalSingleEntryDataObject> journalSingleEntryDataObjects) {

                initialiseScrollView();//function called to initialise the scroll view

                /*For loop iterates through all of the journalSingleEntryDataObjects and populates the activity with the data from the Objects*/
                for(int i = 0 ; i < journalSingleEntryDataObjects.size() ; i++){

                    Log.d("Diss", "In cursor move ");

                    /*Take values from the Objects and store in variables for manipulation and display*/
                    Long _id =  journalSingleEntryDataObjects.get(i).getId();
                    String columnName = journalSingleEntryDataObjects.get(i).getColumnName();
                    String columnType = journalSingleEntryDataObjects.get(i).getColumnType();
                    String entryData = journalSingleEntryDataObjects.get(i).getEntryData();
                    Long entryID_ = journalSingleEntryDataObjects.get(i).getFk_eid();

                    //Name of the Entry Field
                    TextView columnText = new TextView(getApplicationContext());
                    columnText.setText(columnName);
                    columnText.setTextSize(25);
                    columnText.setTextColor(Color.parseColor("#000000"));

                    scroll.addView(columnText);//Add to LinearLayout on ScrollView

                    /*This if statements checks what string was parsed into the function
                    * If TEXT_VIEW was parse display the entry data in a TextView
                    * If EDIT_VIEW was parsed display the entry data in an EditText so the user can update their entry*/
                    if(gDataRepresentation == TEXT_VIEW){
                        //Text Field for the entry data
                        TextView entryData_ = new TextView(JournalEntryData.this);

                        entryData_.setLayoutParams(new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT));

                        entryData_.setText(entryData);

                        scroll.addView(entryData_);//Add to LinearLayout on ScrollView

                        uniqueEntryIDs.add(_id);
                        fk_eids.add(entryID_);

                    }
                    else if(gDataRepresentation == EDIT_VIEW){
                        //Text Field for the entry data
                        EditText entryDataView = new EditText(JournalEntryData.this);

                        entryDataView.setLayoutParams(new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT));

                        entryDataView.setText(entryData);

                        allEds.add(entryDataView);//Adding variables to array for later persistence to the database

                        scroll.addView(entryDataView);//Add to LinearLayout on ScrollView
                    }

                    //Adding variables to array for later persistence to the database
                    entryDataList.add(entryData);
                    columnNames.add(columnName);
                    columnTypes.add(columnType);

                }

            }
        });

    }

    /**Defines the ScrollView and removes views
     * Then defines the LinearLayout 'scroll' to put the TextViews and EditTexts on
     * Set the Orientation Vertical and add to the scrollView*/
    private void initialiseScrollView(){
        ScrollView fieldReGeneration = findViewById(R.id.sv_field_generation_entry_data);
        fieldReGeneration.removeAllViews();
        scroll = new LinearLayout(getApplicationContext());
        scroll.setOrientation(LinearLayout.VERTICAL);
        fieldReGeneration.addView(scroll);
    }

    /**If the user wants to edit their entry this function handles it
    * Boolean Value for edit or save, once in edit mode anything they change will be saved*/
    public void editButtonOnClick(View v){

        Button bt_save_edit = findViewById(R.id.bt_edit);

        if(edit){//true


            tv_entry_name = findViewById(R.id.tv_entry_name);
            tv_entry_name.setVisibility(View.INVISIBLE);

            et_entry_name = findViewById(R.id.et_entry_name);
            et_entry_name.setVisibility(View.VISIBLE);
            et_entry_name.setText(mEntryName);

            /*Re-Generate the EditTexts with the data so they can be edited*/
            createEntryData(mainEntryID, EDIT_VIEW);
            bt_save_edit.setText("Save");
            edit = false;

        }
        else{//false

            /*Save the fields the user has been edited back to the database*/
            bt_save_edit.setText("Edit");


            /*TODO: COMMENT*/
            if(time.equals("")){
                time = mEntryTime;
            }

            if(date.equals("")){
                date = mEntryDate;
            }

            saveEditedEntry();
            saveEditedEntryData();
        }

    }

    public void saveEditedEntry(){

        Calendar entryDateTime = Calendar.getInstance();

        entryDateTime.set(Calendar.YEAR, mYear);
        entryDateTime.set(Calendar.MONTH, mMonth);
        entryDateTime.set(Calendar.DAY_OF_MONTH, mDay);
        entryDateTime.set(Calendar.HOUR_OF_DAY, mHour);
        entryDateTime.set(Calendar.MINUTE, mMinute);
        entryDateTime.set(Calendar.SECOND, 0);

        Long dateTime = System.currentTimeMillis();

        JournalSingleEntryObject journalSingleEntryObject = new JournalSingleEntryObject(et_entry_name.getText().toString(),
                date, time, entryDateTime.getTimeInMillis(), mJournalName, mJournalColour, fk_id);

        journalSingleEntryObject.setId(mainEntryID);

        JournalSingleEntryViewModel journalSingleEntryViewModel = ViewModelProviders.of(JournalEntryData.this).get(JournalSingleEntryViewModel.class);
        journalSingleEntryViewModel.update(journalSingleEntryObject);

    }


    /*Called when the user chooses to 'Save' the update they made to their entry
    * Sends an update request to the database**/
    public void saveEditedEntryData(){


        /*Iterates through all of the objects and updates the fields with the changes the user has made
        * the 'uniqueEntryIDs array is very important as it ensures the correct fields in the database are being updated*/
        for(int i=0; i < uniqueEntryIDs.size(); i++){

            JournalSingleEntryDataObject journalSingleEntryDataObject = new JournalSingleEntryDataObject(
                    columnNames.get(i), columnTypes.get(i), allEds.get(i).getText().toString(),
                     fk_eids.get(i), fk_id );

            journalSingleEntryDataViewModel = ViewModelProviders.of(JournalEntryData.this)
                    .get(JournalSingleEntryDataViewModel.class);

            journalSingleEntryDataObject.setId(uniqueEntryIDs.get(i));


            journalSingleEntryDataViewModel.update(journalSingleEntryDataObject);
        }

        finish();
    }


    /*If the user want to delete their entry this function handles it*/
    public void deleteButtonOnClick(View v){

        String table = "SEntry";
        String whereClause = "_id=?";
        String[] whereArgs = new String[] { String.valueOf(mainEntryID) };

        finish();

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

        String st_date = tv_journal_date.getText().toString();
        String[] dateArray = st_date.split("-");
        st_dayOfMonth = dateArray[0];
        st_monthOfYear = dateArray[1];
        st_year = dateArray[2];

        ps_year = Integer.parseInt(st_year);
        ps_monthOfYear = Integer.parseInt(st_monthOfYear);
        ps_dayOfMonth = Integer.parseInt(st_dayOfMonth);

        /*TODO: COMMENT*/
        DatePickerDialog datePickerDialog = new DatePickerDialog(JournalEntryData.this,
                (DatePickerDialog.OnDateSetListener) this,
                ps_year,
                ps_monthOfYear,
                ps_dayOfMonth
        );
        datePickerDialog.show();


    }

    /*TODO: COMMENT*/
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        mYear = year;
        mMonth = month;
        mDay = dayOfMonth;
        SimpleDateFormat inFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());

        /*TODO: COMMENT*/
        try {
            Date myDate = inFormat.parse(mDay+"-"+mMonth+"-"+mYear);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("E, F MMM", Locale.getDefault());
            date = inFormat.format(myDate);
            //date =simpleDateFormat.format(myDate);

            tv_journal_date.setText(date);


        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * This function utilises the TimePickerDialog provided by android to give the user a
     * clean and simple way of choosing the time for the goal to be completed by
     * the integers mHour, mMinute previously defined are set in this function
     * as is the EditText next to the Date Picker which shows the user the date they picked*/
    public void chooseTimeOnClick(View v){


        /*TODO: COMMENT*/
        // Get Current Time
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        String[] dateArray = mEntryTime.split(":");
        st_hour = dateArray[0];
        st_minute = dateArray[1];

        ps_hour = Integer.parseInt(st_hour);
        ps_minute = Integer.parseInt(st_minute);

        /*TODO: COMMENT*/

        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(JournalEntryData.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                mHour = hourOfDay;
                mMinute = minute;

                try {
                    SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
                    Date myTime = timeFormat.parse(hourOfDay + ":" + minute);
                    time = timeFormat.format(myTime);

                    tv_journal_time.setText(time);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        }, ps_hour, ps_minute, true);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();

    }

}
