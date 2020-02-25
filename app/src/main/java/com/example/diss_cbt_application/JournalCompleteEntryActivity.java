package com.example.diss_cbt_application;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class JournalCompleteEntryActivity extends AppCompatActivity {


    int  journalID, columnCounter;
    String journalIDString;
    int entryID;
    private LinearLayout fieldReGeneration;
    private DatabaseHelper dbHelper = null; //reference to db helper for insertion
    private SQLiteDatabase db_write, db_read;
    ArrayList<EditText> allEds = new ArrayList<EditText>();
    List<String> columnTypes =new ArrayList<String>();
    List<String> columnNames = new ArrayList<String>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal_complete_entry);

        dbHelper = new DatabaseHelper(this);
        db_write = dbHelper.getWritableDatabase();
        db_read = dbHelper.getReadableDatabase();

        Bundle journalBundle = getIntent().getExtras();
        journalID = journalBundle.getInt("_id");

        fieldReGeneration = (LinearLayout) findViewById(R.id.ll_field_regeneration);
        fieldReGeneration.setOrientation(LinearLayout.VERTICAL);

        columnCounter = 0;

        Log.d("Diss", "Value of journalID" + journalID);

        createForm();
    }

    private void createForm(){

        Log.d("Diss", "In createForm");
        journalIDString = Integer.toString(journalID);

        Log.d("Diss", "Value of journalIDString: " + journalIDString);
        String selectQuery = "SELECT * FROM JournalStructure WHERE tableID = " + journalIDString;
        Log.d("Diss", selectQuery);
        Cursor cursor = db_write.rawQuery(selectQuery, null);
        Log.d("Diss", "Cursor: " + cursor );
        if(cursor.moveToFirst()) {
            do {

                Log.d("Diss", "In cursor move ");

                /*Take values from the cursor and store in variables for manipulation*/
                int _id = cursor.getInt(0);
                String columnName = cursor.getString(1);
                String columnType = cursor.getString(1);
                int tableID = cursor.getInt(0);


                //Name of the Entry Field
                TextView columnText = new TextView(JournalCompleteEntryActivity.this);

                columnText.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));

                columnText.setText(columnName);


                //EditText Field for the entry data
                EditText newColumn = new EditText(JournalCompleteEntryActivity.this);

                newColumn.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));

                //Adding variables to array for later persistance
                allEds.add(newColumn);
                columnTypes.add(columnType);
                columnNames.add(columnName);


                //Add the new columns just created to the layout
                fieldReGeneration.addView(columnText);
                fieldReGeneration.addView(newColumn);


                /*Logs of Cursor Values*/
                Log.d("Diss", "Value of id: " + _id);
                Log.d("Diss", "Value of columnName: " + columnName);
                Log.d("Diss", "Value of columnType: " + columnType);
                Log.d("Diss", "Value of tableID: " + tableID);
            }while(cursor.moveToNext());
        }
        else{
            Log.d("Diss", "In cursor no move ");
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
        String st_entry_name = ed_entry_name.getText().toString();
        ContentValues se_values = new ContentValues();
        se_values.put("entryName", st_entry_name);
        se_values.put("tableID", journalID); //tableID

        db_write.insert("SEntry", null, se_values);



        String selectQuery = "SELECT MAX(_id) as _id FROM SEntry";
        Cursor cursor = db_write.rawQuery(selectQuery, null);
        cursor.moveToFirst();

        Log.d("Diss",  "Cursor Val: " + cursor.getInt(0));

        entryID = cursor.getInt(0);



        for(int i=0; i < allEds.size(); i++){

            //Set values to be persisted
            ContentValues js_values = new ContentValues();

            js_values.put("columnName", columnNames.get(i)); //columnName
            js_values.put("columnType", columnTypes.get(i)); //columnType
            js_values.put("entryData", allEds.get(i).getText().toString()); //entryData
            js_values.put("tableID", entryID); //entryID
            js_values.put("entryTime", System.currentTimeMillis()); //entryID

            Log.d("Diss", "Start time: " + System.currentTimeMillis());

            //insert into database
            db_write.insert("SEntryData", null, js_values);


            //strings[i] = allEds.get(i).getText().toString();
            Log.d("Diss","" + columnTypes.get(i));
            Log.d("Diss", "" + allEds.get(i).getText().toString());

        }


    }
}
