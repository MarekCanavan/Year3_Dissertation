package com.example.diss_cbt_application.GoalsFeature;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.diss_cbt_application.DatabaseHelper;
import com.example.diss_cbt_application.MainActivity;
import com.example.diss_cbt_application.Notifications.AlertReceiver;
import com.example.diss_cbt_application.R;
import com.example.diss_cbt_application.VerticalSpaceItemDecoration;

import java.io.Serializable;
import java.util.List;

/**This Activity presents the user their predefined goals in a Recycler View on the fragment
 * The user has the option of clicking a specific goal - to which they will be taken to a new activity - and inspecting it
 * or setting a new goal by clicking the '+' button, which will launch a new activity where they can set their goal*/
public class GoalsFragment extends Fragment {

    /*Member variable for the goalViewModel*/
    private GoalViewModel goalViewModel;

    public ImageView mDeleteImage;
    int iId;

    public GoalsFragment() {
        super();
    }

    /**
     * Sets up the Fragment when the user first navigates to it
     * Populates the RecyclerView
     * Draws a plus on the Button for styling
     *
     * @return RootView which is the View in which all the other views are placed*/
    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Log.d("g53mdp", "In Breath Create View");

        View rootView = inflater.inflate(R.layout.fragment_goals, container, false);

        /*Adds the '+' image in drawable folder to the button for a New Entry*/
        ImageButton newEntry = rootView.findViewById(R.id.bt_new_goal);
        newEntry.setImageResource(R.mipmap.ic_addition_button_dark_blue_round);

        /*Initialise Recycler View*/
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view_goals_fragment);//get reference to recycler view
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext())); //every recycler view needs a layout manager
        recyclerView.setHasFixedSize(true); //Makes recycler view more efficient, we know card size wont change
        recyclerView.addItemDecoration(new VerticalSpaceItemDecoration(5));//Call to VerticalSpaceItemDecoration adds barrier between entries in RV for styling

        final RVAGoalsFragement adapter = new RVAGoalsFragement(); //new adapter
        recyclerView.setAdapter(adapter);//by default this is empty, it gets set in the observer below

        /* Get reference to the Goal View Model - member variable
         * Then observe the changes to the database, specifically the 'getAllGoals' Query
         * Update the recycler with these objects when a change is made to the database */
        goalViewModel = ViewModelProviders.of(this).get(GoalViewModel.class);
        goalViewModel.getAllGoals().observe(this, new Observer<List<GoalObject>>() {
            @Override
            public void onChanged(List<GoalObject> goalObjects) {
                adapter.setGoals(goalObjects);
            }
        });

        /*Handles when a user clicks on a card in the recycler view*/
        adapter.setOnItemClickListener(new RVAGoalsFragement.OnItemClickListener() {
            @Override
            public void onItemClick(GoalObject goal) {
                Intent intent = new Intent(getContext(), GNewEditGoal.class);

                intent.putExtra(GNewEditGoal.EXTRA_ID, goal.getId());
                intent.putExtra(GNewEditGoal.EXTRA_TITLE, goal.getTitle());
                intent.putExtra(GNewEditGoal.EXTRA_DESCRIPTION, goal.getDescription());
                intent.putExtra(GNewEditGoal.EXTRA_DATE, goal.getDate());
                intent.putExtra(GNewEditGoal.EXTRA_TIME, goal.getTime());
                intent.putExtra(GNewEditGoal.EXTRA_TITLE, goal.getTitle());
                intent.putExtra(GNewEditGoal.EXTRA_MC, goal.getMarkedComplete());
                intent.putExtra(GNewEditGoal.EXTRA_UPDATE, GNewEditGoal.EXTRA_UPDATE);

                getActivity().startActivityForResult(intent, MainActivity.EDIT_GOAL_REQUEST);
            }


            /*TODO: COMMENT */
            @Override
            public void onDeleteClick(GoalObject goal) {

                AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
                Intent alarmIntent = new Intent(getContext(), AlertReceiver.class);

                /*Passing the context, the request code needs to be unique - so pass the id of the goal, the intent and any flags (which is 0)*/
                PendingIntent sender = PendingIntent.getBroadcast(getContext(), goal.getAlarmRequestCode(), alarmIntent, 0 );


                /*TODO: COMMENT */
                alarmManager.cancel(sender);

                goalViewModel.delete(goal);
            }
        });

        return rootView;
    }

}
