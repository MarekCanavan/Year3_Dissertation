package com.example.diss_cbt_application;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.ViewGroup;
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

import java.util.ArrayList;
import java.util.List;

public class DataPresentation {

    String dataRepresentation;
    Long id;
    LinearLayout ll_scroll;
    int counter;
    public static final String JOURNAL_STRUCTURE = "JournalStructure";

    /*Defining Arraylists used throughout the class*/
    List<String> columnNames = new ArrayList<>();
    List<String> entryDataList = new ArrayList<>();
    List<String> columnTypes = new ArrayList<>();
    ArrayList<EditText> allEds = new ArrayList<>();
    List<Long> uniqueEntryIDs = new ArrayList<>();
    List<Long> fk_eids = new ArrayList<>();

    public static final String JOURNAL_DATA = "journalData";
    public static final String ENTRY_DATA = "entryData";


    public DataPresentation(final Context context, final ScrollView scroll, final String dataRepresentation, Long id,
                            JournalStructureViewModel journalStructureViewModel) {
        this.dataRepresentation = dataRepresentation;
        this.id = id;



        runJournalStructure(context, scroll, journalStructureViewModel);

    }

    public DataPresentation(final Context context, final ScrollView scroll, final String dataRepresentation, Long id,
                            JournalSingleEntryDataViewModel journalSingleEntryDataViewModel) {

        this.dataRepresentation = dataRepresentation;
        this.id = id;

        runEntryData(context, scroll, journalSingleEntryDataViewModel);
    }

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
            entryData_.setPadding(10, 25, 10, 25);

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

            entryDataView.setText(entryData);
            entryDataView.setTextSize(25);
            entryDataView.setPadding(10, 25, 10, 25);

            allEds.add(entryDataView);//Adding variables to array for later persistence to the database

            ll_scroll.addView(entryDataView);//Add to LinearLayout on ScrollView
        }

        //Adding variables to array for later persistence to the database
        entryDataList.add(entryData);
        columnNames.add(columnName);
        columnTypes.add(columnType);
        counter++;

    }

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
