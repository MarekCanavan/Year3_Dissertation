package com.example.diss_cbt_application.JournalFeature.JActivities_Fragments;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.diss_cbt_application.DatabaseHelper;
import com.example.diss_cbt_application.JournalFeature.JDatabase.JDTables.JournalObject;
import com.example.diss_cbt_application.JournalFeature.JDatabase.JDViewModels.JournalViewModel;
import com.example.diss_cbt_application.JournalFeature.JournalContract;
import com.example.diss_cbt_application.JournalFeature.RVAChooseJournal;
import com.example.diss_cbt_application.R;
import com.example.diss_cbt_application.VerticalSpaceItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**This Activity gives the user the option to complete a Journal Structure that has been already created
 * or they can choose to create their own new one. The already made journals are represented to the user
 * in a recycler view which is fetching the LiveData from our Room database.
 * */
public class JournalChooseActivity extends AppCompatActivity {

    private JournalViewModel journalViewModel;

    int LAUNCH_Journal_Choose_Activity = 1;
    int NEW_JOURNAL_MADE = 2;
    int NEW_ENTRY_MADE = 3;

    /**
     * Sets up the Fragment when the user first navigates to it
     * Populates the RecyclerView
     * Draws a plus on the Button for styling
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
        recyclerView.addItemDecoration(new VerticalSpaceItemDecoration(3));//Call to VerticalSpaceItemDecoration adds barrier between entries in RV for styling

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


    /**Simple function that opens the JournalNewStructure activity when the '+' button is pressed*/
    public void newJournalOnClick(View v){

        Intent i_choose_journal = new Intent(JournalChooseActivity.this, JournalNewStructure.class);
        startActivityForResult(i_choose_journal, LAUNCH_Journal_Choose_Activity);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == LAUNCH_Journal_Choose_Activity) {
            if(resultCode == NEW_JOURNAL_MADE){
                //recyclerViewFunc();
            }
        }
    }//onActivityResult

}
