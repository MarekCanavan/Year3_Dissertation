package com.example.diss_cbt_application.JournalFeature.JActivities_Fragments;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.content.ContentValues;
import android.content.Intent;
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
import com.example.diss_cbt_application.JournalFeature.JDatabase.JDTables.JournalObject;
import com.example.diss_cbt_application.JournalFeature.JDatabase.JDTables.JournalStructureObject;
import com.example.diss_cbt_application.JournalFeature.JDatabase.JDViewModels.JournalViewModel;
import com.example.diss_cbt_application.JournalFeature.JournalContract;
import com.example.diss_cbt_application.R;

import java.util.ArrayList;
import java.util.List;

import yuku.ambilwarna.AmbilWarnaDialog;

public class JournalNewStructure extends AppCompatActivity {

    private Button newColumn, newPercentage;
    private ScrollView fieldReGeneration;
    LinearLayout scroll;
    int Counter;
    long tableID;
    private DatabaseHelper dbHelper = null; //reference to db helper for insertion
    private SQLiteDatabase db_write, db_read;
    ArrayList<EditText> allEds = new ArrayList<EditText>();
    List<String> columnTypes =new ArrayList<String>();

    TextView testTextView ;
    int mDefualtColour;

    JournalViewModel journalViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal_new_strucure);

        dbHelper = new DatabaseHelper(this);
        db_write = dbHelper.getWritableDatabase();
        db_read = dbHelper.getReadableDatabase();

        tableID = 0L;


        /*Very important code
         * Defines the ScrollView and removes views
         * Then defines the LinearLayout 'scroll' to put the TextViews and EditTexts on
         * Set the Orientation Vertical and add to the scrollView*/
        fieldReGeneration = (ScrollView) findViewById(R.id.sv_field_generation);
        fieldReGeneration.removeAllViews();
        scroll = new LinearLayout(this);
        scroll.setOrientation(LinearLayout.VERTICAL);
        fieldReGeneration.addView(scroll);

        Counter = 0;
    }


    public void newColumnOnClick(View v){

        EditText newColumn = new EditText(this);
        newColumn.setGravity(0);

        allEds.add(newColumn);

        TextView text = new TextView(this);

        text.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));

        text.setText("Enter a name for wordy column");
        text.setTextSize(17);
        text.setTextColor(Color.BLACK);

        newColumn.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));


        newColumn.setId(Counter);
        columnTypes.add(JournalContract.COLUMN);
        Counter++;

        scroll.addView((text));
        scroll.addView(newColumn);

    }

    public void newPercentageOnClick(View v){

        EditText newPercentage = new EditText(JournalNewStructure.this);
        allEds.add(newPercentage);

        TextView text = new TextView(JournalNewStructure.this);

        text.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));

        text.setText("Enter a name for percentage column");
        text.setTextSize(17);
        text.setTextColor(Color.BLACK);

        newPercentage.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));

        newPercentage.setId(Counter);
        columnTypes.add(JournalContract.PERCENTAGE);
        Counter++;

        scroll.addView((text));
        scroll.addView(newPercentage);
    }

    public void changeColourOnClick(View v){
        testTextView = findViewById(R.id.tv_test_text);
        openColorPicker();
    }

    public void openColorPicker(){
        AmbilWarnaDialog colorPicker = new AmbilWarnaDialog(this, 0, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onCancel(AmbilWarnaDialog dialog) {

            }

            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {

                mDefualtColour = color;
                testTextView.setTextColor(mDefualtColour);
            }
        });

        colorPicker.show();
    }

    public void saveStructureOnClick(View v){

        //Insert the Journal Name into JournalNames
        EditText et_name_of_journal = (EditText) findViewById(R.id.et_name_of_journal);
        String name_of_journal = et_name_of_journal.getText().toString();
        ContentValues jn_values = new ContentValues();


        //Log.d("Diss", "Value of colour: " + mDefualtColour);
        //Log.d("Diss", name_of_journal);
        jn_values.put(JournalContract.JOURNAL_NAME, name_of_journal);
        jn_values.put(JournalContract.JOURNAL_COLOUR, mDefualtColour);
        jn_values.put(JournalContract.ARCHIVED, 0);

        JournalObject journal = new JournalObject(name_of_journal, mDefualtColour, 0);

        JournalViewModel.insert(journal).observe(this,
                new Observer<Long>() {
                    @Override
                    public void onChanged(Long aLong) {
                        Toast.makeText(JournalNewStructure.this, "ID: " + aLong, Toast.LENGTH_SHORT).show();

                        tableID = aLong;
                    }
                });




        //db_write.insert(JournalContract.JOURNAL_NAMES, null, jn_values);

        /*
        //Query the Journal database to findout the highest value in it
        //This will be the value we have just inserted
        //Save the value to then store with the JournalStructure

        String selectQuery = "SELECT MAX(_id) as _id FROM JournalNames";
        Cursor cursor = db_write.rawQuery(selectQuery, null);
        cursor.moveToFirst();

        Log.d("Diss",  "Cursor Val: " + cursor.getInt(0));

        tableID = cursor.getInt(0);


        //Log.d("Diss",  "Cursor Val: " + cursor.getInt(0));

        Log.d("Diss", "test 3" );
        String[] strings = new String[allEds.size()];

        for(int i=0; i < allEds.size(); i++){

            ContentValues js_values = new ContentValues();
            js_values.put(JournalContract.COLUMN_NAME, allEds.get(i).getText().toString());
            js_values.put(JournalContract.COLUMN_TYPE, columnTypes.get(i));
            js_values.put(JournalContract.TABLE_ID, tableID);

            db_write.insert(JournalContract.JOURNAL_STRUCTURE, null, js_values);

            strings[i] = allEds.get(i).getText().toString();
            Log.d("Diss","" + columnTypes.get(i));
            Log.d("Diss", "" + allEds.get(i).getText().toString());

        }*/

        Intent returnIntent = new Intent();
        setResult(2,returnIntent);
        finish();

    }

}
