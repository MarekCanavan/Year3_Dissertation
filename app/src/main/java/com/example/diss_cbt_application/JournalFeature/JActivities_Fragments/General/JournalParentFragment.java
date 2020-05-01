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

/**The Journal Feature is made up of two fragments, one for the Journals and one for the Entries.
 * They are both placed in a tab layout so the user can easily switch between them. So this class is the parent of both of
 * those fragments and inflates the initial view. */
public class JournalParentFragment extends Fragment {

    public JournalParentFragment() {
        super();
    }

    /**onCreateView is responsible for inflating the initial view and then handling the two child fragments.
     * JournalFragmentAdapter is used to manage both of the fragments. The Viewpager and TabLayout are also set in this
     * class to help with handling the fragments. */
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
