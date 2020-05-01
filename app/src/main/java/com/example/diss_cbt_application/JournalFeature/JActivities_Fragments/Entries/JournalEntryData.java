package com.example.diss_cbt_application.JournalFeature.JActivities_Fragments.Entries;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.diss_cbt_application.Utils.DataPresentation;
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
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * The user is shown this Activity after choosing to View a specific entry they have made.
 * This Activity is a skeleton to represent any entries data to the user.
 * The user has the option of editing/updating their entry as well as deleting it.
 * Both of these options will be sent to the database which will be updated appropriately.*/
public class JournalEntryData extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    /*Member Variables fo the text views in the Activity*/
    TextView tv_entry_name, tv_journal_name;
    EditText et_entry_name;

    /*Setting strings*/
    String date, time, mEntryName, mJournalName, mEntryTime, mEntryDate;
    String st_dayOfMonth, st_monthOfYear, st_year, st_hour, st_minute;

    /*Member Variables*/
    Long mainEntryID = 0L;
    Long fk_id;
    int mJournalColour, mYear, mMonth, mDay, mHour, mMinute,
                    ps_year, ps_monthOfYear, ps_dayOfMonth, ps_hour, ps_minute;
    boolean edit = true;
    private JournalSingleEntryDataViewModel journalSingleEntryDataViewModel;
    JournalSingleEntryViewModel journalSingleEntryViewModel;

    /*Defining Arraylists used throughout the class*/
    List<String> columnNames =new ArrayList<>();
    List<String> columnTypes =new ArrayList<>();
    ArrayList<EditText> allEds = new ArrayList<>();
    List<Long> uniqueEntryIDs = new ArrayList<>();
    List<Long> fk_eids = new ArrayList<>();

    Button bt_goal_time_picker, bt_goal_date_picker;
    DataPresentation dataPresentationObj;
    ScrollView scroll;

    /**The onCreate function is responsible for initiaising some of the member variables
     * and calling a function that unpacks the bundle that was sent from the user clicking the recyclerview.
     * In addition an instance of DataPresentation is created so the entryData is set on the ScrollView*/
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
        scroll = findViewById(R.id.sv_field_generation_entry_data);        /*Observe the database, sending a query for data entries that have the foreign key id we pass (which is the mainID of the entry*/
        journalSingleEntryDataViewModel = ViewModelProviders.of(JournalEntryData.this)
                .get(JournalSingleEntryDataViewModel.class);

        unpackingBundle();//unpack bundle and populate Text Fields

        dataPresentationObj = new DataPresentation(this, scroll, JournalContract.TEXT_VIEW, mainEntryID,
                journalSingleEntryDataViewModel);
    }

    /**This function simply extracts away the unpacking of the bundle
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
        bt_goal_time_picker = findViewById(R.id.bt_goal_time_picker);
        bt_goal_date_picker = findViewById(R.id.bt_goal_date_picker);

        /*Setting the TextViews with the data retrieved from the bundle*/
        tv_entry_name.setText(mEntryName);
        tv_journal_name.setText(mJournalName);
        tv_journal_name.setTextColor(mJournalColour);
        bt_goal_date_picker.setText("Date : " + mEntryDate);
        bt_goal_time_picker.setText("Time : " + mEntryTime);

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
            et_entry_name.setImeOptions(EditorInfo.IME_ACTION_DONE);
            et_entry_name.setRawInputType(InputType.TYPE_CLASS_TEXT);

            dataPresentationObj.setDataRepresentation(JournalContract.EDIT_VIEW);
            dataPresentationObj.runEntryData(this, scroll, journalSingleEntryDataViewModel);

            bt_save_edit.setText("Save");
            edit = false;

        }
        else{//false

            /*Save the fields the user has been edited back to the database*/
            bt_save_edit.setText("Edit");

            /*Checks if a new time and date have been set before saving the data,If not make the values which will be persisted to the database
            * equal to the initial date and time for the entry*/
            if(time.equals("")){
                time = mEntryTime;
            }
            if(date.equals("")){
                date = mEntryDate;
            }

            saveEditedEntry();//Save Entry Name
            saveEditedEntryData();//Save EntryData
            finish();//Finish Activity
        }
    }

    /**This function saves the title of the entry to the databsae by creating a new object,
     * setting the id to the id of this entry and calling update on the SingleEntryViewModel*/
    public void saveEditedEntry(){

        Calendar entryDateTime = Calendar.getInstance();

        entryDateTime.set(Calendar.YEAR, mYear);
        entryDateTime.set(Calendar.MONTH, mMonth);
        entryDateTime.set(Calendar.DAY_OF_MONTH, mDay);
        entryDateTime.set(Calendar.HOUR_OF_DAY, mHour);
        entryDateTime.set(Calendar.MINUTE, mMinute);
        entryDateTime.set(Calendar.SECOND, 0);

        JournalSingleEntryObject journalSingleEntryObject = new JournalSingleEntryObject(et_entry_name.getText().toString(),
                date, time, entryDateTime.getTimeInMillis(), mJournalName, mJournalColour, fk_id);

        journalSingleEntryObject.setId(mainEntryID);

        JournalSingleEntryViewModel journalSingleEntryViewModel = ViewModelProviders.of(JournalEntryData.this).get(JournalSingleEntryViewModel.class);
        journalSingleEntryViewModel.update(journalSingleEntryObject);
    }

    /**Called when the user chooses to 'Save' the update they made to their entry
    * Sends an update request to the database**/
    public void saveEditedEntryData(){

        allEds = dataPresentationObj.getAllEds();
        columnNames = dataPresentationObj.getColumnNames();
        columnTypes = dataPresentationObj.getColumnTypes();
        fk_eids = dataPresentationObj.getFk_eids();
        uniqueEntryIDs = dataPresentationObj.getUniqueEntryIDs();


        /*Iterates through all of the objects and updates the fields with the changes the user has made
        * the 'uniqueEntryIDs array is very important as it ensures the correct fields in the database are being updated*/
        for(int i=0; i < uniqueEntryIDs.size(); i++){

            EditText et_temp = allEds.get(i);

            JournalSingleEntryDataObject journalSingleEntryDataObject = new JournalSingleEntryDataObject(
                    columnNames.get(i), columnTypes.get(i), et_temp.getText().toString(),
                     fk_eids.get(i), fk_id );

            journalSingleEntryDataViewModel = ViewModelProviders.of(JournalEntryData.this)
                    .get(JournalSingleEntryDataViewModel.class);

            journalSingleEntryDataObject.setId(uniqueEntryIDs.get(i));

            journalSingleEntryDataViewModel.update(journalSingleEntryDataObject);
        }
    }

    /**If the user wants to delete their entry this function handles it but first a dialog box
    * is shown to confirm the user wants to delete their entry.*/
    public void deleteButtonOnClick(View v){
        AlertDialog diaBox = AskOption();
        diaBox.show();
    }

    /*Shared execution thread is needed for the database persistence*/
    private Executor deleteSharedSingleThreadExecutor = Executors.newSingleThreadExecutor();
    JournalSingleEntryObject entryObjectForDeletion;

    /**This shared thread of execution is responsible for deleting an object. To delete an object it first
     * needs to be retrieved, so this is done in the first thread. Then it is deleted from the database
     * in the second thread of execution.*/
    private void saveEntrySharedThreadOfExecution(){

        journalSingleEntryViewModel = ViewModelProviders.of(JournalEntryData.this)
                .get(JournalSingleEntryViewModel.class);

        /*To Delete First You Need the Object */
        /*Thread 1  */
        deleteSharedSingleThreadExecutor.execute(new Runnable() {
            @Override
            public void run() {
                entryObjectForDeletion = journalSingleEntryViewModel.getEntryWithId(mainEntryID);
            }
        });

        /*Thread 2 -*/
        deleteSharedSingleThreadExecutor.execute(new Runnable() {
            @Override
            public void run() {
                journalSingleEntryViewModel.delete(entryObjectForDeletion);
            }
        });
    }

    /**
     * This AlertDialog box is created when the user indicates they want to delete an entry. The check is done
     * to confirm they want to delete that entry. So 2 options are generated, if the user chooses to cancel then the
     * dialog box is dismissed. If the user confirms they want to delete then the logic is triggered to delete the entry,
     **/
    private AlertDialog AskOption(){
        AlertDialog myDeletingDialogBox = new AlertDialog.Builder(this)
                .setTitle("Delete")
                .setMessage("Are you sure you want to delete? There wil be no way to retrieve it")
                .setIcon(R.drawable.ic_delete_black)
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        saveEntrySharedThreadOfExecution();
                        finish();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();

        return myDeletingDialogBox;
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

        String st_date = mEntryDate;
        String[] dateArray = st_date.split("-");
        st_dayOfMonth = dateArray[0];
        st_monthOfYear = dateArray[1];
        st_year = dateArray[2];

        ps_year = Integer.parseInt(st_year);
        ps_monthOfYear = Integer.parseInt(st_monthOfYear);
        ps_dayOfMonth = Integer.parseInt(st_dayOfMonth);

        DatePickerDialog datePickerDialog = new DatePickerDialog(JournalEntryData.this,
                (DatePickerDialog.OnDateSetListener) this,
                ps_year,
                ps_monthOfYear,
                ps_dayOfMonth
        );
        datePickerDialog.show();


    }

    /**This function sets the date picker dialog so that when the user goes to edit the date
     * it is already loaded with the date they last set for the entry. */
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        mYear = year;
        mMonth = month;
        mDay = dayOfMonth;
        SimpleDateFormat inFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());

        try {
            Date myDate = inFormat.parse(mDay+"-"+mMonth+"-"+mYear);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("E, F MMM", Locale.getDefault());
            date = inFormat.format(myDate);
            bt_goal_date_picker.setText("Date : " + date);

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

        // Get Current Time
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        String[] dateArray = mEntryTime.split(":");
        st_hour = dateArray[0];
        st_minute = dateArray[1];

        ps_hour = Integer.parseInt(st_hour);
        ps_minute = Integer.parseInt(st_minute);

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

                    bt_goal_time_picker.setText("Time : " + time);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        }, ps_hour, ps_minute, true);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }
}
