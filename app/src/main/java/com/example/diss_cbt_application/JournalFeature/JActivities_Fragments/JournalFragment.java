package com.example.diss_cbt_application.JournalFeature.JActivities_Fragments;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.diss_cbt_application.DatabaseHelper;
import com.example.diss_cbt_application.GoalsFeature.GoalObject;
import com.example.diss_cbt_application.GoalsFeature.GoalViewModel;
import com.example.diss_cbt_application.JournalFeature.JDatabase.JDTables.JournalObject;
import com.example.diss_cbt_application.JournalFeature.JDatabase.JDTables.JournalSingleEntryObject;
import com.example.diss_cbt_application.JournalFeature.JDatabase.JDViewModels.JournalSingleEntryViewModel;
import com.example.diss_cbt_application.JournalFeature.JDatabase.JDViewModels.JournalViewModel;
import com.example.diss_cbt_application.JournalFeature.JournalContract;
import com.example.diss_cbt_application.JournalFeature.RVAJournalFragement;
import com.example.diss_cbt_application.R;
import com.example.diss_cbt_application.VerticalSpaceItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * Fragment for the Journal Page
 * The user is presented with a RecyclerView of their previous journal entries
 * The user has the option of:
 *      - Viewing an entry
 *      - Creating a new entry
 *      - Viewing their current Journals
 * */
public class JournalFragment extends Fragment implements View.OnClickListener{

    /*Member variable for the journalSingleEntryViewModel*/
    JournalSingleEntryViewModel journalSingleEntryViewModel;

    public JournalFragment() {
        super();
    }

    /**
     * Sets up the Fragment when the user first navigates to it
     * Populates the RecyclerView
     * Draws a plus on the Button for styling
     *
     * @return RootView which is the View in which all the other views are placed*/
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("g53mdp", "In Journal Create View");


        View rootView = inflater.inflate(R.layout.fragment_journal, container, false);

        /*Adds the '+' image in drawable folder to the button for a New Entry*/
        ImageButton newEntry = rootView.findViewById(R.id.bt_new_entry);
        newEntry.setImageResource(R.mipmap.ic_addition_button_dark_blue_round);

        /*Initialise Recycler View*/
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view_journal_fragment);//get reference to the recycler view
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext())); //every recycler view needs a layout manager
        recyclerView.setHasFixedSize(true); //Makes recycler view more efficient, we know card size wont change
        recyclerView.addItemDecoration(new VerticalSpaceItemDecoration(5));//Call to VerticalSpaceItemDecoration adds barrier between entries in RV for styling

        final RVAJournalFragement adapter = new RVAJournalFragement(); //Parse RecyclerView arrays to populate with data
        recyclerView.setAdapter(adapter);

        /* Get reference to the JournalSingleEntry View Model - member variable
         * Then observe the changes to the database, specifically the 'getAllEntries' Query
         * Update the recycler with these objects when a change is made to the database */
        journalSingleEntryViewModel = ViewModelProviders.of(this)
                .get(JournalSingleEntryViewModel.class);
        journalSingleEntryViewModel.getAllEntries().observe(this, new Observer<List<JournalSingleEntryObject>>() {
            @Override
            public void onChanged(List<JournalSingleEntryObject> journalSingleEntryObjects) {

                adapter.setEntries(journalSingleEntryObjects);

            }
        });

        /*Handles when a user clicks on a card in the recycler view*/
        adapter.setOnItemClickListener(new RVAJournalFragement.OnItemClickListener() {
            @Override
            public void onItemClick(JournalSingleEntryObject entry) {

                Intent intent = new Intent(getContext(), JournalEntryData.class);

                intent.putExtra("_id", entry.getId());
                intent.putExtra(JournalContract.ENTRY_NAME, entry.getEntryName());
                intent.putExtra(JournalContract.JOURNAL_NAME, entry.getJournalType());
                intent.putExtra(JournalContract.ENTRY_TIME, entry.getEntryTime());
                intent.putExtra(JournalContract.ENTRY_DATE, entry.getEntryDate());
                intent.putExtra(JournalContract.JOURNAL_COLOUR, entry.getJournalColour());

                startActivity(intent);

            }
        });

        return rootView;
    }

    @Override
    public void onClick(View v) {

    }

}
