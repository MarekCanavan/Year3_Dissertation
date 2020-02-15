package com.example.diss_cbt_application;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

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

}
