package com.example.diss_cbt_application.JournalFeature.JActivities_Fragments.MyJournals;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.util.Log;

import com.example.diss_cbt_application.JournalFeature.JActivities_Fragments.Entries.JournalEntryData;
import com.example.diss_cbt_application.JournalFeature.JActivities_Fragments.General.JournalContract;
import com.example.diss_cbt_application.JournalFeature.JDatabase.JDTables.JournalSingleEntryDataObject;
import com.example.diss_cbt_application.JournalFeature.JDatabase.JDTables.JournalStructureObject;
import com.example.diss_cbt_application.JournalFeature.JDatabase.JDViewModels.JournalSingleEntryDataViewModel;
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
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.List;

public class JournalData extends AppCompatActivity {

    Long journalID;
    String journalName;
    int journalColour;

    LineChart chart;

    private JournalSingleEntryDataViewModel journalSingleEntryDataViewModel;
    private JournalStructureViewModel journalStructureViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal_data);

        Bundle entryBundle = getIntent().getExtras();

        journalID = entryBundle.getLong(JournalContract._ID);
        journalName = entryBundle.getString(JournalContract.JOURNAL_COLOUR);
        journalColour = entryBundle.getInt(JournalContract.JOURNAL_COLOUR);

        journalStructureViewModel = ViewModelProviders.of(JournalData.this)
                .get(JournalStructureViewModel.class);
        journalStructureViewModel.getStructureWithID(journalID).observe(this, new Observer<List<JournalStructureObject>>() {
            @Override
            public void onChanged(List<JournalStructureObject> journalStructureObjects) {

                for(int i = 0 ; i < journalStructureObjects.size() ; i++){

                    Log.d("Diss", "Value of column type: " + journalStructureObjects.get(i).getColumnType());

                    Log.d("Diss", "Value of column name: " + journalStructureObjects.get(i).getColumnName());

                }
            }
        });

        journalSingleEntryDataViewModel = ViewModelProviders.of(JournalData.this)
                .get(JournalSingleEntryDataViewModel.class);
        journalSingleEntryDataViewModel.getEntriesWithTIDType(journalID, "percentage").observe(this, new Observer<List<JournalSingleEntryDataObject>>() {
            @Override
            public void onChanged(List<JournalSingleEntryDataObject> journalSingleEntryDataObjects) {

                for(int i = 0 ; i < journalSingleEntryDataObjects.size() ; i++){
                    Log.d("Diss", "Value of entryData: " + journalSingleEntryDataObjects.get(i).getEntryData());


                }
            }
        });

        chartFunc();



    }

    private void chartFunc(){
        GraphView graph = (GraphView) findViewById(R.id.graph);
        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(new DataPoint[] {
                new DataPoint(0, 1),
                new DataPoint(1, 5),
                new DataPoint(2, 3),
                new DataPoint(3, 2),
                new DataPoint(4, 6)
        });
        graph.addSeries(series);
    }


}
