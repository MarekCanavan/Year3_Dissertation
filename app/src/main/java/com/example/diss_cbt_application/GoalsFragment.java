package com.example.diss_cbt_application;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class GoalsFragment extends Fragment {

    /*Database variable declarations*/
    private DatabaseHelper dbHelper = null; //reference to db helper for insertion
    private SQLiteDatabase db_read;


    /*ArrayList definitions for the representation of data on the RecyclerView*/
    private ArrayList<Integer> mIDs = new ArrayList<>();
    private ArrayList<String> mTitle = new ArrayList<>();
    private ArrayList<String> mDate= new ArrayList<>();
    private ArrayList<String> mTime = new ArrayList<>();

    public GoalsFragment() {
        super();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Log.d("g53mdp", "In Breath Create View");

        View rootView = inflater.inflate(R.layout.fragment_goals, container, false);

        loadDataForRecyclerView();

        /*Adds the image to the button for a New Entry*/
        ImageButton newEntry = rootView.findViewById(R.id.bt_new_goal);
        newEntry.setImageResource(R.mipmap.ic_addition_button_dark_blue_round);


        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view_goals_fragment);
        recyclerView.addItemDecoration(new VerticalSpaceItemDecoration(5));//Call to VerticalSpaceItemDecoration adds barrier between entries in RV for styling
        recyclerView.setHasFixedSize(true); //Needed as we know our cards will always be the same size
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        RVAGoalsFragement adapter = new RVAGoalsFragement(mIDs, mTitle, mDate,
                mTime, getContext()); //Parse RecyclerView arrays to populate with data
        recyclerView.setAdapter(adapter);


        return rootView;
    }

    private void loadDataForRecyclerView(){

        /*Connect to database */
        dbHelper = new DatabaseHelper(getContext());
        db_read = dbHelper.getReadableDatabase();

        /*The fields we expect to return from the database */
        String[] projection = new String[]{
                GContract._ID,
                GContract.G_TITLE,
                GContract.G_DATE,
                GContract.G_TIME
        };

        /*Projection is sent to the databased and all fields retrieved and stored in a cursor*/
        Cursor c = db_read.query(GContract.G_TABLE, projection, null, null ,
                null, null, null);

        Log.d("Diss", "Value of cursor: " + c);

        /*If statement iterates through the cursor to retrieve entry data*/
        if(c.moveToFirst()) {
            do {

                /*Values are taken from the cursor and stored in ArrayList
                 * After this function is finished the Arraylists will be sent to the RecyclerView
                 * And the data will be represented to the user*/
                mIDs.add(c.getInt(0));
                mTitle.add(c.getString(1));
                mDate.add(c.getString(2));
                mTime.add(c.getString(3));

            }while(c.moveToNext());
        }

    }
}
