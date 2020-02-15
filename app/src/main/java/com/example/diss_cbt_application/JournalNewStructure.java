package com.example.diss_cbt_application;

import androidx.appcompat.app.AppCompatActivity;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class JournalNewStructure extends AppCompatActivity {

    private Button newColumn, newPercentage;
    private LinearLayout fieldGeneration;
    int Counter;
    private DatabaseHelper dbHelper = null; //reference to db helper for insertion
    private SQLiteDatabase db;
    ArrayList<EditText> allEds = new ArrayList<EditText>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal_new_strucure);

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

        fieldGeneration.addView((text));
        fieldGeneration.addView(newPercentage);
    }

    public void saveStructureOnClick(View v){

        String[] strings = new String[allEds.size()];

        for(int i=0; i < allEds.size(); i++){
            strings[i] = allEds.get(i).getText().toString();
        }



    }

}
