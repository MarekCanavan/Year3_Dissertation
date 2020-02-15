package com.example.diss_cbt_application;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class JournalFragment extends Fragment implements View.OnClickListener{

    public JournalFragment() {
        super();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("g53mdp", "In Journal Create View");
        return inflater.inflate(R.layout.fragment_journal, container, false);
    }

    @Override
    public void onClick(View v) {

    }





}
