package com.example.diss_cbt_application.JournalFeature.JActivities_Fragments;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.diss_cbt_application.DatabaseHelper;
import com.example.diss_cbt_application.JournalFeature.JDatabase.JDTables.JournalSingleEntryDataObject;
import com.example.diss_cbt_application.JournalFeature.JDatabase.JDTables.JournalStructureObject;
import com.example.diss_cbt_application.JournalFeature.JDatabase.JDViewModels.JournalSingleEntryDataViewModel;
import com.example.diss_cbt_application.JournalFeature.JDatabase.JDViewModels.JournalStructureViewModel;
import com.example.diss_cbt_application.JournalFeature.JournalContract;
import com.example.diss_cbt_application.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class JournalEntryData extends AppCompatActivity {

    TextView tv_entry_name, tv_journal_name, tv_journal_date, tv_journal_time;

    private DatabaseHelper dbHelper = null; //reference to db helper for insertion
    private SQLiteDatabase db_write, db_read;
    private static String st_Text_View = "TextView";
    private static String st_Edit_View = "EditView";
    Long mainEntryID = 0L;
    int mJournalColour;



    String gDataRepresentation, date, time;


    List<String> columnNames =new ArrayList<String>();
    List<String> entryDataList =new ArrayList<String>();
    List<String> columnTypes =new ArrayList<String>();
    ArrayList<EditText> allEds = new ArrayList<EditText>();
    ArrayList<TextView> allTvs = new ArrayList<TextView>();
    List<Long> uniqueEntryIDs = new ArrayList<Long>();
    List<Long> fk_ids = new ArrayList<Long>();

    private JournalSingleEntryDataViewModel journalSingleEntryDataViewModel;




    boolean edit = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal_entry_data);

        /*Setting references to the database*/
        dbHelper = new DatabaseHelper(this);
        db_write = dbHelper.getWritableDatabase();
        db_read = dbHelper.getReadableDatabase();

        uniqueEntryIDs.clear();
        fk_ids.clear();


        /*Unpacking the bundle and placing values in varaibles*/
        Bundle entryBundle = getIntent().getExtras();;
        mainEntryID = entryBundle.getLong("_id");
        String mEntryName = entryBundle.getString(JournalContract.ENTRY_NAME);
        String mJournalName = entryBundle.getString(JournalContract.JOURNAL_NAME);
        String mEntryTime = entryBundle.getString(JournalContract.ENTRY_TIME);
        String mEntryDate = entryBundle.getString(JournalContract.ENTRY_DATE);
        mJournalColour = entryBundle.getInt(JournalContract.JOURNAL_COLOUR);

       // Log.d("Diss", "Value of id from bundle: " + mainEntryID);
        Toast.makeText(this, "Value of Entry ID: " + mainEntryID, Toast.LENGTH_SHORT).show();


        /*Getting reference to the textViews*/
        tv_entry_name = findViewById(R.id.tv_entry_name);
        tv_journal_name = findViewById(R.id.tv_journal_name);
        tv_journal_date = findViewById(R.id.tv_entry_data_date);
        tv_journal_time = findViewById(R.id.tv_entry_data_time);

        tv_entry_name.setText(mEntryName);
        tv_journal_name.setText(mJournalName);
        tv_journal_name.setTextColor(mJournalColour);
        tv_journal_date.setText(mEntryDate);
        tv_journal_time.setText(mEntryTime);

        createEntryData(mainEntryID, st_Text_View);
    }

    /*dataRepresentation is pararmemter that defines if an EditText will be generated for the data or a TextView
    * In the editButton onClick an If statement determines what value will be set
    * Doing it this way save copying a lot of the same good*/
    public void createEntryData(final Long entryID, String dataRepresentation ){

        Log.d("Diss", "In createEntryData");
        gDataRepresentation = dataRepresentation;

        journalSingleEntryDataViewModel = ViewModelProviders.of(JournalEntryData.this)
                .get(JournalSingleEntryDataViewModel.class);
        journalSingleEntryDataViewModel.getEntryDataWithId(mainEntryID).observe(this, new Observer<List<JournalSingleEntryDataObject>>() {
            @Override
            public void onChanged(List<JournalSingleEntryDataObject> journalSingleEntryDataObjects) {

                /*Very important code
                 * Defines the ScrollView and removes views
                 * Then defines the LinearLayout 'scroll' to put the TextViews and EditTexts on
                 * Set the Orientation Vertical and add to the scrollView*/
                ScrollView fieldReGeneration = (ScrollView) findViewById(R.id.sv_field_generation_entry_data);
                fieldReGeneration.removeAllViews();
                LinearLayout scroll = new LinearLayout(getApplicationContext());
                scroll.setOrientation(LinearLayout.VERTICAL);
                fieldReGeneration.addView(scroll);

                for(int i = 0 ; i < journalSingleEntryDataObjects.size() ; i++){

                    Log.d("Diss", "In cursor move ");

                    /*Take values from the cursor and store in variables for manipulation*/
                    Long _id =  journalSingleEntryDataObjects.get(i).getId();
                    String columnName = journalSingleEntryDataObjects.get(i).getColumnName();
                    String columnType = journalSingleEntryDataObjects.get(i).getColumnType();
                    String entryData = journalSingleEntryDataObjects.get(i).getEntryData();
                    Long entryID_ = journalSingleEntryDataObjects.get(i).getFk_id();

                    //Name of the Entry Field
                    TextView columnText = new TextView(getApplicationContext());
                    columnText.setText(columnName);
                    columnText.setTextSize(25);
                    columnText.setTextColor(Color.parseColor("#000000"));

                    scroll.addView(columnText);//Add to LinearLayout on ScrollView


                    if(gDataRepresentation == st_Text_View){
                        //Text Field for the entry data
                        TextView entryData_ = new TextView(JournalEntryData.this);

                        entryData_.setLayoutParams(new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT));

                        entryData_.setText(entryData);

                        scroll.addView(entryData_);//Add to LinearLayout on ScrollView

                        uniqueEntryIDs.add(_id);
                        fk_ids.add(entryID_);

                        Log.d("updatetest", "Value of _id in creation: " + _id );
                        Log.d("updatetest", "Value of fk_id in creation: " + entryID_ );

                    }
                    else if(gDataRepresentation == st_Edit_View){
                        //Text Field for the entry data
                        EditText entryDataView = new EditText(JournalEntryData.this);

                        entryDataView.setLayoutParams(new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT));

                        entryDataView.setText(entryData);

                        allEds.add(entryDataView);

                        scroll.addView(entryDataView);//Add to LinearLayout on ScrollView
                    }


                    //Adding variables to array for later persistance
                    entryDataList.add(entryData);
                    columnNames.add(columnName);
                    columnTypes.add(columnType);



                }

            }
        });

    }

    /*If the user wants to edit their entry this function handles it
    * Boolean Value for edit or save, once in edit mode anything they change will be saved*/
    public void editButtonOnClick(View v){

        Button bt_save_edit = findViewById(R.id.bt_edit);

        if(edit){//true

            /*Re-Generate the EditTexts with the data so they can be edited*/
            createEntryData(mainEntryID, st_Edit_View);

            bt_save_edit.setText("Save");
            edit = false;
        }
        else{//false
            /*Save the fields the user has been edited back to the database*/

            bt_save_edit.setText("Edit");
            saveEditedData();
            createEntryData(mainEntryID, st_Text_View);

            edit = true;
        }


    }

    public void saveEditedData(){

        //Getting current date to save in database
        SimpleDateFormat dateFormat = new SimpleDateFormat("E, F MMM", Locale.getDefault());
        date = dateFormat.format(new java.util.Date());

        //Getting current time to save in database
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        time = timeFormat.format(new java.util.Date());



        for(int i=0; i < uniqueEntryIDs.size(); i++){

            Log.d("updatetest", "Value in uniqueEntry array in save: " + uniqueEntryIDs.get(i));



            JournalSingleEntryDataObject journalSingleEntryDataObject = new JournalSingleEntryDataObject(
                    columnNames.get(i), columnTypes.get(i), allEds.get(i).getText().toString(),
                    date, time, fk_ids.get(i));

            journalSingleEntryDataViewModel = ViewModelProviders.of(JournalEntryData.this)
                    .get(JournalSingleEntryDataViewModel.class);

            journalSingleEntryDataObject.setId(uniqueEntryIDs.get(i));


            Log.d("updatetest", "Value of _id in saving: " + uniqueEntryIDs.get(i) );
            Log.d("updatetest", "Value of fk_id in saving: " + fk_ids.get(i) );


            journalSingleEntryDataViewModel.update(journalSingleEntryDataObject);
        }

        finish();
    }


    /*If the user want to delete their entry this function handles it*/
    public void deleteButtonOnClick(View v){

        String table = "SEntry";
        String whereClause = "_id=?";
        String[] whereArgs = new String[] { String.valueOf(mainEntryID) };
        db_write.delete(table, whereClause, whereArgs);

        finish();

    }
}
