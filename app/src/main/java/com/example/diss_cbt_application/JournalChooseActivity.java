package com.example.diss_cbt_application;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

public class JournalChooseActivity extends AppCompatActivity {

    private static final String TAG = "JournalChooseActivity";
    //vars
    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<Integer> mIDs = new ArrayList<>();
    private DatabaseHelper dbHelper = null; //reference to db helper for insertion
    private SQLiteDatabase db_read;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal_choose);

        Log.d(TAG, "onCreate: started");

        dbHelper = new DatabaseHelper(this);
        db_read = dbHelper.getReadableDatabase();


        recyclerViewFunc();
    }

    private void recyclerViewFunc(){


        String[] projection = new String[]{
                "_id",
                "journalName"
        };

        Cursor c = db_read.query("JournalNames", projection, null, null ,
                                    null, null, null);

        Log.d("Diss", "Value of cursor: " + c);

        if(c.moveToFirst()) {
            do {
                int id = c.getInt(0);
                String name = c.getString(1);


                mIDs.add(c.getInt(0));
                mNames.add(c.getString(1));

                Log.d("Diss", "Value of id: " + id);
                Log.d("Diss", "Value of name: " + name);
            }while(c.moveToNext());
        }

        initRecylcerView();
    }

    private void initRecylcerView(){
        RecyclerView recyclerView = findViewById(R.id.recycler_view);

        RVAChooseJournal adapter = new RVAChooseJournal(mIDs, mNames, this);
        recyclerView.setAdapter(adapter);

        //Not sure this will work with LinearLayoutMananger, amaybe need constraint or relative
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }


    public void newJournalOnClick(View v){
        Intent i_choose_journal = new Intent(JournalChooseActivity.this, JournalNewStructure.class);
        startActivity(i_choose_journal);
    }

}
