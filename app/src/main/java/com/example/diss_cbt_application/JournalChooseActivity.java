package com.example.diss_cbt_application;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class JournalChooseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal_choose);
    }

    public void newJournalOnClick(View v){
        Intent i_choose_journal = new Intent(JournalChooseActivity.this, JournalNewStructure.class);
        startActivity(i_choose_journal);
    }

}
