package com.example.diss_cbt_application.JournalFeature.JActivities_Fragments.MyJournals;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProviders;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.diss_cbt_application.Utils.DataPresentation;
import com.example.diss_cbt_application.Utils.DatePickerFrag;
import com.example.diss_cbt_application.JournalFeature.JActivities_Fragments.General.JournalContract;
import com.example.diss_cbt_application.JournalFeature.JDatabase.JDTables.JournalObject;
import com.example.diss_cbt_application.JournalFeature.JDatabase.JDTables.JournalSingleEntryDataObject;
import com.example.diss_cbt_application.JournalFeature.JDatabase.JDTables.JournalSingleEntryObject;
import com.example.diss_cbt_application.JournalFeature.JDatabase.JDTables.JournalStructureObject;
import com.example.diss_cbt_application.JournalFeature.JDatabase.JDViewModels.JournalSingleEntryDataViewModel;
import com.example.diss_cbt_application.JournalFeature.JDatabase.JDViewModels.JournalSingleEntryViewModel;
import com.example.diss_cbt_application.JournalFeature.JDatabase.JDViewModels.JournalStructureViewModel;
import com.example.diss_cbt_application.JournalFeature.JDatabase.JDViewModels.JournalViewModel;
import com.example.diss_cbt_application.R;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * The user is shown this Activity after choosing to View a specific journal.
 * This Activity is a skeleton to represent any journals to the user.
 * The user has the option of editing/updating their journal as well as deleting it.
 * The activity also presents the users numeric data they have entered into all journal entries on a line graph.
 * All of these options and changes the user makes will be sent to the database which will be updated appropriately.*/
public class JournalData extends AppCompatActivity {

    /*Member variables used throughout the class*/
    Long journalID;
    String journalName, st_numeric_1, st_numeric_2, st_numeric_3;
    int journalColour;
    TextView tvJournalName, tv_num_entries;
    EditText et_journal_name;
    boolean edit = true;
    ScrollView scroll;
    DataPresentation dataPresentationObj;

    /*ViewModels used throughout the class*/
    private JournalSingleEntryDataViewModel journalSingleEntryDataViewModel;
    private JournalStructureViewModel journalStructureViewModel;
    private JournalSingleEntryViewModel journalSingleEntryViewModel;
    private JournalViewModel journalViewModel;

    /*Defining Arraylists used throughout the class*/
    ArrayList<Long> dateTimes = new ArrayList<Long>();
    ArrayList<Integer> numericList1 = new ArrayList<Integer>();
    ArrayList<Integer> numericList2 = new ArrayList<Integer>();
    ArrayList<Integer> numericList3 = new ArrayList<Integer>();
    List<String> columnTypes =new ArrayList<>();
    ArrayList<EditText> allEds = new ArrayList<>();
    List<Long> uniqueEntryIDs = new ArrayList<>();
    List<Long> uniqueFKIDs = new ArrayList<>();
    List<JournalStructureObject> structureObjects;
    List<JournalSingleEntryObject> singleEntryObjects;
    List<JournalSingleEntryDataObject> singleEntryDataObjects;

    /*Shared execution thread is needed for the database persistence (further explanation above function doThingAThenThingB*/
    private Executor sharedSingleThreadExecutor = Executors.newSingleThreadExecutor();

    /**onCreate is the first function that is called when this activity is loaded. It calls 2 functions, the first of which sets the view
     * and the second retrieves the users numeric data and presents it on a line graph.*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal_data);

        setVarsAndFields();
        displayGraphandData();;

    }

    /*This function unpacks the bundle which has the Journal Name and Colour information.
    * As well as creating an instance of DataPresentation to place the journal column names on to a ScrollView**/
    public void setVarsAndFields(){
        Bundle entryBundle = getIntent().getExtras();

        journalID = entryBundle.getLong(JournalContract._ID);
        journalName = entryBundle.getString(JournalContract.JOURNAL_NAME);
        journalColour = entryBundle.getInt(JournalContract.JOURNAL_COLOUR);

        Log.d("Diss", "Journal name: " + journalName);

        journalViewModel = ViewModelProviders.of(JournalData.this)
                .get(JournalViewModel.class);

        tvJournalName = findViewById(R.id.tv_jd_journal_name);
        tvJournalName.setText(journalName);
        tvJournalName.setTextColor(journalColour);

        scroll = findViewById(R.id.sv_jd_field_generation_entry_data);
        journalStructureViewModel = ViewModelProviders.of(JournalData.this)
                .get(JournalStructureViewModel.class);

        dataPresentationObj = new DataPresentation(this, scroll, JournalContract.TEXT_VIEW,
                journalID, journalStructureViewModel);

        tv_num_entries = findViewById(R.id.tv_jd_num_entries);

    }

