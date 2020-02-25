package com.example.diss_cbt_application;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class JournalFragment extends Fragment implements View.OnClickListener{

    private DatabaseHelper dbHelper = null; //reference to db helper for insertion
    private SQLiteDatabase db_read;


    private ArrayList<Integer> mIDs = new ArrayList<>();
    private ArrayList<String> mEntryNames = new ArrayList<>();
    private ArrayList<String> mJournalNames = new ArrayList<>();
    private ArrayList<String> mEntryTimes = new ArrayList<>();

    public JournalFragment() {
        super();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("g53mdp", "In Journal Create View");

        mIDs.add(0);
        mEntryNames.add("Entry 1");
        mJournalNames.add("Journal 1");
        mEntryTimes.add("00:00");

        mIDs.add(0);
        mEntryNames.add("Entry 2");
        mJournalNames.add("Journal 1");
        mEntryTimes.add("00:00");

        mIDs.add(0);
        mEntryNames.add("Entry 2");
        mJournalNames.add("Journal 1");
        mEntryTimes.add("00:00");

        mIDs.add(0);
        mEntryNames.add("Entry 2");
        mJournalNames.add("Journal 2");
        mEntryTimes.add("00:00");
        mIDs.add(0);
        mEntryNames.add("Entry 1");
        mJournalNames.add("Journal 1");
        mEntryTimes.add("00:00");

        mIDs.add(0);
        mEntryNames.add("Entry 2");
        mJournalNames.add("Journal 1");
        mEntryTimes.add("00:00");

        mIDs.add(0);
        mEntryNames.add("Entry 2");
        mJournalNames.add("Journal 1");
        mEntryTimes.add("00:00");

        mIDs.add(0);
        mEntryNames.add("Entry 2");
        mJournalNames.add("Journal 2");
        mEntryTimes.add("00:00");

        mIDs.add(0);
        mEntryNames.add("Entry 1");
        mJournalNames.add("Journal 1");
        mEntryTimes.add("00:00");

        mIDs.add(0);
        mEntryNames.add("Entry 2");
        mJournalNames.add("Journal 1");
        mEntryTimes.add("00:00");

        mIDs.add(0);
        mEntryNames.add("Entry 2");
        mJournalNames.add("Journal 1");
        mEntryTimes.add("00:00");

        mIDs.add(0);
        mEntryNames.add("Entry 2");
        mJournalNames.add("Journal 2");
        mEntryTimes.add("00:00");

        recyclerViewLoadData();

        View rootView = inflater.inflate(R.layout.fragment_journal, container, false);
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view_journal_fragment);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        RVAJournalFragement adapter = new RVAJournalFragement(mIDs, mEntryNames, mJournalNames,mEntryTimes , getContext());
        recyclerView.setAdapter(adapter);

        return rootView;
    }

    @Override
    public void onClick(View v) {

    }

    private void recyclerViewLoadData(){

        dbHelper = new DatabaseHelper(getContext());
        db_read = dbHelper.getReadableDatabase();

        /*String[] projection = new String[]{

                "",
                "",
                "",
                ""

        };

        Cursor c = db_read.query("JournalNames", projection, null, null ,
                null, null, null);

        Log.d("Diss", "Value of cursor: " + c);

        if(c.moveToFirst()) {
            do {

            }while(c.moveToNext());
        }*/


    }






}
