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
        chart = findViewById(R.id.chart);

        ArrayList<Entry> entries = new ArrayList<>();
        entries.add(new Entry(2010-3-2, 4));
        entries.add(new Entry(2012-9-2, 1));
        entries.add(new Entry(2013-3-2, 2));
        entries.add(new Entry(2015-3-2, 4));
        entries.add(new Entry(2017-3-2, 4));
        entries.add(new Entry(2019-3-2, 1));
        entries.add(new Entry(2020-3-2, 2));
//        entries.add(new Entry(5.2f, 1));
//        entries.add(new Entry(5.4f, 1));
//        entries.add(new Entry(5.6f, 1));
//        entries.add(new Entry(5.8f, 1));

        LineDataSet dataSet = new LineDataSet(entries, "Customized values");
        dataSet.setColor(ContextCompat.getColor(this, R.color.colorPrimary));
        dataSet.setValueTextColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));

        //****
        // Controlling X axis
        XAxis xAxis = chart.getXAxis();
        // Set the xAxis position to bottom. Default is top
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        /*This is what we want for when we have limiting factors on the data
        //Customizing x axis value
        final String[] months = new String[]{"Mon", "Tue", "Wed", "Thur", "Fri", "Sat", "Sun"};

        IAxisValueFormatter formatter = new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return months[(int) value];
            }
        };
        xAxis.setGranularity(1f); // minimum axis-step (interval) is 1
        xAxis.setValueFormatter(formatter);*/

        //***
        // Controlling right side of y axis
        YAxis yAxisRight = chart.getAxisRight();
        yAxisRight.setEnabled(false);

        //***
        // Controlling left side of y axis
        YAxis yAxisLeft = chart.getAxisLeft();
        yAxisLeft.setGranularity(1f);

        // Setting Data
        LineData data = new LineData(dataSet);
        chart.setData(data);
        chart.animateX(2500);
        //refresh
        chart.invalidate();
    }


}
