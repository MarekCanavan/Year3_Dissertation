package com.example.diss_cbt_application.Utils;

import android.content.Context;
import android.graphics.Color;
import android.text.InputType;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;

import com.example.diss_cbt_application.JournalFeature.JActivities_Fragments.General.JournalContract;
import com.example.diss_cbt_application.JournalFeature.JDatabase.JDTables.JournalSingleEntryDataObject;
import com.example.diss_cbt_application.JournalFeature.JDatabase.JDTables.JournalStructureObject;
import com.example.diss_cbt_application.JournalFeature.JDatabase.JDViewModels.JournalSingleEntryDataViewModel;
import com.example.diss_cbt_application.JournalFeature.JDatabase.JDViewModels.JournalStructureViewModel;
import com.example.diss_cbt_application.R;
import com.google.android.material.textfield.TextInputLayout;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**This class is responsible for populating journal data and entry data onto scroll views.
 * This is a functionality is used a couple times in the codebase so the functionality was extracted out to reduce
 * code repitition. The concept of constructor overloading is used to distinguish which ViewModel which format is needed*/
public class DataPresentation {

    String dataRepresentation;
    Long id;
    LinearLayout ll_scroll;
    int counter;

    /*Defining Arraylists used throughout the class*/
    List<String> columnNames = new ArrayList<>();
    List<String> entryDataList = new ArrayList<>();
    List<String> columnTypes = new ArrayList<>();
    ArrayList<EditText> allEds = new ArrayList<>();
    List<Long> uniqueEntryIDs = new ArrayList<>();
    List<Long> fk_eids = new ArrayList<>();

    public static final String ENTRY_DATA = "entryData";
    public static final String JOURNAL_DATA = "journalData";
    public static final String JOURNAL_STRUCTURE = "JournalStructure";

    /**Constructor which is called when the JournalStructure is to be populated on a View.
     *
     * @param context - context of the activity the data is being placed on
     * @param scroll - the ScrollView the data is being generated on.
     * @param dataRepresentation  - the format the data is to take, either an EditText or TextView
     * @param id  - id is needed to retrieve data from the ViewModel i.e the id of the journal for which the structure is needed.
     * @param journalStructureViewModel - the ViewModel we will be using to access the database and retrieve the data to present on the scroll view*/
    public DataPresentation(final Context context, final ScrollView scroll, final String dataRepresentation, Long id,
                            JournalStructureViewModel journalStructureViewModel) {

        this.dataRepresentation = dataRepresentation;
        this.id = id;

        runJournalStructure(context, scroll, journalStructureViewModel);
    }

    /**Constructor which is called when the EntryData is to be populated on a View.
     *
     * @param context - context of the activity the data is being placed on
     * @param scroll - the ScrollView the data is being generated on.
     * @param dataRepresentation  - the format the data is to take, either an EditText or TextView
     * @param id  - id is needed to retrieve data from the ViewModel i.e the id of the entry for which the data is needed.
     * @param journalSingleEntryDataViewModel - the ViewModel we will be using to access the database and retrieve the data to present on the scroll view*/
    public DataPresentation(final Context context, final ScrollView scroll, final String dataRepresentation, Long id,
                            JournalSingleEntryDataViewModel journalSingleEntryDataViewModel) {

        this.dataRepresentation = dataRepresentation;
        this.id = id;

        runEntryData(context, scroll, journalSingleEntryDataViewModel);
    }

    /**We need to retrieve the entry data that will be populated on to the ScrollView. This function uses the ViewModel passed in the constructor
     * to retrieve that data and then calls a separate function to populate the data on the view.
     *
     * @param context - context of the activity the data is being placed on
     * @param scroll - the ScrollView the data is being generated on.
     * @param journalSingleEntryDataViewModel - the ViewModel we will be using to access the database and retrieve the data to populate on the view
     * */
    public void runEntryData(final Context context, final ScrollView scroll, JournalSingleEntryDataViewModel journalSingleEntryDataViewModel){

        counter = 1;

        journalSingleEntryDataViewModel.getEntryDataWithId(id).observe((LifecycleOwner) context, new Observer<List<JournalSingleEntryDataObject>>() {
            @Override
            public void onChanged(List<JournalSingleEntryDataObject> journalSingleEntryDataObjects) {

                initialiseScrollView(scroll, context);//function called to initialise the scroll view

                /*For loop iterates through all of the journalSingleEntryDataObjects and populates the activity with the data from the Objects*/
                for(int i = 0 ; i < journalSingleEntryDataObjects.size() ; i++){

                    Log.d("Diss", "In cursor move ");

                    /*Take values from the Objects and store in variables for manipulation and display*/
                    Long _id =  journalSingleEntryDataObjects.get(i).getId();
                    String columnName = journalSingleEntryDataObjects.get(i).getColumnName();
                    String columnType = journalSingleEntryDataObjects.get(i).getColumnType();
                    String entryData = journalSingleEntryDataObjects.get(i).getEntryData();
                    Long entryID_ = journalSingleEntryDataObjects.get(i).getFk_eid();

                    createFields(context, scroll, _id, dataRepresentation,
                            columnName, columnType, entryData, entryID_, ENTRY_DATA);

                }
            }
        });
    }

