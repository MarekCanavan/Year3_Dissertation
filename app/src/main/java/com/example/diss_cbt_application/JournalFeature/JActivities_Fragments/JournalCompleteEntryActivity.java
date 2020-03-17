package com.example.diss_cbt_application.JournalFeature.JActivities_Fragments;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.diss_cbt_application.DatabaseHelper;
import com.example.diss_cbt_application.JournalFeature.JDatabase.JDTables.JournalSingleEntryDataObject;
import com.example.diss_cbt_application.JournalFeature.JDatabase.JDTables.JournalSingleEntryObject;
import com.example.diss_cbt_application.JournalFeature.JDatabase.JDTables.JournalStructureObject;
import com.example.diss_cbt_application.JournalFeature.JDatabase.JDViewModels.JournalSingleEntryDataViewModel;
import com.example.diss_cbt_application.JournalFeature.JDatabase.JDViewModels.JournalSingleEntryViewModel;
import com.example.diss_cbt_application.JournalFeature.JDatabase.JDViewModels.JournalStructureViewModel;
import com.example.diss_cbt_application.JournalFeature.JournalContract;
import com.example.diss_cbt_application.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class JournalCompleteEntryActivity extends AppCompatActivity {


    int columnCounter, journalColour;
    Long journalID;
    String journalIDString, journalNameString;
    Long entryID;
    private ScrollView fieldReGeneration;
    LinearLayout scroll;
    ArrayList<EditText> allEds = new ArrayList<EditText>();
    List<String> columnTypes =new ArrayList<String>();
    List<String> columnNames = new ArrayList<String>();

    String st_entry_name, date, time;


    private List<JournalStructureObject> journalStructuresWithIds;
    JournalStructureViewModel journalStructureViewModel;
    JournalStructureObject journalStructureObject;

    JournalSingleEntryDataViewModel journalSingleEntryDataViewModel;


    private Executor sharedSingleThreadExecutor = Executors.newSingleThreadExecutor();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal_complete_entry);

        fieldReGeneration = (ScrollView) findViewById(R.id.sv_field_regeneration);
        fieldReGeneration.removeAllViews();
        scroll = new LinearLayout(this);
        scroll.setOrientation(LinearLayout.VERTICAL);
        fieldReGeneration.addView(scroll);

        columnCounter = 0;


        Bundle journalBundle = getIntent().getExtras();
        journalID = journalBundle.getLong("id");
        String journalName = journalBundle.getString("journalName");
        journalNameString = journalBundle.getString("journalName");

        Toast.makeText(this, "Name of Journal and id: " + journalName + " " + journalID , Toast.LENGTH_SHORT).show();

        journalColour = journalBundle.getInt(JournalContract.JOURNAL_COLOUR);

        journalStructureViewModel = ViewModelProviders.of(JournalCompleteEntryActivity.this)
                .get(JournalStructureViewModel.class);
        journalStructureViewModel.getStructureWithID(journalID).observe(this, new Observer<List<JournalStructureObject>>() {
            @Override
            public void onChanged(List<JournalStructureObject> journalStructureObjects) {

                Log.d("Diss", "Seeing if there is anything in the list: " +
                        journalStructureObjects.get(0));

                createForm(journalStructureObjects);
            }
        });
    }

    private void createForm(List<JournalStructureObject> journalStructureObjects){


        for (int i = 0 ; i < journalStructureObjects.size() ; i++){

            journalStructureObject = journalStructureObjects.get(i);
            /*Take values from the cursor and store in variables for manipulation*/
            Long _id = journalStructureObject.getId();
            String columnName = journalStructureObject.getColumnName();
            String columnType = journalStructureObject.getColumnType();
            Long tableID = journalStructureObject.getFk_id();

            Log.d("Diss", "Value of Column Type: " + columnType);

            //Name of the Entry Field
            TextView columnText = new TextView(JournalCompleteEntryActivity.this);

            columnText.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));

            columnText.setText(columnName);
            columnText.setPadding(0,20,0,20);
            columnText.setTextSize(20);
            columnText.setTextColor(Color.BLACK);

            //Add the new columns just created to the layout
            scroll.addView(columnText);

            if(columnType.equals(JournalContract.COLUMN)){

                Log.d("Diss", "Value of Column Type in if: " + columnType);

                //EditText Field for the entry data
                EditText newColumn = new EditText(JournalCompleteEntryActivity.this);
                newColumn.setGravity(0);

                newColumn.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        200));

                allEds.add(newColumn);
                scroll.addView(newColumn);
            }
            else if(columnType.equals(JournalContract.PERCENTAGE)){

                Log.d("Diss", "Value of Column Type in if else: " + columnType);


                //EditText Field for the entry data
                EditText newColumn = new EditText(JournalCompleteEntryActivity.this);
                newColumn.setInputType(InputType.TYPE_CLASS_NUMBER);

                newColumn.setLayoutParams(new LinearLayout.LayoutParams(
                        100,
                        LinearLayout.LayoutParams.MATCH_PARENT));

                allEds.add(newColumn);
                scroll.addView(newColumn);

            }
            else{
                Log.d("Diss", "Not going in either if/else " );

            }

            //Adding variables to array for later persistance
            columnTypes.add(columnType);
            columnNames.add(columnName);

        }

    }

    /*Function saves entries to the SingleEntries Database*/
    public void saveEntryOnClick(View v){

        /*Values being saved into the database
                entryName - EditText
                columnName - in Array
                columnType - in Array
                entryData - EditText in Array
                tableID - Passed in onCreate*/

        //Collecting entryName from EditText

        EditText ed_entry_name = (EditText) findViewById(R.id.et_name_of_entry);
        st_entry_name = ed_entry_name.getText().toString();

        //Getting current date to save in database
        SimpleDateFormat dateFormat = new SimpleDateFormat("E, F MMM", Locale.getDefault());
        date = dateFormat.format(new java.util.Date());

        //Getting current time to save in database
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        time = timeFormat.format(new java.util.Date());

        sharedSingleThreadExecutor.execute(new Runnable() {
            @Override
            public void run() {

                JournalSingleEntryObject journalSingleEntryObject = new JournalSingleEntryObject(st_entry_name,
                        date, time, journalNameString, journalColour, journalID);

                JournalSingleEntryViewModel journalSingleEntryViewModel = ViewModelProviders.of(JournalCompleteEntryActivity.this)
                        .get(JournalSingleEntryViewModel.class);

                Long id = JournalSingleEntryViewModel.InsertNotAsync(journalSingleEntryObject);

                entryID = id;
                Log.d("Diss", "Value of table id in doThingAdoThingb: " + entryID);
            }
        });

        sharedSingleThreadExecutor.execute(new Runnable() {
            @Override
            public void run() {

                for(int i = 0 ; i < allEds.size() ; i++){

                    JournalSingleEntryDataObject journalSingleEntryDataObject = new JournalSingleEntryDataObject(
                            columnNames.get(i), columnTypes.get(i), allEds.get(i).getText().toString(),
                            date, time, entryID);

                    journalSingleEntryDataViewModel = ViewModelProviders.of(JournalCompleteEntryActivity.this)
                            .get(JournalSingleEntryDataViewModel.class);

                    journalSingleEntryDataViewModel.insert(journalSingleEntryDataObject);

                }


            }
        });

        finish();

    }
}
