package com.example.diss_cbt_application.JournalFeature.JActivities_Fragments;

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

    /*Database variable declarations*/
    private DatabaseHelper dbHelper = null; //reference to db helper for insertion
    private SQLiteDatabase db_read;




    /*The ArrayLists are need to parse the data to the RecyclerView*/
    private ArrayList<Integer> mIDs = new ArrayList<>();
    private ArrayList<String> mEntryNames = new ArrayList<>();
    private ArrayList<String> mJournalNames = new ArrayList<>();
    private ArrayList<String> mEntryTimes = new ArrayList<>();
    private ArrayList<String> mEntryDates = new ArrayList<>();
    private ArrayList<Integer> mJournalColour = new ArrayList<>();

    public JournalFragment() {
        super();
    }

    /**
     * Sets up the Fragment when the user first navigates to it
     * Populates the RecyclerView
     * Draws a plus on the Button for styling
     * @return RootView which is the View in which all the other views are placed*/
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("g53mdp", "In Journal Create View");


        /*Call to function to retrieve data for the RecyclerView*/
        recyclerViewLoadData();

        View rootView = inflater.inflate(R.layout.fragment_journal, container, false);

        /*Adds the image to the button for a New Entry*/
        ImageButton newEntry = rootView.findViewById(R.id.bt_new_entry);
        newEntry.setImageResource(R.mipmap.ic_addition_button_dark_blue_round);


        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view_journal_fragment);

        recyclerView.addItemDecoration(new VerticalSpaceItemDecoration(5));//Call to VerticalSpaceItemDecoration adds barrier between entries in RV for styling
        recyclerView.setHasFixedSize(true); //Needed as we know our cards will always be the same size

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        final RVAJournalFragement adapter = new RVAJournalFragement(); //Parse RecyclerView arrays to populate with data

        //recyclerView.setAdapter(adapter);


        return rootView;
    }

    @Override
    public void onClick(View v) {

    }

    /**
     * Function separates the code responsible for retrieving data for the recycler view  */
    private void recyclerViewLoadData(){

        /*Connect to database */
        dbHelper = new DatabaseHelper(getContext());
        db_read = dbHelper.getReadableDatabase();

        /*The fields we expect to return from the database */
        String[] projection = new String[]{
                JournalContract._ID,
                JournalContract.ENTRY_NAME,
                JournalContract.ENTRY_DATE,
                JournalContract.ENTRY_TIME,
                JournalContract.ENTRY_JOURNAL_TYPE,
                JournalContract.JOURNAL_COLOUR
        };

        /*Projection is sent to the databased and all fields retrieved and stored in a cursor*/
        Cursor c = db_read.query(JournalContract.SENTRY, projection, null, null ,
                null, null, null);

        Log.d("Diss", "Value of cursor: " + c);

        /*If statement iterates through the cursor to retrieve entry data*/
        if(c.moveToFirst()) {
            do {

                Log.d("Diss", "mEntryNames" + c.getString(1));
                Log.d("Diss", "mEntryTimes" + c.getString(2) );
                Log.d("Diss", "mJournalNames" + c.getString(3));


                /*Values are taken from the cursor and stored in ArrayList
                * After this function is finished the Arraylists will be sent to the RecyclerView
                * And the data will be represented to the user*/
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
