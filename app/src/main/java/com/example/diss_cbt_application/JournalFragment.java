package com.example.diss_cbt_application;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
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
    private ArrayList<String> mEntryDates = new ArrayList<>();
    private ArrayList<Integer> mJournalColour = new ArrayList<>();

    public JournalFragment() {
        super();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("g53mdp", "In Journal Create View");


        recyclerViewLoadData();

        View rootView = inflater.inflate(R.layout.fragment_journal, container, false);
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view_journal_fragment);

        ImageButton newEntry = rootView.findViewById(R.id.bt_new_entry);
        newEntry.setImageResource(R.mipmap.ic_addition_button_dark_blue_round);

        //Call to VerticalSpaceItemDecoration adds barrier between entries in RV for styling
        recyclerView.addItemDecoration(new VerticalSpaceItemDecoration(5));

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        RVAJournalFragement adapter = new RVAJournalFragement(mIDs, mEntryNames, mJournalNames,
                mEntryTimes,mEntryDates, mJournalColour, getContext());
        recyclerView.setAdapter(adapter);

        return rootView;
    }

    @Override
    public void onClick(View v) {

    }

    private void recyclerViewLoadData(){

        dbHelper = new DatabaseHelper(getContext());
        db_read = dbHelper.getReadableDatabase();

        String[] projection = new String[]{
                JournalContract._ID,
                JournalContract.ENTRY_NAME,
                JournalContract.ENTRY_DATE,
                JournalContract.ENTRY_TIME,
                JournalContract.ENTRY_JOURNAL_TYPE,
                JournalContract.JOURNAL_COLOUR
        };

        Cursor c = db_read.query(JournalContract.SENTRY, projection, null, null ,
                null, null, null);

        Log.d("Diss", "Value of cursor: " + c);

        if(c.moveToFirst()) {
            do {

                Log.d("Diss", "mEntryNames" + c.getString(1));
                Log.d("Diss", "mEntryTimes" + c.getString(2) );
                Log.d("Diss", "mJournalNames" + c.getString(3));


                mIDs.add(c.getInt(0));
                mEntryNames.add(c.getString(1));
                mEntryDates.add(c.getString(2));
                mEntryTimes.add(c.getString(3));
                mJournalNames.add(c.getString(4));
                mJournalColour.add(c.getInt(5));


            }while(c.moveToNext());
        }


    }






}