    /**We need to retrieve the structure that will be populated on to the ScrollView. This function uses the ViewModel passed in the constructor
     * to retrieve that data and then calls a separate function to populate the data on the view.
     *
     * @param context - context of the activity the data is being placed on
     * @param scroll - the ScrollView the data is being generated on.
     * @param journalStructureViewModel - the ViewModel we will be using to access the database and retrieve the data to populate on the view
     * */
    public void runJournalStructure(final Context context, final ScrollView scroll, JournalStructureViewModel journalStructureViewModel ){

        counter = 1;

        journalStructureViewModel.getStructureWithID(id).observe((LifecycleOwner) context, new Observer<List<JournalStructureObject>>() {
            @Override
            public void onChanged(List<JournalStructureObject> journalStructureObjects) {

                initialiseScrollView(scroll, context);//function called to initialise the scroll view

                for(int i = 0 ; i < journalStructureObjects.size() ; i++){

                    Log.d("Diss", "In cursor move ");

                    /*Take values from the Objects and store in variables for manipulation and display*/
                    Long _id =  journalStructureObjects.get(i).getId();
                    String columnName = JOURNAL_STRUCTURE;
                    String columnType = journalStructureObjects.get(i).getColumnType();
                    String entryData = journalStructureObjects.get(i).getColumnName();
                    Long fk_id = journalStructureObjects.get(i).getFk_id();

                    createFields(context, scroll, _id, dataRepresentation,
                            columnName, columnType, entryData, fk_id, JOURNAL_DATA);
                }
            }
        });
    }

    /**This function is responsible for generating the TextFields onto the view.
     *
     * @param context - context of the activity the data is being placed on
     * @param scroll - the ScrollView the data is being generated on.
     * @param id - id of the object being placed on the view
     * @param dataRepresentation - differntiates which field we want generated on the View i.e. TextView or EditText
     * @param columnName - name of the column being generated
     * @param columnType - type of the column being generated i.e. numeric or generic
     * @param entryData - user data that is being generated on to the view
     * @param entryID_ - id of the entry data
     * @param dataType - type the data is in
     * */
    public void createFields(Context context, ScrollView scroll, Long id, String dataRepresentation,
                               String columnName, String columnType, String entryData, Long entryID_, String dataType){

        if(!columnName.equals(JOURNAL_STRUCTURE)){
            //Name of the Entry Field
            TextView columnText = new TextView(context);
            columnText.setText(Integer.toString(counter) + ". " + columnName);
            columnText.setTextSize(25);
            columnText.setTextColor(Color.parseColor("#000000"));

            ll_scroll.addView(columnText);//Add to LinearLayout on ScrollView
        }

        Log.d("DataPresentation", "Value of DataRepresenation: " + dataRepresentation);

        /*This if statements checks what string was parsed into the function
         * If TEXT_VIEW was parse display the entry data in a TextView
         * If EDIT_VIEW was parsed display the entry data in an EditText so the user can update their entry*/
        if(dataRepresentation == JournalContract.TEXT_VIEW){
            Log.d("Diss", "In the if equals text View");
            //Text Field for the entry data
            TextView entryData_ = new TextView(context);

            entryData_.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));

            if(dataType.equals(JOURNAL_DATA)){
                entryData_.setText(Integer.toString(counter) + ". " + entryData);
            }
            else{
                entryData_.setText(entryData);
            }

            entryData_.setTextColor(Color.BLACK);
            entryData_.setTextSize(25);
            entryData_.setPadding(10, 25, 10, 55);

            ll_scroll.addView(entryData_);//Add to LinearLayout on ScrollView

            uniqueEntryIDs.add(id);
            fk_eids.add(entryID_);

        }
        else if(dataRepresentation == JournalContract.EDIT_VIEW){
            //Text Field for the entry data
            EditText entryDataView = new EditText(context);

            entryDataView.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));

            TextInputLayout textInputLayout = new TextInputLayout(context, null, R.style.Widget_MaterialComponents_TextInputLayout_OutlinedBox);
            LinearLayout.LayoutParams textInputLayoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);

            textInputLayout.setLayoutParams(textInputLayoutParams);
            textInputLayout.setBoxBackgroundMode(TextInputLayout.BOX_BACKGROUND_OUTLINE);
            textInputLayout.setBoxCornerRadii(5,5,5,5);
            //textInputLayout.setHint(columnName);
            textInputLayout.setPadding(10, 20, 10, 25);

            entryDataView.setText(entryData);
            entryDataView.setTextSize(25);
            entryDataView.setPadding(10, 25, 10, 25);
            entryDataView.setSingleLine(false);
            entryDataView.setImeOptions(EditorInfo.IME_ACTION_DONE);
            entryDataView.setRawInputType(InputType.TYPE_CLASS_TEXT);

            allEds.add(entryDataView);//Adding variables to array for later persistence to the database

            textInputLayout.addView(entryDataView);
            ll_scroll.addView(textInputLayout);//Add to LinearLayout on ScrollView
        }

        //Adding variables to array for later persistence to the database
        entryDataList.add(entryData);
        columnNames.add(columnName);
        columnTypes.add(columnType);
        counter++;
    }

    /**Function initialises the scroll view we will be generating the data on
     *
     * @param scroll - ScrollView the data will be generated on
     * @param context - the context of the activity the ScrollView is being set on */
    public void initialiseScrollView(ScrollView scroll, Context context){
        scroll.removeAllViews();
        ll_scroll = new LinearLayout(context);
        ll_scroll.setOrientation(LinearLayout.VERTICAL);
        scroll.addView(ll_scroll);
    }

    public void setDataRepresentation(String dataRepresentation){
        this.dataRepresentation = dataRepresentation;
    }

    public List<Long> getFk_eids(){
        return fk_eids;
    }

    public List<Long> getUniqueEntryIDs(){
        return uniqueEntryIDs;
    }

    public List<String> getColumnNames(){
        return columnNames;
    }

    public List<String> getColumnTypes(){
        return columnTypes;
    }

    public ArrayList<EditText> getAllEds(){
        return allEds;
    }

}
