package com.example.diss_cbt_application.JournalFeature.JActivities_Fragments.Entries;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.diss_cbt_application.JournalFeature.JDatabase.JDTables.JournalObject;
import com.example.diss_cbt_application.JournalFeature.JDatabase.JDViewModels.JournalViewModel;
import com.example.diss_cbt_application.JournalFeature.JActivities_Fragments.General.JournalContract;
import com.example.diss_cbt_application.R;
import com.example.diss_cbt_application.Utils.VerticalSpaceItemDecoration;

import java.util.List;

/**This Activity gives the user the option to complete a Journal Structure that has been already created
 * or they can choose to create their own new one. The already made journals are represented to the user
 * in a recycler view which is fetching the LiveData from our Room database.
 * */
public class JournalChooseActivity extends AppCompatActivity {

    /*Instance of the JournalView Model*/
    private JournalViewModel journalViewModel;

    /**
     * Sets up the Fragment when the user first navigates to it.
     * Populates the RecyclerView with the journals the user can choose from
     * or they can create a new journal structure.
     *
     * @return RootView which is the View in which all the other views are placed*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal_choose);

        /*Initialise Recycler View*/
        RecyclerView recyclerView = findViewById(R.id.recycler_view);//get reference to recycler view
        recyclerView.setLayoutManager(new LinearLayoutManager(this));//every recycler view needs a layout manager
        recyclerView.setHasFixedSize(true);//Makes recycler view more efficient, we know card size wont change
        //recyclerView.addItemDecoration(new VerticalSpaceItemDecoration(3));//Call to VerticalSpaceItemDecoration adds barrier between entries in RV for styling

        final RVAChooseJournal adapter = new RVAChooseJournal();//new adapter
        recyclerView.setAdapter(adapter);//by default this is empty, it gets set in the observer below

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

        /*Handles when a user clicks on a card in the recycler view*/
        adapter.setOnItemClickListener(new RVAChooseJournal.OnItemClickListener() {
            @Override
            public void onItemClick(JournalObject journal) {
                Intent intent = new Intent(JournalChooseActivity.this, JournalCompleteEntryActivity.class);

                intent.putExtra(JournalContract.ID , journal.getId());
                intent.putExtra(JournalContract.JOURNAL_NAME, journal.getJournalName());
                intent.putExtra(JournalContract.JOURNAL_COLOUR, journal.getJournalColour());

                startActivity(intent);
            }
        });
    }

    /**Simple function that opens the JournalNewStructure activity when the user presses the button to create a new journal*/
    public void newJournalOnClick(View v){
        Intent i_choose_journal = new Intent(JournalChooseActivity.this, JournalNewStructure.class);
        startActivity(i_choose_journal);
    }
}
