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
import com.example.diss_cbt_application.JournalFeature.JDatabase.JDViewModels.JournalStructureViewModel;
import com.example.diss_cbt_application.JournalFeature.JDatabase.JDViewModels.JournalViewModel;
import com.example.diss_cbt_application.JournalFeature.JournalContract;
import com.example.diss_cbt_application.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import yuku.ambilwarna.AmbilWarnaDialog;

import static java.lang.Thread.currentThread;
import static java.lang.Thread.sleep;

public class JournalNewStructure extends AppCompatActivity {

    private Button newColumn, newPercentage;
    private ScrollView fieldReGeneration;
    LinearLayout scroll;
    int Counter;
    Long tableID;

    ArrayList<EditText> allEds = new ArrayList<EditText>();
    List<String> columnTypes =new ArrayList<String>();

    TextView testTextView ;
    int mDefualtColour;
    private JournalStructureViewModel JournalStructureViewModel;
    private JournalViewModel JournalViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal_new_strucure);

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

    private Executor sharedSingleThreadExecutor = Executors.newSingleThreadExecutor();


    public void saveStructureOnClick(View v){

        doThingAThenThingB();


        Log.d("Diss", "Value of table id: AFTER OBSERVER " + tableID);



        Intent returnIntent = new Intent();
        setResult(2,returnIntent);
        finish();

    }

    private void doThingAThenThingB(){

        //Insert the Journal Name into JournalNames
        final EditText et_name_of_journal = (EditText) findViewById(R.id.et_name_of_journal);
        final String name_of_journal = et_name_of_journal.getText().toString();


        sharedSingleThreadExecutor.execute(new Runnable() {
            @Override
            public void run() {
                JournalObject journal = new JournalObject(name_of_journal, mDefualtColour, 0);

                JournalViewModel = ViewModelProviders.of(JournalNewStructure.this).get(JournalViewModel.class);
                Long id = JournalViewModel.insertNotAsync(journal);

                tableID = id;

                Log.d("Diss", "Value of table id in doThingAdoThingb: " + tableID);
            }
        });

        sharedSingleThreadExecutor.execute(new Runnable() {
            @Override
            public void run() {

                for(int i = 0 ; i < allEds.size() ; i++){


                    Log.d("Diss", "Value of table id in second run: " + tableID);
                    JournalStructureObject journalStructure = new JournalStructureObject(allEds.get(i).getText().toString(),
                            columnTypes.get(i).toString(), tableID);

                    JournalStructureViewModel = ViewModelProviders.of(JournalNewStructure.this).get(JournalStructureViewModel.class);
                    JournalStructureViewModel.insert(journalStructure);

                    //Log.d("Diss","Value of Column Types: " + columnTypes.get(i));
                    //Log.d("Diss", "Value of ED's Text: " + allEds.get(i).getText().toString());
                    //Log.d("Diss", "Value of tableID: " + tableID);

                }

            }
        });

    }

}
