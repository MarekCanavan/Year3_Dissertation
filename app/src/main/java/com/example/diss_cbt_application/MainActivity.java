package com.example.diss_cbt_application;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
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


public class MainActivity extends AppCompatActivity {


    public static final int ADD_GOAL_REQUEST = 1;
    public static final int EDIT_GOAL_REQUEST = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
    }

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



    public void newEntryOnClick(View v){
        Intent i_choose_journal = new Intent(MainActivity.this, JournalChooseActivity.class);
        startActivity(i_choose_journal);
    }

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
