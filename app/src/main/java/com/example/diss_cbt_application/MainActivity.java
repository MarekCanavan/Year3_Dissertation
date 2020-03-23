package com.example.diss_cbt_application;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

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
import com.example.diss_cbt_application.GoalsFeature.GoalObject;
import com.example.diss_cbt_application.GoalsFeature.GoalViewModel;
import com.example.diss_cbt_application.GoalsFeature.GoalsFragment;
import com.example.diss_cbt_application.HomeFeature.HomeFragment;
import com.example.diss_cbt_application.JournalFeature.JActivities_Fragments.JournalChooseActivity;
import com.example.diss_cbt_application.JournalFeature.JActivities_Fragments.JournalFragment;
import com.example.diss_cbt_application.JournalFeature.JActivities_Fragments.JournalsMyJActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.sql.Date;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

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

    int count, breathBoolean;
    long startTime;
    TextView breathTextView;
    Button breathButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        breathBoolean = 0;

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
    }

    /*This is where the bottom navigation is handled
    * When the user selects an option of the navigation bar their selection is checked in the switch statement
    * The corresponding Fragment is then launched*/
    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    Fragment selectedFragment = null;

                    switch(menuItem.getItemId()){
                        case R.id.nav_home:
                            Log.d("g53mdp", "In Home");
                            selectedFragment = new HomeFragment();
                            break;
                        case R.id.nav_journal:
                            Log.d("g53mdp", "In Journal");
                            selectedFragment = new JournalFragment();
                            break;
                        case R.id.nav_breath:
                            Log.d("g53mdp", "In Breath");
                            selectedFragment = new BreathFragment();
                            break;
                        case R.id.nav_goals:
                            Log.d("g53mdp", "In Goals");
                            selectedFragment = new GoalsFragment();
                            break;
                    }

                    Log.d("g53mdp", "Selected Fragment: + " + selectedFragment );
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            selectedFragment).commit();

                    return true;
                }
            };


    /**Simple function that launches an Activity when the user wants to create a new journal entry*/
    public void newEntryOnClick(View v){
        Intent i_choose_journal = new Intent(MainActivity.this, JournalChooseActivity.class);
        startActivity(i_choose_journal);
    }

    /**Simple function that launches an Activity when the user wants to inspect their already made Journal Structures*/
    public void journalsOnClick(View v){
        Intent i_my_journals = new Intent(MainActivity.this, JournalsMyJActivity.class);
        startActivity(i_my_journals);

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

            timerHandler.postDelayed(this, 500);
        }
    };
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
            breathTextView.setText("" + count);
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

            GoalObject goal = new GoalObject(title, description, date, time, markedComplete);
            GoalViewModel.insert(goal);


            Toast.makeText(this, "Goal Saved", Toast.LENGTH_SHORT).show();
        }
        else if(requestCode == EDIT_GOAL_REQUEST && resultCode == RESULT_OK){

            int id = data.getIntExtra(GNewEditGoal.EXTRA_ID, -1);

            if(id == -1){
                Toast.makeText(this, "Goal Can't be updated", Toast.LENGTH_SHORT).show();
                return;
            }

            String title = data.getStringExtra(GNewEditGoal.EXTRA_TITLE);
            String description = data.getStringExtra(GNewEditGoal.EXTRA_DESCRIPTION);
            String date = data.getStringExtra(GNewEditGoal.EXTRA_DATE);
            String time = data.getStringExtra(GNewEditGoal.EXTRA_TIME);
            int markedComplete = data.getIntExtra(GNewEditGoal.EXTRA_MC, 0);

            GoalObject goal = new GoalObject(title, description, date, time, markedComplete);
            goal.setId(id);
            GoalViewModel.update(goal);

            Toast.makeText(this, "Goal Updated", Toast.LENGTH_SHORT).show();


        }
        else{
            Log.d("Diss", "Value of request code: " + requestCode);
            Log.d("Diss", "Value of result code: " + resultCode);

            Toast.makeText(this, "Goal Not Saved", Toast.LENGTH_SHORT).show();
        }
    }
}
