package com.example.diss_cbt_application;

import androidx.appcompat.app.AppCompatActivity;

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

import java.util.ArrayList;
import java.util.List;

public class JournalEntryData extends AppCompatActivity {

    TextView tv_entry_name, tv_journal_name;

    private DatabaseHelper dbHelper = null; //reference to db helper for insertion
    private SQLiteDatabase db_write, db_read;
    private static String st_Text_View = "TextView";
    private static String st_Edit_View = "EditView";
    int mainEntryID = 0;
    int mJournalColour;

    private LinearLayout fieldReGeneration;

    List<String> columnNames =new ArrayList<String>();
    List<String> entryDataList =new ArrayList<String>();
    List<String> columnTypes =new ArrayList<String>();
    ArrayList<EditText> allEds = new ArrayList<EditText>();
    List<Integer> uniqueEntryIDs = new ArrayList<Integer>();

    boolean edit = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal_entry_data);

        /*Setting references to the database*/
        dbHelper = new DatabaseHelper(this);
        db_write = dbHelper.getWritableDatabase();
        db_write.execSQL("PRAGMA foreign_keys=ON");
        db_read = dbHelper.getReadableDatabase();

        fieldReGeneration = (LinearLayout) findViewById(R.id.ll_field_generation_entry_data);
        fieldReGeneration.setOrientation(LinearLayout.VERTICAL);


        /*Unpacking the bundle and placing values in varaibles*/
        Bundle entryBundle = getIntent().getExtras();;
        mainEntryID = entryBundle.getInt("_id");
        String mEntryName = entryBundle.getString(JournalContract.ENTRY_NAME);
        String mJournalName = entryBundle.getString(JournalContract.JOURNAL_NAME);
        String mEntryTime = entryBundle.getString(JournalContract.ENTRY_TIME);
        mJournalColour = entryBundle.getInt(JournalContract.JOURNAL_COLOUR);

        Log.d("Diss", "Value of id from bundle: " + mainEntryID);

        //Log.d("Diss", "Value of mEntryName: " + entryBundle.getString("entryName"));
        //Log.d("Diss", "Value of mJournalName: " + entryBundle.getString("journalName"));
        /*Getting reference to the textViews*/
        tv_entry_name = findViewById(R.id.tv_entry_name);
        tv_journal_name = findViewById(R.id.tv_journal_name);

        tv_entry_name.setText(mEntryName);
        tv_journal_name.setText(mJournalName);
        tv_journal_name.setTextColor(mJournalColour);

        createEntryData(mainEntryID, st_Text_View);
    }

    /*dataRepresentation is pararmemter that defines if an EditText will be generated for the data or a TextView
    * In the editButton onClick an If statement determines what value will be set
    * Doing it this way save copying a lot of the same good*/
    public void createEntryData(int entryID, String dataRepresentation ){

        fieldReGeneration.removeAllViews();

        String entryIDString = Integer.toString(entryID);

        Log.d("Diss", "Value of journalIDString: " + entryIDString);

        String selectQuery = "SELECT * FROM " + JournalContract.SENTRY_DATA + " WHERE " + JournalContract.ENTRY_ID +" = " + entryIDString;

        //Log.d("Diss", selectQuery);

        Cursor cursor = db_write.rawQuery(selectQuery, null);

        //Log.d("Diss", "Cursor: " + cursor );

        if(cursor.moveToFirst()) {
            do {

                Log.d("Diss", "In cursor move ");

                /*Take values from the cursor and store in variables for manipulation*/
                int _id = cursor.getInt(0);
                String columnName = cursor.getString(1);
                String columnType = cursor.getString(2);
                String entryData = cursor.getString(3);
                int entryID_ = cursor.getInt(4);


                //Name of the Entry Field
                TextView columnText = new TextView(JournalEntryData.this);

                columnText.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));

                columnText.setText(columnName);
                columnText.setTextSize(25);
                columnText.setTextColor(Color.parseColor("#000000"));


                //Add the new columns just created to the layout
                fieldReGeneration.addView(columnText);


                if(dataRepresentation == st_Text_View){
                    //Text Field for the entry data
                    TextView entryData_ = new TextView(JournalEntryData.this);

                    entryData_.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT));

                    entryData_.setText(entryData);

                    //Add the new columns just created to the layout
                    fieldReGeneration.addView(entryData_);
                }
                else if(dataRepresentation == st_Edit_View){
                    //Text Field for the entry data
                    EditText entryDataView = new EditText(JournalEntryData.this);

                    entryDataView.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT));

                    entryDataView.setText(entryData);

                    allEds.add(entryDataView);

                    //Add the new columns just created to the layout
                    fieldReGeneration.addView(entryDataView);
                }


                //Adding variables to array for later persistance
                entryDataList.add(entryData);
                columnNames.add(columnName);
                columnTypes.add(columnType);
                uniqueEntryIDs.add(_id);


                /*Logs of Cursor Values*/
                //Log.d("Diss", "Value of id: " + _id);
                //Log.d("Diss", "Value of columnName: " + columnName);
                //Log.d("Diss", "Value of columnType: " + columnType);
                // Log.d("Diss", "Value of tableID: " + mainEntryID);
            }while(cursor.moveToNext());
        }
        else{
            Log.d("Diss", "In cursor no move ");
        }
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

        for(int i=0; i < allEds.size(); i++){

            //Set values to be persisted
            ContentValues js_values = new ContentValues();

            js_values.put(JournalContract.COLUMN_NAME, columnNames.get(i)); //columnName
            js_values.put(JournalContract.COLUMN_TYPE, columnTypes.get(i)); //columnType
            js_values.put(JournalContract.ENTRY_DATA, allEds.get(i).getText().toString()); //entryData
            js_values.put(JournalContract.ENTRY_ID, mainEntryID); //mainEntryID
            js_values.put(JournalContract.ENTRY_TIME, System.currentTimeMillis()); //mainEntryID

            Log.d("Diss", "Start time: " + System.currentTimeMillis());

            //insert into database
            db_write.update(JournalContract.SENTRY_DATA, js_values,
                    "_id="+ uniqueEntryIDs.get(i), null);


            //strings[i] = allEds.get(i).getText().toString();
            Log.d("Diss","" + columnTypes.get(i));
            Log.d("Diss", "" + allEds.get(i).getText().toString());

        }
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
