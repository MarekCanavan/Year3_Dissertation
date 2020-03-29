package com.example.diss_cbt_application.JournalFeature.JActivities_Fragments.Entries;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.diss_cbt_application.JournalFeature.JDatabase.JDTables.JournalObject;
import com.example.diss_cbt_application.JournalFeature.JDatabase.JDTables.JournalStructureObject;
import com.example.diss_cbt_application.JournalFeature.JDatabase.JDViewModels.JournalStructureViewModel;
import com.example.diss_cbt_application.JournalFeature.JDatabase.JDViewModels.JournalViewModel;
import com.example.diss_cbt_application.JournalFeature.JActivities_Fragments.General.JournalContract;
import com.example.diss_cbt_application.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import yuku.ambilwarna.AmbilWarnaDialog;

import static java.lang.Thread.sleep;

/**This Activity is where the user can create a new journal structure for themselves
 * This will then be saved to the database and they can complete entries with this structure
 * They can also assign a colour to their journal */
public class JournalNewStructure extends AppCompatActivity {

    /*Member Variables for the ScrollView*/
    private ScrollView fieldReGeneration;
    LinearLayout scroll;

    /*Integer member variables*/
    Long tableID;
    int Counter, mDefualtColour;

    /*Arraylist that are needed to store the column type and edit texts to later be persisted to the database*/
    List<String> columnTypes =new ArrayList<String>();
    ArrayList<EditText> allEds = new ArrayList<EditText>();

    /*TextView used to present the colour of the journal chosen by the user*/
    TextView testTextView ;

    /*ViewModel References needed for persisting to the database*/
    private JournalViewModel JournalViewModel;
    private JournalStructureViewModel JournalStructureViewModel;

    /*Shared execution thread is needed for the database persistence (further explanation above function doThingAThenThingB*/
    private Executor sharedSingleThreadExecutor = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal_new_strucure);

        /*Initialise Variables*/
        tableID = 0L;
        Counter = 0;
        initialiseScrollView();
    }


    /**This function creates a new Edit Text Field where the user can assign a name to a column */
    public void newColumnOnClick(View v){

        EditText newColumn = new EditText(this);
        newColumn.setGravity(0);

        allEds.add(newColumn);//add this column to the arraylist of all EditTexts

        /*Creating and Setting the edit texts*/
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

    /**This function creates a new Edit Text Field where the user can assign a name to a percentage field */
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

    /**Button onClick that launches the color picker*/
    public void changeColourOnClick(View v){
        openColorPicker();
    }

    /**This function implements the colorPicker Library which allows the user to choose form a wide range of colours
     * This colour is then displayed to the user via a TextView*/
    public void openColorPicker(){
        AmbilWarnaDialog colorPicker = new AmbilWarnaDialog(this, 0, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onCancel(AmbilWarnaDialog dialog) {

            }
            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {

                final EditText et_name_of_journal = (EditText) findViewById(R.id.et_name_of_journal);
                mDefualtColour = color;
                et_name_of_journal.setTextColor(mDefualtColour);
            }
        });

        colorPicker.show();
    }


    /**When the user chooses to click the 'Save' button, this function is called
     * The JournalName and Structure is saved to the database using function doThingAThenThingB
     * and then a result is set to send back to the previous activity*/
    public void saveStructureOnClick(View v){

        doThingAThenThingB();

        Intent returnIntent = new Intent();
        setResult(2,returnIntent);
        finish();

    }

    /**This function is very important for the persisting of data to the database
     * Room will only do database operations on a background thread as to not block the main thread
     * In order to insert the Journal Structure the id of the Journal needs to be inserted with the Object as a foriegn key
     * This requires retrieving the id from insertion that has just happened
     * A sharedSingleThreadExecutor needs to be setup so that the insertion can be completed, the id saved in tableID
     * and then that tableID can be used in the next thread to insert the Journal Structure Object */
    private void doThingAThenThingB(){

        //Insert the Journal Name into JournalNames
        final EditText et_name_of_journal = (EditText) findViewById(R.id.et_name_of_journal);
        final String name_of_journal = et_name_of_journal.getText().toString();


        /*Thread 1 - insertion of the Journal Object, which returns an ID */
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

        /*Thread 2 - insertion of multiple Journal Structure objects which use the tableID we have just set
        * This thread only executes ones the insertion in Thread 1 is complete*/
        sharedSingleThreadExecutor.execute(new Runnable() {
            @Override
            public void run() {

                for(int i = 0 ; i < allEds.size() ; i++){


                    Log.d("Diss", "Value of table id in second run: " + tableID);
                    JournalStructureObject journalStructure = new JournalStructureObject(allEds.get(i).getText().toString(),
                            columnTypes.get(i).toString(), tableID);

                    JournalStructureViewModel = ViewModelProviders.of(JournalNewStructure.this).get(JournalStructureViewModel.class);
                    JournalStructureViewModel.insert(journalStructure);

                }

            }
        });

    }

    /** Defines the ScrollView and removes views
     * Then defines the LinearLayout 'scroll' to put the TextViews and EditTexts on
     * Set the Orientation Vertical and add to the scrollView*/
    private void initialiseScrollView(){
        fieldReGeneration = (ScrollView) findViewById(R.id.sv_field_generation);
        fieldReGeneration.removeAllViews();
        scroll = new LinearLayout(this);
        scroll.setOrientation(LinearLayout.VERTICAL);
        fieldReGeneration.addView(scroll);
    }

}
