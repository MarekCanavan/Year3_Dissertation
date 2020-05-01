package com.example.diss_cbt_application.JournalFeature.JActivities_Fragments.MyJournals;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.diss_cbt_application.JournalFeature.JActivities_Fragments.Entries.JournalNewStructure;
import com.example.diss_cbt_application.JournalFeature.JDatabase.JDTables.JournalObject;
import com.example.diss_cbt_application.JournalFeature.JActivities_Fragments.General.JournalContract;
import com.example.diss_cbt_application.JournalFeature.JDatabase.JDViewModels.JournalViewModel;
import com.example.diss_cbt_application.R;
import com.example.diss_cbt_application.Utils.VerticalSpaceItemDecoration;

import java.util.List;

/**This Activity presents the user their Journals in a Recycler View on the fragment
 * The user has the option of clicking a specific journal - to which they will be taken to a new activity - and nbe able to inspect it
 * or setting a new journal structure by clicking the '+' button, which will launch a new activity where they can set a new structure
 * */
public class MyJournalsFragment extends Fragment implements View.OnClickListener {

    /*Member variable for the journalSingleEntryViewModel*/
    private JournalViewModel journalViewModel;

    /**
     * Sets up the Fragment when the user first navigates to it
     * Populates the RecyclerView
     * Draws a plus on the Button for styling
     * Handles when the user clicks on a journal, by packaging the information in an intent and sending to the next activity.
     *
     * @return RootView - which is the View in which all the other views are placed*/
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("g53mdp", "In Journal Create View");

        View rootView = inflater.inflate(R.layout.fragment_my_journals, container, false);

        /*Adds the '+' image in drawable folder to the button for a New Entry*/
        ImageButton newEntry = rootView.findViewById(R.id.bt_new_entry);
        newEntry.setImageResource(R.mipmap.ic_addition_button_dark_blue_round);

        newEntry.setOnClickListener(this);

        /*Initialise Recycler View*/
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view__my_journals_fragment);//get reference to the recycler view
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext())); //every recycler view needs a layout manager
        recyclerView.setHasFixedSize(true); //Makes recycler view more efficient, we know card size wont change
        recyclerView.addItemDecoration(new VerticalSpaceItemDecoration(5));//Call to VerticalSpaceItemDecoration adds barrier between entries in RV for styling

        final RVAMyJournals adapter = new RVAMyJournals(); //Parse RecyclerView arrays to populate with data
        recyclerView.setAdapter(adapter);

        /* Get reference to the Journal View Model
         * Then observe the changes to the database, specifically the 'getAllJournals' Query
         * Update the recycler with these objects when a change is made to the database */
        journalViewModel = ViewModelProviders.of(this).get(JournalViewModel.class);
        journalViewModel.getAllJournals().observe(this, new Observer<List<JournalObject>>() {
            @Override
            public void onChanged(List<JournalObject> journalObjects) {
                adapter.setJournals(journalObjects);
            }
        });


        adapter.setOnItemClickListener(new RVAMyJournals.OnItemClickListener() {
            @Override
            public void onItemClick(JournalObject entry) {

                Intent intent = new Intent(getContext(), JournalData.class);

                intent.putExtra(JournalContract._ID, entry.getId());
                intent.putExtra(JournalContract.JOURNAL_NAME, entry.getJournalName());
                intent.putExtra(JournalContract.JOURNAL_COLOUR, entry.getJournalColour());

                Log.d("Diss", "Journal name before: " + entry.getJournalName());

                startActivity(intent);

            }
        });
        

        return rootView;
    }


    /**When the user clicks on the '+' button on the MyJournals Fragment this function is called
     * It sends an intent to the 'JournalNewStructure' Activity where the user can create a new journal structure
     *
     * @param v - the function is parsed the view */
    @Override
    public void onClick(View v) {
        Toast.makeText(getContext(), "newJournalToast", Toast.LENGTH_SHORT).show();
        Intent i_choose_journal = new Intent(getContext(), JournalNewStructure.class);
        startActivity(i_choose_journal);
    }

}
