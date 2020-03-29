package com.example.diss_cbt_application.JournalFeature.JActivities_Fragments.MyJournals;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;

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
    String journalName;
    int journalColour;

    LineChart chart;

    private JournalSingleEntryDataViewModel journalSingleEntryDataViewModel;
    private JournalStructureViewModel journalStructureViewModel;
    private JournalSingleEntryViewModel journalSingleEntryViewModel;

    ArrayList<Long> dateTimes = new ArrayList<Long>();
    ArrayList<Integer> vals1 = new ArrayList<Integer>();
    ArrayList<Integer> vals2 = new ArrayList<Integer>();

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


                /*journalStructureViewModel = ViewModelProviders.of(JournalData.this)
                        .get(JournalStructureViewModel.class);
                journalStructureViewModel.getStructureWithID(journalID).observe(this, new Observer<List<JournalStructureObject>>() {
                    @Override
                    public void onChanged(List<JournalStructureObject> journalStructureObjects) {

                        for(int i = 0 ; i < journalStructureObjects.size() ; i++){

                            Log.d("Diss", "Value of column type: " + journalStructureObjects.get(i).getColumnType());

                            Log.d("Diss", "Value of column name: " + journalStructureObjects.get(i).getColumnName());

                        }
                    }
                });*/


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
                singleEntryDataObjects = journalSingleEntryDataViewModel.getEntriesWithTIDType(journalID, "percentage");

                for(int i = 0 ; i < singleEntryDataObjects.size() ; i++) {
                    Log.d("Diss", "Value of entryData: " + singleEntryDataObjects.get(i).getEntryData());
                }


            }
        });

        /*Thread 2 - */
        sharedSingleThreadExecutor.execute(new Runnable() {
            @Override
            public void run() {

                Log.d("Diss", "In Chart Func");
                GraphView graph = (GraphView) findViewById(R.id.graph);

                DataPoint[] dp = new DataPoint[dateTimes.size()];

                Date firstDate = new Date(System.currentTimeMillis());
                Date lastDate = new Date(System.currentTimeMillis());

                for(int i = 0 ; i < dateTimes.size() ; i++){

                    Date date = new Date(dateTimes.get(i));
                    Log.d("Diss", "In Chart Func For Loop");
                    Log.d("Diss", "Value of dateTime when assigning in array: " + dateTimes.get(i));

                    Log.d("Diss", "Value of y values when in assinging array " + i*2);
                    dp[i] = new DataPoint(date, i*2);

                    if(i == 0){
                        firstDate = date;
                    }
                    else if(i == dateTimes.size()){
                        lastDate = date;
                    }
                }

                LineGraphSeries<DataPoint> series = new LineGraphSeries<>(dp);
                series.setDrawDataPoints(true);
                graph.addSeries(series);
                graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(getApplicationContext()));
                graph.getGridLabelRenderer().setNumHorizontalLabels(5);
                graph.getGridLabelRenderer().setNumVerticalLabels(6);

                graph.getViewport().setMinX(firstDate.getTime());
                graph.getViewport().setMaxX(lastDate.getTime());
                graph.getViewport().setMinY(0);
                graph.getViewport().setMaxY(10);
                graph.getViewport().setXAxisBoundsManual(true);
                graph.getViewport().setYAxisBoundsManual(true);

                graph.getGridLabelRenderer().setHumanRounding(false);

            }
        });

    }

//    public void arraySplitter(int numericColumns )


}
