package com.example.diss_cbt_application.JournalFeature.JActivities_Fragments.General;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.diss_cbt_application.JournalFeature.JActivities_Fragments.Entries.JournalFragment;
import com.example.diss_cbt_application.JournalFeature.JActivities_Fragments.MyJournals.MyJournalsFragment;

public class JournalFragmentAdapter extends FragmentPagerAdapter {

    private static int NUM_ITEMS = 2;

    public JournalFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        switch(position){
            case 0:
                return new JournalFragment();
            case 1:
                return new MyJournalsFragment();
            default:
                return null;

        }
    }

    @Override
    public int getCount() {
        return NUM_ITEMS;
    }

    @Override
    public CharSequence getPageTitle(int position){
        switch(position){
            case 0:
                return "Entries" ;
            case 1:
                return "Journals";
        }
        return null;
    }
}
