package com.example.diss_cbt_application.JournalFeature.JActivities_Fragments.Entries;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.diss_cbt_application.JournalFeature.JDatabase.JDTables.JournalObject;
import com.example.diss_cbt_application.JournalFeature.JDatabase.JDTables.JournalStructureObject;
import com.example.diss_cbt_application.JournalFeature.JDatabase.JDViewModels.JournalStructureViewModel;
import com.example.diss_cbt_application.JournalFeature.JDatabase.JDViewModels.JournalViewModel;
import com.example.diss_cbt_application.JournalFeature.JActivities_Fragments.General.JournalContract;
import com.example.diss_cbt_application.R;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import yuku.ambilwarna.AmbilWarnaDialog;

import static java.lang.Thread.sleep;

/**This Activity is where the user can create a new journal structure for themselves.
 * This will then be saved to the database and they can complete entries with this structure
 * They can also assign a colour to their journal. */
public class JournalNewStructure extends AppCompatActivity {

    /*Member Variables for the ScrollView*/
    private ScrollView fieldReGeneration;
    LinearLayout scroll;
    int counter;

    /*Integer member variables*/
    Long tableID;
    int Counter, mDefualtColour, numericColumn;

    /*Arraylist that are needed to store the column type and edit texts to later be persisted to the database*/
    List<String> columnTypes =new ArrayList<String>();
    ArrayList<EditText> allEds = new ArrayList<EditText>();

    /*ViewModel References needed for persisting to the database*/
    private JournalViewModel JournalViewModel;
    private JournalStructureViewModel JournalStructureViewModel;

    /*Shared execution thread is needed for the database persistence (further explanation above function doThingAThenThingB*/
    private Executor sharedSingleThreadExecutor = Executors.newSingleThreadExecutor();

    /**onCreate is responsible for initialising some of the member variables, inflating the viewing and calling a function
     * that initialises the scroll view.*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal_new_strucure);

        counter = 0;
        /*Initialise Variables*/
        tableID = 0L;
        Counter = 0;
        numericColumn = 0;
        initialiseScrollView();
    }

    /**This function creates a new Edit Text Field where the user can assign a name to a column */
    public void newGeneralOnClick(View v){

        counter++;

        EditText newColumn = new EditText(this);
        newColumn.setPadding(10, 30, 10, 30);
        newColumn.setGravity(0);
        newColumn.setSingleLine(false);
        newColumn.setImeOptions(EditorInfo.IME_ACTION_DONE);
        newColumn.setRawInputType(InputType.TYPE_CLASS_TEXT);

        allEds.add(newColumn);//add this column to the arraylist of all EditTexts

        TextInputLayout textInputLayout = new TextInputLayout(this, null, R.style.Widget_MaterialComponents_TextInputLayout_OutlinedBox);
        LinearLayout.LayoutParams textInputLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        textInputLayout.setLayoutParams(textInputLayoutParams);
        textInputLayout.setBoxBackgroundMode(TextInputLayout.BOX_BACKGROUND_OUTLINE);
        textInputLayout.setBoxCornerRadii(5,5,5,5);
        //textInputLayout.setHint(columnName);
        textInputLayout.setPadding(10, 20, 10, 25);

        /*Creating and Setting the edit texts*/
        TextView text = new TextView(this);

        text.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));

        text.setText("" + counter +". Enter a name for this generic column");
        text.setTextSize(17);
        text.setTextColor(Color.BLACK);
        text.setPadding(10, 30, 10, 30);

        newColumn.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));

        newColumn.setId(Counter);
        columnTypes.add(JournalContract.GENERAL);
        Counter++;

        scroll.addView((text));
        textInputLayout.addView(newColumn);
        scroll.addView(textInputLayout);
    }

    /**This function creates a new Edit Text Field where the user can assign a name to a percentage field */
    public void newNumericOnClick(View v){

        counter++;

        if(numericColumn < 3){

            numericColumn += 1;

            EditText newPercentage = new EditText(JournalNewStructure.this);
            newPercentage.setPadding(10, 30, 10, 30);
            newPercentage.setSingleLine(false);
            newPercentage.setImeOptions(EditorInfo.IME_ACTION_DONE);
            newPercentage.setRawInputType(InputType.TYPE_CLASS_TEXT);

            allEds.add(newPercentage);

            TextInputLayout textInputLayout = new TextInputLayout(this, null, R.style.Widget_MaterialComponents_TextInputLayout_OutlinedBox);
            LinearLayout.LayoutParams textInputLayoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);

            textInputLayout.setLayoutParams(textInputLayoutParams);
            textInputLayout.setBoxBackgroundMode(TextInputLayout.BOX_BACKGROUND_OUTLINE);
            textInputLayout.setBoxCornerRadii(5,5,5,5);
            //textInputLayout.setHint(columnName);
            textInputLayout.setPadding(10, 20, 10, 25);


            TextView text = new TextView(JournalNewStructure.this);

            text.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));

            text.setText("" + counter + ". Enter a name for this numeric column");
            text.setTextSize(17);
            text.setTextColor(Color.BLACK);
            text.setPadding(10, 30, 10, 30);

            newPercentage.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));

            newPercentage.setId(Counter);
            columnTypes.add(JournalContract.NUMERIC + numericColumn);
            Counter++;

            scroll.addView((text));
            textInputLayout.addView(newPercentage);
            scroll.addView(textInputLayout);

        }
        else{
            Toast.makeText(this, "You can only generate 3 numeric columns", Toast.LENGTH_SHORT).show();
        }

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

        saveStructureSharedThreadOfExecution();
        finish();
    }

    /**This function is very important for the persisting of data to the database.
     * Room will only do database operations on a background thread as to not block the main thread
     * In order to insert the Journal Structure the id of the Journal needs to be inserted with the Object as a foriegn key
     * This requires retrieving the id from insertion that has just happened
     * A sharedSingleThreadExecutor needs to be setup so that the insertion can be completed, the id saved in tableID
     * and then that tableID can be used in the next thread to insert the Journal Structure Object */
    private void saveStructureSharedThreadOfExecution(){

        //Insert the Journal Name into JournalNames
        final EditText et_name_of_journal = (EditText) findViewById(R.id.et_name_of_journal);
        final String name_of_journal = et_name_of_journal.getText().toString();

        JournalViewModel = ViewModelProviders.of(JournalNewStructure.this).get(JournalViewModel.class);

        /*Thread 1 - insertion of the Journal Object, which returns an ID */
        sharedSingleThreadExecutor.execute(new Runnable() {
            @Override
            public void run() {
                JournalObject journal = new JournalObject(name_of_journal, mDefualtColour);

                Long id = JournalViewModel.insertNotAsync(journal);

                tableID = id;
            }
        });

        /*Thread 2 - insertion of multiple Journal Structure objects which use the tableID we have just set
        * This thread only executes ones the insertion in Thread 1 is complete*/
        sharedSingleThreadExecutor.execute(new Runnable() {
            @Override
            public void run() {

                JournalStructureViewModel = ViewModelProviders.of(JournalNewStructure.this).get(JournalStructureViewModel.class);

                for(int i = 0 ; i < allEds.size() ; i++){

                 JournalStructureObject journalStructure = new JournalStructureObject(allEds.get(i).getText().toString(),
                            columnTypes.get(i).toString(), tableID);

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

    public int getAllEdsSize(){
        return allEds.size();
    }

}
