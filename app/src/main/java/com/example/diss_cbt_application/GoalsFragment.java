package com.example.diss_cbt_application;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class GoalsFragment extends Fragment {

    public GoalsFragment() {
        super();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Log.d("g53mdp", "In Breath Create View");

        View rootView = inflater.inflate(R.layout.fragment_goals, container, false);

        /*Adds the image to the button for a New Entry*/
        ImageButton newEntry = rootView.findViewById(R.id.bt_new_goal);
        newEntry.setImageResource(R.mipmap.ic_addition_button_dark_blue_round);

        return rootView;
    }
}
