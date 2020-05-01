package com.example.diss_cbt_application.Utils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.diss_cbt_application.BreathFeature.BreathFragment;
import com.example.diss_cbt_application.GoalsFeature.GContract;
import com.example.diss_cbt_application.GoalsFeature.GNewEditGoal;
import com.example.diss_cbt_application.GoalsFeature.GoalsFragment;
import com.example.diss_cbt_application.HomeFeature.HomeFragment;
import com.example.diss_cbt_application.JournalFeature.JActivities_Fragments.Entries.JournalChooseActivity;
import com.example.diss_cbt_application.JournalFeature.JActivities_Fragments.General.JournalParentFragment;
import com.example.diss_cbt_application.Notifications.DailyNotificationReceiver;
import com.example.diss_cbt_application.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Calendar;

/**This is the MainActivity of the Application, so is the first Activity that gets loaded
 * The Main Activity holds 4 fragments which represent the 4 features of the application
 *      - Home Fragment - the first one that is loaded
 *      - Journal Fragment
 *      - Goals Fragment
 *      - Breath Fragment
 *This activity holds the bottom navigation bar to switch between these fragments/features*/
public class MainActivity extends AppCompatActivity {

    int fragmentFlag;

    final FragmentManager fragmentManager = getSupportFragmentManager();
    Fragment homeFragment = new HomeFragment();
    Fragment goalFragment = new GoalsFragment();
    Fragment breathFragment = new BreathFragment();
    Fragment journalParentFragment = new JournalParentFragment();
    Fragment active = homeFragment;

    BottomNavigationView bottomNav;

    /**this onCreate method is the first that is run when the application is first loaded. So it is responsible
     * for handling and generating the bottom nav and all of the feature fragments.
     *
     * @param savedInstanceState - parameter that is passed to the onCreate of every Android Activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Morning Notification
        setDailyNotification(9, 0, "Good Morning", "Start the day by setting some goals to complete");

        //Evening Notification
        setDailyNotification(17, 0, "Good Afternoon", "How has your day been? Why not reflect on it in a journal entry...");

        String menuFragment = getIntent().getStringExtra("notificationLaunch");


        if(savedInstanceState == null ){//First time, create fragments

            fragmentManager.beginTransaction().add(R.id.fragment_container, goalFragment, "homeFragment")
                    .hide(goalFragment).commit();
            fragmentManager.beginTransaction().add(R.id.fragment_container, journalParentFragment, "journalParentFragment")
                    .hide(journalParentFragment).commit();
            fragmentManager.beginTransaction().add(R.id.fragment_container, breathFragment, "breathFragment")
                    .hide(breathFragment).commit();
            fragmentManager.beginTransaction().add(R.id.fragment_container, homeFragment, "homeFragment").commit();

            if(menuFragment != null){
                if(menuFragment.equals("goalsFragment")){
                    fragmentManager.beginTransaction().hide(active).show(goalFragment).commit();
                    active = goalFragment;
                }
            }
        }
        else{

            //if there is already an instance state retireve them from the amanger with the key you give them

            homeFragment = fragmentManager.getFragment(savedInstanceState, "homeFragment");
            goalFragment = fragmentManager.getFragment(savedInstanceState, "goalFragment");
            breathFragment = fragmentManager.getFragment(savedInstanceState, "breathFragment");
            journalParentFragment = fragmentManager.getFragment(savedInstanceState, "journalParentFragment");

        }

        bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        bottomNav.getMenu().getItem(1).setChecked(true);//Sets Navigation bar to Highlight Home initially
    }

    /**The Daily Notifications need to be set when the application is run, so this function is run when onCreate is ran
     * and is responsible for setting the alarmManager for the notifications.
     *
     * @param hour - hour the notification is to trigger
     * @param minute - minute the notification is to trigger
     * @param title - title of the notification
     * @param description - description of the notification */
    public void setDailyNotification(int hour, int minute, String title, String description){

        AlarmManager manager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);

        Intent dailyIntent = new Intent (MainActivity.this, DailyNotificationReceiver.class);
        dailyIntent.putExtra(GContract.G_TITLE, title);
        dailyIntent.putExtra(GContract.G_DESCRIPTION, description);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, hour, dailyIntent, 0);

        manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);

    }

    /**onSavedInstanceState is called before the activity is destroyed. This activity handles fragments
     * and bottom navigation, so we want to save the state of the fragments before the activity is destroyed.
     *
     * @param savedInstanceState - parameter that is passed to the onCreate of every Android Activity
     * */
    @Override
    public void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putInt("fragmentFlag", fragmentFlag);

        fragmentManager.putFragment(savedInstanceState, "homeFragment", homeFragment);
        fragmentManager.putFragment(savedInstanceState, "goalFragment", goalFragment);
        fragmentManager.putFragment(savedInstanceState, "breathFragment", breathFragment);
        fragmentManager.putFragment(savedInstanceState, "journalParentFragment", journalParentFragment);

    }

    /**onRestoreInstance state restores the activity and child fragments to their original state before the
     * activity was destroyed. This will help to keep continuity in the user experience.
     * @param savedInstanceState - parameter that is passed to the onCreate of every Android Activity
     * */
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.hide(homeFragment);
        fragmentTransaction.hide(goalFragment);
        fragmentTransaction.hide(breathFragment);
        fragmentTransaction.hide(journalParentFragment);

        fragmentFlag = savedInstanceState.getInt("fragmentFlag");

        switch(fragmentFlag){
            case 0:
                fragmentTransaction.show(breathFragment);
                bottomNav.getMenu().getItem(0).setChecked(true);
                break;
            case 1:
                fragmentTransaction.show(homeFragment);
                bottomNav.getMenu().getItem(1).setChecked(true);
                break;
            case 2:
                fragmentTransaction.show(journalParentFragment);
                bottomNav.getMenu().getItem(2).setChecked(true);
                break;
            case 3:
                fragmentTransaction.show(goalFragment);
                bottomNav.getMenu().getItem(3).setChecked(true);
                break;
        }
    }

    /**This is where the bottom navigation is handled
    * When the user selects an option of the navigation bar their selection is checked in the switch statement
    * The corresponding Fragment is then launched*/
    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                    switch(menuItem.getItemId()){
                        case R.id.nav_breath:
                            Log.d("g53mdp", "In Breath");
                            fragmentManager.beginTransaction().hide(active).show(breathFragment).commit();
                            active = breathFragment;
                            fragmentFlag = 0;
                            return true;
                        case R.id.nav_home:
                            Log.d("g53mdp", "In Home");
                            fragmentManager.beginTransaction().hide(active).show(homeFragment).commit();
                            active = homeFragment;
                            fragmentFlag = 1;
                            return true;
                        case R.id.nav_journal:
                            Log.d("g53mdp", "In Journal");
                            fragmentManager.beginTransaction().hide(active).show(journalParentFragment).commit();
                            active = journalParentFragment;
                            fragmentFlag = 2;
                            return true;
                        case R.id.nav_goals:
                            Log.d("g53mdp", "In Goals");
                            fragmentManager.beginTransaction().hide(active).show(goalFragment).commit();
                            active = goalFragment;
                            fragmentFlag = 3;
                            return true;
                    }

                    return true;
                }
            };

}
