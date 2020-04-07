package com.example.diss_cbt_application;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.diss_cbt_application.BreathFeature.BreathFragment;
import com.example.diss_cbt_application.GoalsFeature.GNewEditGoal;
import com.example.diss_cbt_application.GoalsFeature.GoalsFragment;
import com.example.diss_cbt_application.HomeFeature.HomeFragment;
import com.example.diss_cbt_application.JournalFeature.JActivities_Fragments.Entries.JournalChooseActivity;
import com.example.diss_cbt_application.JournalFeature.JActivities_Fragments.General.JournalParentFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

/**This is the MainActivity of the Application, so is the first Activity that gets loaded
 * The Main Activity holds 4 fragments which represent the 4 features of the application
 *      - Home Fragment - the first one that is loaded
 *      - Journal Fragment
 *      - Goals Fragment
 *      - Breath Fragment
 *This activity hols the bottom navigation bar to switch between these fragments/features*/
public class MainActivity extends AppCompatActivity {

    public static final int ADD_GOAL_REQUEST = 1;
    public static final int EDIT_GOAL_REQUEST = 2;

    int count, breathBoolean, fragmentFlag;
    long startTime;
    TextView breathTextView, instruction;
    Button breathButton;

    final FragmentManager fragmentManager = getSupportFragmentManager();
    Fragment homeFragment = new HomeFragment();
    Fragment goalFragment = new GoalsFragment();
    Fragment breathFragment = new BreathFragment();
    Fragment journalParentFragment = new JournalParentFragment();
    Fragment active = homeFragment;

    BottomNavigationView bottomNav;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        if(savedInstanceState == null ){//First time, create fragments

            fragmentManager.beginTransaction().add(R.id.fragment_container, goalFragment, "homeFragment")
                    .hide(goalFragment).commit();
            fragmentManager.beginTransaction().add(R.id.fragment_container, journalParentFragment, "journalParentFragment")
                    .hide(journalParentFragment).commit();
            fragmentManager.beginTransaction().add(R.id.fragment_container, breathFragment, "breathFragment")
                    .hide(breathFragment).commit();
            fragmentManager.beginTransaction().add(R.id.fragment_container, homeFragment, "homeFragment").commit();

        }
        else{

            //if there is already an instance state retireve them from the amanger with the key you give them

            homeFragment = fragmentManager.getFragment(savedInstanceState, "homeFragment");
            goalFragment = fragmentManager.getFragment(savedInstanceState, "goalFragment");
            breathFragment = fragmentManager.getFragment(savedInstanceState, "breathFragment");
            journalParentFragment = fragmentManager.getFragment(savedInstanceState, "journalParentFragment");

        }


        breathBoolean = 0;

        bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        bottomNav.getMenu().getItem(1).setChecked(true);//Sets Navigation bar to Highlight Home initially
    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putInt("fragmentFlag", fragmentFlag);

