package com.example.diss_cbt_application.JournalFeature.JActivities_Fragments.General;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.diss_cbt_application.JournalFeature.JActivities_Fragments.General.JournalFragmentAdapter;
import com.example.diss_cbt_application.R;
import com.google.android.material.tabs.TabLayout;

public class JournalParentFragment extends Fragment {


    public JournalParentFragment() {
        super();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("g53mdp", "In Journal Create View");

        View rootView = inflater.inflate(R.layout.fragement_journal_parent, container, false);

        /*Skeleton Implementation*/

        // Get the ViewPager and set it's PagerAdapter so that it can display items
        ViewPager viewPager = rootView.findViewById(R.id.vpPager);
        viewPager.setAdapter(new JournalFragmentAdapter(getChildFragmentManager()));

        // Give the TabLayout the ViewPager
        TabLayout tabLayout =  rootView.findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);


        return rootView;
    }


}
