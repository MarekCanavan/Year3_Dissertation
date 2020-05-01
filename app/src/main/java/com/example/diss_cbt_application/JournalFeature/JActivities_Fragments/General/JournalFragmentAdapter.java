package com.example.diss_cbt_application.JournalFeature.JActivities_Fragments.General;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.diss_cbt_application.JournalFeature.JActivities_Fragments.Entries.JournalFragment;
import com.example.diss_cbt_application.JournalFeature.JActivities_Fragments.MyJournals.MyJournalsFragment;

/**This is a helper class that manages the transitions between two child fragments of the journal feature - entries and journals.
 * When the user swipes between the two this class will be notified of those changes and create a new instance of the fragment for the user
 * to view and interact with. */
public class JournalFragmentAdapter extends FragmentPagerAdapter {

    private static int NUM_ITEMS = 2;

    public JournalFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    /**Generates a new fragment instance of the required child fragment given position
     *
     * @param position - the fragment we want to retrieve
     * @return Fragment - the fragment the user wants to interact with*/
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

    /**Returns the title page label needed based on where the user has navigated to.
     *
     * @param position - the fragment we want to retrieve.
     * @return CharSequence - string of characters representing the page the user is interacting with.
     * */
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