        fragmentManager.putFragment(savedInstanceState, "homeFragment", homeFragment);
        fragmentManager.putFragment(savedInstanceState, "goalFragment", goalFragment);
        fragmentManager.putFragment(savedInstanceState, "breathFragment", breathFragment);
        fragmentManager.putFragment(savedInstanceState, "journalParentFragment", journalParentFragment);

    }

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

    /*This is where the bottom navigation is handled
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


    /**Simple function that launches an Activity when the user wants to create a new journal entry*/
    public void newEntryOnClick(View v){
        Intent i_choose_journal = new Intent(MainActivity.this, JournalChooseActivity.class);
        startActivity(i_choose_journal);
    }

    /**When the user clicks on the '+' button on the Goals Fragment this function is called
     * It sends an intent to the 'GNewGoals' Activity where the user can define a new goal
     *
     * @param v - the function is parsed the view */
    public void newGoalOnClick(View v){

        /*Define the intent that will be sent from the MainActivity to the New Goal Page*/
        Intent i_new_goal = new Intent(MainActivity.this, GNewEditGoal.class);
        startActivityForResult(i_new_goal, ADD_GOAL_REQUEST);
    }

    //runs without a timer by reposting this handler at the end of the runnable
    Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {

        @Override
        public void run() {
            long millis = System.currentTimeMillis() - startTime;
            int seconds = (int) (millis / 1000);
            int minutes = seconds / 60;
            seconds = seconds % 60;

            breathTextView.setText("" + seconds);
            breathCases(seconds);

            timerHandler.postDelayed(this, 1000);
        }
    };

    private void breathCases(int seconds){

        instruction = findViewById(R.id.tv_breath_instruction);
        int value = seconds % 13;

        switch(value){
            case 0:
                breathTextView.setText("4");
                instruction.setText("Breath in");
                break;
            case 1:
                breathTextView.setText("3");
                break;
            case 2:
                breathTextView.setText("2");
                break;
            case 3:
                breathTextView.setText("1");
                break;
            case 4:
                breathTextView.setText("2");
                instruction.setText("Hold");
            case 5:
                breathTextView.setText("1");
                break;
            case 6:
                breathTextView.setText("5");
                instruction.setText("Breath Out");
            case 7:
                breathTextView.setText("4");
                break;
            case 8:
                breathTextView.setText("3");
                break;
            case 9:
                breathTextView.setText("2");
                break;
            case 10:
                breathTextView.setText("1");
                break;
            case 11:
                breathTextView.setText("2");
                instruction.setText("Hold");
                break;
            case 12:
                breathTextView.setText("1");
                break;
        }


    }


    public void startBreathOnClick(View v){

        //startTime = (Date) Calendar.getInstance().getTime();
        breathTextView = findViewById(R.id.tv_breath_number);
        startTime = System.currentTimeMillis();;

        breathButton = (Button) findViewById(R.id.bt_breath_start_stop);

        if(breathBoolean == 0){
            startTime = System.currentTimeMillis();;
            timerHandler.postDelayed(timerRunnable, 0);

            breathBoolean = 1;
            breathButton.setText("Stop");

        }
        else{

            count = 0;
            timerHandler.removeCallbacks(timerRunnable);
            breathButton.setText("Start");
            breathBoolean = 0;

        }

    }

    /**This function handles request and result codes back from the MainActivity
     * Due to the Fragment structure there are a few different results that can be returned
     * so these are handled by this function*/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == ADD_GOAL_REQUEST && resultCode == RESULT_OK){
            Log.d("Diss", "Value of request code: " + requestCode);
            Log.d("Diss", "Value of result code: " + resultCode);


            String title = data.getStringExtra(GNewEditGoal.EXTRA_TITLE);
            String description = data.getStringExtra(GNewEditGoal.EXTRA_DESCRIPTION);
            String date = data.getStringExtra(GNewEditGoal.EXTRA_DATE);
            String time = data.getStringExtra(GNewEditGoal.EXTRA_TIME);
            int markedComplete = data.getIntExtra(GNewEditGoal.EXTRA_MC, 0);

//            GoalObject goal = new GoalObject(title, description, date, time, markedComplete);
//            GoalViewModel.insert(goal);


            Toast.makeText(this, "Goal Saved", Toast.LENGTH_SHORT).show();
        }
        else if(requestCode == EDIT_GOAL_REQUEST && resultCode == RESULT_OK){

//            Long id = data.getLongExtra(GNewEditGoal.EXTRA_ID, -1);

//            if(id == -1){
//                Toast.makeText(this, "Goal can't be updated", Toast.LENGTH_SHORT).show();
//                return;
//            }
//
//            String title = data.getStringExtra(GNewEditGoal.EXTRA_TITLE);
//            String description = data.getStringExtra(GNewEditGoal.EXTRA_DESCRIPTION);
//            String date = data.getStringExtra(GNewEditGoal.EXTRA_DATE);
//            String time = data.getStringExtra(GNewEditGoal.EXTRA_TIME);
//            int markedComplete = data.getIntExtra(GNewEditGoal.EXTRA_MC, 0);
//
//            GoalObject goal = new GoalObject(title, description, date, time, markedComplete);
//            goal.setId(id);
//            GoalViewModel.update(goal);

            Toast.makeText(this, "Goal Updated", Toast.LENGTH_SHORT).show();


        }
        else{
            Log.d("Diss", "Value of request code: " + requestCode);
            Log.d("Diss", "Value of result code: " + resultCode);

            Toast.makeText(this, "Goal Not Saved", Toast.LENGTH_SHORT).show();
        }
    }
}
