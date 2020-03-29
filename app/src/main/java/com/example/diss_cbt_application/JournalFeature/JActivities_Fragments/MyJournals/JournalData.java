package com.example.diss_cbt_application.JournalFeature.JActivities_Fragments.MyJournals;

import androidx.annotation.ColorRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;

import android.graphics.Color;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.format.DateFormat;
import android.util.Log;

import com.example.diss_cbt_application.JournalFeature.JActivities_Fragments.Entries.JournalEntryData;
import com.example.diss_cbt_application.JournalFeature.JActivities_Fragments.General.JournalContract;
import com.example.diss_cbt_application.JournalFeature.JDatabase.JDTables.JournalSingleEntryDataObject;
import com.example.diss_cbt_application.JournalFeature.JDatabase.JDTables.JournalSingleEntryObject;
import com.example.diss_cbt_application.JournalFeature.JDatabase.JDTables.JournalStructureObject;
import com.example.diss_cbt_application.JournalFeature.JDatabase.JDViewModels.JournalSingleEntryDataViewModel;
import com.example.diss_cbt_application.JournalFeature.JDatabase.JDViewModels.JournalSingleEntryViewModel;
import com.example.diss_cbt_application.JournalFeature.JDatabase.JDViewModels.JournalStructureViewModel;
import com.example.diss_cbt_application.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.lang.reflect.Array;
import java.sql.Date;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class JournalData extends AppCompatActivity {

    Long journalID;
    String journalName, st_numeric_1, st_numeric_2, st_numeric_3, st_numeric_4;
    int journalColour;

    LineChart chart;

    private JournalSingleEntryDataViewModel journalSingleEntryDataViewModel;
    private JournalStructureViewModel journalStructureViewModel;
    private JournalSingleEntryViewModel journalSingleEntryViewModel;

    ArrayList<Long> dateTimes = new ArrayList<Long>();
    ArrayList<Integer> numericList1 = new ArrayList<Integer>();
    ArrayList<Integer> numericList2 = new ArrayList<Integer>();
    ArrayList<Integer> numericList3 = new ArrayList<Integer>();
    ArrayList<Integer> numericList4 = new ArrayList<Integer>();


    List<JournalStructureObject> structureObjects;
    List<JournalSingleEntryObject> singleEntryObjects;
    List<JournalSingleEntryDataObject> singleEntryDataObjects;

    /*Shared execution thread is needed for the database persistence (further explanation above function doThingAThenThingB*/
    private Executor sharedSingleThreadExecutor = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal_data);

        Bundle entryBundle = getIntent().getExtras();

        journalID = entryBundle.getLong(JournalContract._ID);
        journalName = entryBundle.getString(JournalContract.JOURNAL_COLOUR);
        journalColour = entryBundle.getInt(JournalContract.JOURNAL_COLOUR);

        doThingAThenThingB();;

    }


    private void doThingAThenThingB(){


        /*Thread 1 -  */
        sharedSingleThreadExecutor.execute(new Runnable() {
            @Override
            public void run() {


                Log.d("Diss", "IN THREAD ONE");
                journalStructureViewModel = ViewModelProviders.of(JournalData.this)
                        .get(JournalStructureViewModel.class);
                structureObjects = journalStructureViewModel.getStructureWithIDNotLive(journalID);

                for(int i = 0 ; i < structureObjects.size() ; i++){

                    Log.d("Diss", "Value of column type: " + structureObjects.get(i).getColumnType());

                    Log.d("Diss", "Value of column name: " + structureObjects.get(i).getColumnName());

                }



                /*IF Column Type == Percentage1 && TableID == TableID */

                /*This View Model Returns The time Values of Each Journal with this ID
                * TODO: Select a range from the given start and end times*/
                journalSingleEntryViewModel = ViewModelProviders.of(JournalData.this)
                        .get(JournalSingleEntryViewModel.class);

                singleEntryObjects = journalSingleEntryViewModel.getEntriesWithId(journalID);

                for(int i = 0 ; i < singleEntryObjects.size() ; i++){
                    Log.d("Diss", "Value of dateTime: " + singleEntryObjects.get(i).getDateTime());
                    dateTimes.add(singleEntryObjects.get(i).getDateTime());
                }


                journalSingleEntryDataViewModel = ViewModelProviders.of(JournalData.this)
                        .get(JournalSingleEntryDataViewModel.class);
                singleEntryDataObjects = journalSingleEntryDataViewModel.getEntriesWithIDNotLive(journalID);

                Log.d("Diss", "Value of singleEntryDataObject size: " + singleEntryDataObjects.size());
                Log.d("Diss", "Value of JournalID: " + journalID);

                for(int i = 0 ; i < singleEntryDataObjects.size() ; i++) {

                    Log.d("Diss", "IN SINGLE OBJECTS FOR LOOP ");

                    if(singleEntryDataObjects.get(i).getColumnType().equals(JournalContract.PERCENTAGE + 1)){
                        Log.d("Diss", "Value of entryData: " + singleEntryDataObjects.get(i).getEntryData());
                        numericList1.add(Integer.parseInt(singleEntryDataObjects.get(i).getEntryData()));
                        st_numeric_1 = structureObjects.get(0).getColumnName();
                    }
                    else if(singleEntryDataObjects.get(i).getColumnType().equals(JournalContract.PERCENTAGE + 2)){
                        Log.d("Diss", "Value of entryData: " + singleEntryDataObjects.get(i).getEntryData());
                        numericList2.add(Integer.parseInt(singleEntryDataObjects.get(i).getEntryData()));
                        st_numeric_2 = structureObjects.get(1).getColumnName();
                    }
                    else if(singleEntryDataObjects.get(i).getColumnType().equals(JournalContract.PERCENTAGE + 3)){
                        Log.d("Diss", "Value of entryData: " + singleEntryDataObjects.get(i).getEntryData());
                        numericList3.add(Integer.parseInt(singleEntryDataObjects.get(i).getEntryData()));
                        st_numeric_3 = structureObjects.get(2).getColumnName();
                    }
                    else if(singleEntryDataObjects.get(i).getColumnType().equals(JournalContract.PERCENTAGE + 4)){
                        Log.d("Diss", "Value of entryData: " + singleEntryDataObjects.get(i).getEntryData());
                        numericList4.add(Integer.parseInt(singleEntryDataObjects.get(i).getEntryData()));
                        st_numeric_4 = structureObjects.get(3).getColumnName();
                    }
                    else{
                        Log.d("Diss", "Did not go into any of the .equals PERCENTAGE");
                    }
                }


            }
        });

        /*Thread 2 - */
        sharedSingleThreadExecutor.execute(new Runnable() {
            @Override
            public void run() {

                Log.d("Diss", "IN THREAD TWO");

                Log.d("Diss", "In Chart Func");
                GraphView graph = (GraphView) findViewById(R.id.graph);

                DataPoint[] dp = new DataPoint[dateTimes.size()];
                DataPoint[] dp2 = new DataPoint[dateTimes.size()];
                DataPoint[] dp3 = new DataPoint[dateTimes.size()];

                Date firstDate = new Date(System.currentTimeMillis());
                Date lastDate = new Date(System.currentTimeMillis());

                for(int i = 0 ; i < dateTimes.size() ; i++){

                    Date date = new Date(dateTimes.get(i));
                    Log.d("Diss", "In Chart Func For Loop");
                    Log.d("Diss", "Value of dateTime when assigning in array: " + dateTimes.get(i));

                    dp[i] = new DataPoint(date, numericList1.get(i));
                    dp2[i] = new DataPoint(date, numericList2.get(i));
                    dp3[i] = new DataPoint(date, numericList3.get(i));

                    if(i == 0){
                        firstDate = date;
                    }
                    else if(i == dateTimes.size()){
                        lastDate = date;
                    }
                }

                LineGraphSeries<DataPoint> series = new LineGraphSeries<>(dp);
                LineGraphSeries<DataPoint> series2 = new LineGraphSeries<>(dp2);
                LineGraphSeries<DataPoint> series3 = new LineGraphSeries<>(dp3);

                series.setDrawDataPoints(true);
                series.setColor(Color.RED);
                series.setTitle(st_numeric_1);
                graph.addSeries(series);

                series2.setDrawDataPoints(true);
                series2.setColor(Color.GREEN);
                series2.setTitle(st_numeric_2);
                graph.addSeries(series2);

                series3.setDrawDataPoints(true);
                series3.setTitle(st_numeric_3);
                graph.addSeries(series3);


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
        });

    }

}
