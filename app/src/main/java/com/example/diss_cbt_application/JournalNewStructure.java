package com.example.diss_cbt_application;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class JournalNewStructure extends AppCompatActivity {

    private Button newColumn, newPercentage;
    private LinearLayout fieldGeneration;
    int Counter, tableID;
    private DatabaseHelper dbHelper = null; //reference to db helper for insertion
    private SQLiteDatabase db_write, db_read;
    ArrayList<EditText> allEds = new ArrayList<EditText>();
    List<String> columnTypes =new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal_new_strucure);

        dbHelper = new DatabaseHelper(this);
        db_write = dbHelper.getWritableDatabase();
        db_read = dbHelper.getReadableDatabase();
        
        tableID = 0;

        fieldGeneration = (LinearLayout) findViewById(R.id.ll_field_generation);
        fieldGeneration.setOrientation(LinearLayout.VERTICAL);
        Counter = 0;
    }

    public void newColumnOnClick(View v){

        EditText newColumn = new EditText(JournalNewStructure.this);
        allEds.add(newColumn);

        TextView text = new TextView(JournalNewStructure.this);

        text.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));

        text.setText("Enter a name for wordy column");

        newColumn.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));


        newColumn.setId(Counter);
        columnTypes.add("column");
        Counter++;

        fieldGeneration.addView((text));
        fieldGeneration.addView(newColumn);

    }

    public void newPercentageOnClick(View v){

        EditText newPercentage = new EditText(JournalNewStructure.this);
        allEds.add(newPercentage);

        TextView text = new TextView(JournalNewStructure.this);

        text.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));

        text.setText("Enter a name for percentage column");

        newPercentage.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));

        newPercentage.setId(Counter);
        columnTypes.add("percentage");
        Counter++;

        fieldGeneration.addView((text));
        fieldGeneration.addView(newPercentage);
    }

    public void saveStructureOnClick(View v){

        //Insert the Journal Name into JournalNames

        EditText et_name_of_journal = (EditText) findViewById(R.id.et_name_of_journal);
        String name_of_journal = et_name_of_journal.getText().toString();
        ContentValues jn_values = new ContentValues();

        Log.d("Diss", name_of_journal);
        jn_values.put("journalName", name_of_journal);

        db_write.insert("JournalNames", null, jn_values);

        //Query the Journal database to findout the highest value in it
        //This will be the value we have just inserted
        //Save the value to then store with the JournalStructure
        Log.d("Diss", "test 2" );

        String selectQuery = "SELECT MAX(_id) as _id FROM JournalNames";
        Cursor cursor = db_write.rawQuery(selectQuery, null);

        cursor.moveToFirst();

        Log.d("Diss",  "Cursor Val: " + cursor.getInt(0));

        tableID = cursor.getInt(0);

        //cursor.moveToNext();

        //Log.d("Diss",  "Cursor Val: " + cursor.getInt(0));

        Log.d("Diss", "test 3" );
        String[] strings = new String[allEds.size()];

        for(int i=0; i < allEds.size(); i++){

            ContentValues js_values = new ContentValues();
            js_values.put("columnName",allEds.get(i).getText().toString());
            js_values.put("columnType",columnTypes.get(i));
            js_values.put("tableID", tableID);

            db_write.insert("JournalStructure", null, js_values);

            strings[i] = allEds.get(i).getText().toString();
            Log.d("Diss","" + columnTypes.get(i));
            Log.d("Diss", "" + allEds.get(i).getText().toString());

        }

    }

}