    /**This function utilises the shared thread of execution to display the users numeric data for a journal entry on a graph.
     * The first thread retrieves the data that and the second thread maps it on to a graph using the GraphView library.*/
    private void displayGraphandData(){

        /*Thread 1 -  */
        sharedSingleThreadExecutor.execute(new Runnable() {
            @Override
            public void run() {

                st_numeric_1 = "fgabwgeha";

                Log.d("Diss", "IN THREAD ONE");
                structureObjects = journalStructureViewModel.getStructureWithIDNotLive(journalID);

                for(int i = 0 ; i < structureObjects.size() ; i++){

                    if(structureObjects.get(i).getColumnType().equals(JournalContract.NUMERIC + 1)){
                        st_numeric_1 = structureObjects.get(i).getColumnName();
                    }
                    if(structureObjects.get(i).getColumnType().equals(JournalContract.NUMERIC + 2)){
                        st_numeric_2 = structureObjects.get(i).getColumnName();
                    }
                    if(structureObjects.get(i).getColumnType().equals(JournalContract.NUMERIC + 3)){
                        st_numeric_3 = structureObjects.get(i).getColumnName();
                    }
                }

                /*This View Model Returns The time Values of Each Journal with this ID*/
                journalSingleEntryViewModel = ViewModelProviders.of(JournalData.this)
                        .get(JournalSingleEntryViewModel.class);

                singleEntryObjects = journalSingleEntryViewModel.getEntriesWithId(journalID);

                tv_num_entries.setText("" + singleEntryObjects.size());

                for(int i = 0 ; i < singleEntryObjects.size() ; i++){
                    dateTimes.add(singleEntryObjects.get(i).getDateTime());
                }

                journalSingleEntryDataViewModel = ViewModelProviders.of(JournalData.this)
                        .get(JournalSingleEntryDataViewModel.class);
                singleEntryDataObjects = journalSingleEntryDataViewModel.getEntriesWithIDNotLive(journalID);

                for(int i = 0 ; i < singleEntryDataObjects.size() ; i++) {

                    if(singleEntryDataObjects.get(i).getColumnType().equals(JournalContract.NUMERIC + 1)){
                        numericList1.add(Integer.parseInt(singleEntryDataObjects.get(i).getEntryData()));
                    }
                    else if(singleEntryDataObjects.get(i).getColumnType().equals(JournalContract.NUMERIC + 2)){
                        numericList2.add(Integer.parseInt(singleEntryDataObjects.get(i).getEntryData()));
                    }
                    else if(singleEntryDataObjects.get(i).getColumnType().equals(JournalContract.NUMERIC + 3)){
                        numericList3.add(Integer.parseInt(singleEntryDataObjects.get(i).getEntryData()));
                    }
                    else{
                        Log.d("Diss", "Did not go into any of the .equals PERCENTAGE");
                    }
                }

                //tv_num_entries.setText(singleEntryDataObjects.size());

            }
        });

        /*Thread 2 - */
        sharedSingleThreadExecutor.execute(new Runnable() {
            @Override
            public void run() {

                if(!st_numeric_1.equals("fgabwgeha")){
                    GraphView graph = (GraphView) findViewById(R.id.graph);

                    DataPoint[] dp = new DataPoint[dateTimes.size()];
                    DataPoint[] dp2 = new DataPoint[dateTimes.size()];
                    DataPoint[] dp3 = new DataPoint[dateTimes.size()];

                    Date firstDate = new Date(System.currentTimeMillis());
                    Date lastDate = new Date(System.currentTimeMillis());

                    for(int i = 0 ; i < dateTimes.size() ; i++){

                        Date date = new Date(dateTimes.get(i));

                        if(numericList1.size() != 0){
                            dp[i] = new DataPoint(date, numericList1.get(i));
                        }
                        if(numericList2.size() != 0){
                            dp2[i] = new DataPoint(date, numericList2.get(i));
                        }
                        if(numericList3.size() != 0){
                            dp3[i] = new DataPoint(date, numericList3.get(i));
                        }

                        if(i == 0){
                            firstDate = date;
                        }
                        else if(i == dateTimes.size()){
                            lastDate = date;
                        }
                    }

                    if(dp[0] != null){
                        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(dp);
                        series.setDrawDataPoints(true);
                        series.setColor(Color.RED);
                        series.setTitle(st_numeric_1);
                        graph.addSeries(series);
                    }
                    if(dp2[0] != null){
                        LineGraphSeries<DataPoint> series2 = new LineGraphSeries<>(dp2);
                        series2.setDrawDataPoints(true);
                        series2.setColor(Color.GREEN);
                        series2.setTitle(st_numeric_2);
                        graph.addSeries(series2);
                    }
                    if(dp3[0] != null){
                        LineGraphSeries<DataPoint> series3 = new LineGraphSeries<>(dp3);
                        series3.setDrawDataPoints(true);
                        series3.setTitle(st_numeric_3);
                        graph.addSeries(series3);
                    }

                    graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(getApplicationContext()));
                    graph.getGridLabelRenderer().setNumVerticalLabels(6);

                    graph.getViewport().setMinX(firstDate.getTime());
                    graph.getViewport().setMaxX(lastDate.getTime());
                    graph.getViewport().setMinY(0);
                    graph.getViewport().setMaxY(10);
                    graph.getViewport().setXAxisBoundsManual(true);
                    graph.getViewport().setYAxisBoundsManual(true);

                    graph.getLegendRenderer().setVisible(true);
                    graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
                    graph.getGridLabelRenderer().setHumanRounding(false);

                }
                else{
                    Log.d("Dissss", "No Numeric Columns");
                }


            }
        });
    }

    /**If the user wants to edit their journal this function handles it.
     * Boolean Value for edit or save, once in edit mode anything they change will be saved*/
    public void editButtonOnClick(View v){

        Button bt_save_edit = findViewById(R.id.bt_journal_data_edit);

        if(edit){//true

            tvJournalName.setVisibility(View.INVISIBLE);

            et_journal_name = findViewById(R.id.ed_jd_journal_name);
            et_journal_name.setVisibility(View.VISIBLE);
            et_journal_name.setText(journalName);
            et_journal_name.setTextColor(journalColour);
            et_journal_name.setImeOptions(EditorInfo.IME_ACTION_DONE);
            et_journal_name.setRawInputType(InputType.TYPE_CLASS_TEXT);

            dataPresentationObj.setDataRepresentation(JournalContract.EDIT_VIEW);
            dataPresentationObj.runJournalStructure(this, scroll, journalStructureViewModel);

            bt_save_edit.setText("Save");
            edit = false;
        }
        else{//false

            saveJournalName();
            saveJournalStructure();
            finish();
        }
    }

    /**this function saves the name of the journal when the user chooses to save the journal structure.
     * The new name is retrieved form the EditText and this journal object is updated in the database.*/
    public void saveJournalName(){

        JournalObject journalObject = new JournalObject(et_journal_name.getText().toString(),
                journalColour);

        journalObject.setId(journalID);
        journalViewModel.update(journalObject);
    }

    /** Called when the user chooses to 'Save' the update they made to their journal.
     * Updates this journal with the new information the user has entered in the EditTexts.**/
    public void saveJournalStructure(){

        uniqueFKIDs = dataPresentationObj.getFk_eids();
        uniqueEntryIDs = dataPresentationObj.getUniqueEntryIDs();
        columnTypes = dataPresentationObj.getColumnTypes();
        allEds = dataPresentationObj.getAllEds();

        /*Iterates through all of the objects and updates the fields with the changes the user has made
         * the 'uniqueEntryIDs array is very important as it ensures the correct fields in the database are being updated*/
        for(int i=0; i < uniqueEntryIDs.size(); i++){

            EditText et_temp = allEds.get(i);

            JournalStructureObject journalStructureObject = new JournalStructureObject(et_temp.getText().toString(),
                    columnTypes.get(i), uniqueFKIDs.get(i));

            journalStructureObject.setId(uniqueEntryIDs.get(i));

            journalStructureViewModel.update(journalStructureObject);
        }
    }

    /*If the user want to delete their entry this function handles. first checking they actually want to delete the journal
    * by presenting a dialog box*/
    public void deleteButtonOnClick(View v){
        AlertDialog diaBox = AskOption();
        diaBox.show();
    }

    /**This is the shared thread of execution that is used to delete the journal.
     * the first thread retrieves the object (this journal) we want to delete. Then the second thread uses
     * this object to delete the journal.*/
    private Executor deleteJournalSharedSingleThreadExecutor = Executors.newSingleThreadExecutor();
    JournalObject journalObjectForDeletion;
    private void deleteJournalSharedThreadExecution(){

        /*To Delete First You Need the Object */
        /*Thread 1  */
        deleteJournalSharedSingleThreadExecutor.execute(new Runnable() {
            @Override
            public void run() {
                journalObjectForDeletion = journalViewModel.getEntryWithId(journalID);
            }
        });

        /*Thread 2 -*/
        deleteJournalSharedSingleThreadExecutor.execute(new Runnable() {
            @Override
            public void run() {
                journalViewModel.delete(journalObjectForDeletion);
            }
        });
    }

    /**This AlertDialog box is created when the user indicates they want to delete a journal. The check is done
     * to confirm they want to delete that journal and make them aware of the consequences of deleting the journal.
     * So 2 options are generated, if the user chooses to cancel then the dialog box is dismissed.
     * If the user confirms they want to delete then the logic is triggered to delete the journal.
     **/
    private AlertDialog AskOption(){
        AlertDialog myDeletingDialogBox = new AlertDialog.Builder(this)
                .setTitle("Delete")
                .setMessage("Are you sure you want to delete? This will also delete all of the entries you have made for this Journal and there wil be no way to retrieve them")
                .setIcon(R.drawable.ic_delete_black)
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteJournalSharedThreadExecution();
                        finish();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();

        return myDeletingDialogBox;
    }

}
