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

public class JournalChooseActivity extends AppCompatActivity {

    private static final String TAG = "JournalChooseActivity";
    //vars
    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<Integer> mIDs = new ArrayList<>();
    private ArrayList<Integer> mJournalColours = new ArrayList<>();
    private DatabaseHelper dbHelper = null; //reference to db helper for insertion
    private SQLiteDatabase db_read;

    private JournalViewModel journalViewModel;

    int LAUNCH_Journal_Choose_Activity = 1;
    int NEW_JOURNAL_MADE = 2;
    int NEW_ENTRY_MADE = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal_choose);

        Log.d(TAG, "onCreate: started");

        dbHelper = new DatabaseHelper(this);
        db_read = dbHelper.getReadableDatabase();

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        //recyclerView.removeAllViews();
        //Call to VerticalSpaceItemDecoration adds barrier between entries in RV for styling
        recyclerView.addItemDecoration(new VerticalSpaceItemDecoration(3));

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        final RVAChooseJournal adapter = new RVAChooseJournal();
        recyclerView.setAdapter(adapter);

        /*Had major error with this line, needed to look at comments from codinginflow video */
        journalViewModel = ViewModelProviders.of(this).get(JournalViewModel.class);
        journalViewModel.getAllJournals().observe(this, new Observer<List<JournalObject>>() {
            @Override
            public void onChanged(List<JournalObject> journalObjects) {
                Toast.makeText(JournalChooseActivity.this, "OnChanged", Toast.LENGTH_SHORT).show();
                adapter.setJournals(journalObjects);
            }
        });

        //recyclerViewFunc();
    }

    private void recyclerViewFunc(){

        //Clear and left over values
        mNames.clear();
        mIDs.clear();
        mJournalColours.clear();

        String[] projection = new String[]{
                JournalContract._ID,
                JournalContract.JOURNAL_NAME,
                JournalContract.JOURNAL_COLOUR
        };

        Cursor c = db_read.query(JournalContract.JOURNAL_NAMES, projection, null, null ,
                                    null, null, null);

        Log.d("Diss", "Value of cursor: " + c);

        if(c.moveToFirst()) {
            do {
                int id = c.getInt(0);
                String name = c.getString(1);
                int colour = c.getInt(2);


                mIDs.add(c.getInt(0));
                mNames.add(c.getString(1));
                mJournalColours.add(colour);

                Log.d("Diss", "Value of id: " + id);
                Log.d("Diss", "Value of name: " + name);

            }while(c.moveToNext());
        }

        initRecylcerView();
    }

    private void initRecylcerView(){
        //RecyclerView recyclerView = findViewById(R.id.recycler_view);
        //recyclerView.removeAllViews();
        //Call to VerticalSpaceItemDecoration adds barrier between entries in RV for styling
        //recyclerView.addItemDecoration(new VerticalSpaceItemDecoration(3));
        //RVAChooseJournal adapter = new RVAChooseJournal(mIDs, mNames, mJournalColours, this);
        //recyclerView.setAdapter(adapter);

       // recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }


    public void newJournalOnClick(View v){

        Intent i_choose_journal = new Intent(JournalChooseActivity.this, JournalNewStructure.class);
        startActivityForResult(i_choose_journal, LAUNCH_Journal_Choose_Activity);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == LAUNCH_Journal_Choose_Activity) {
            if(resultCode == NEW_JOURNAL_MADE){
                recyclerViewFunc();
            }
        }
    }//onActivityResult

}
