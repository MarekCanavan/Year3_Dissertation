package com.example.diss_cbt_application.HomeFeature;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.diss_cbt_application.R;

/**This class represent the HomeFeature. The implementation of this feature is small, the page simply gives the user
 * instructions on what the other features in the application are for. This information is set in the xml file
 * which is called using inflater. Inflating this view is the sole responsibility of this class. */
public class HomeFragment extends Fragment {

    public HomeFragment() {
        super();
    }

    /**onCreateView simply inflates the xml file fragment_home, which has all of the instructions for the exercises in the xml file*/
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("g53mdp", "In Home Create View ");
        return inflater.inflate(R.layout.fragment_home, container, false);
    }
}
