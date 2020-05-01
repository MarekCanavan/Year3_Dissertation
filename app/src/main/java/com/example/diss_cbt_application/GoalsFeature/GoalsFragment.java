package com.example.diss_cbt_application.GoalsFeature;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.diss_cbt_application.GoalsFeature.GDatabase.GoalObject;
import com.example.diss_cbt_application.GoalsFeature.GDatabase.GoalViewModel;
import com.example.diss_cbt_application.Utils.MainActivity;
import com.example.diss_cbt_application.Notifications.AlertReceiver;
import com.example.diss_cbt_application.R;
import com.example.diss_cbt_application.Utils.VerticalSpaceItemDecoration;

import java.util.List;

/**This Activity presents the user their predefined goals in a Recycler View on the fragment
 * The user has the option of clicking a specific goal - to which they will be taken to a new activity - and inspecting it
 * or setting a new goal by clicking the '+' button, which will launch a new activity where they can set a new goal
 * */
public class GoalsFragment extends Fragment implements View.OnClickListener {

    /*Member variable for the goalViewModel*/
    private GoalViewModel goalViewModel;

    public GoalsFragment() {
        super();
    }

    /**
     * Sets up the Fragment when the user first navigates to it
     * Populates the RecyclerView
     * Draws a plus on the Button for styling
     * Handles when the user clicks on a goal, by packaging the goal information in an intent and sending to the next activity.
     * Also handles if the user wants to delete a goal by clicking on the 'delete' button on a card.
     *
     * @return RootView - which is the View in which all the other views are placed*/
    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Log.d("g53mdp", "In Breath Create View");

        View rootView = inflater.inflate(R.layout.fragment_goals, container, false);

        /*Adds the '+' image in drawable folder to the button for a New Entry*/
        ImageButton newEntry = rootView.findViewById(R.id.bt_new_goal);
        newEntry.setImageResource(R.mipmap.ic_addition_button_dark_blue_round);
        newEntry.setOnClickListener(this);

        /*Initialise Recycler View*/
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view_goals_fragment);//get reference to recycler view
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext())); //every recycler view needs a layout manager
        recyclerView.setHasFixedSize(true); //Makes recycler view more efficient, we know card size wont change

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

        /*Handles when a user clicks on a card in the recycler view
        * Sends the object information in an intent so it can be used to populate the next activity*/
        adapter.setOnItemClickListener(new RVAGoalsFragement.OnItemClickListener() {
            @Override
            public void onItemClick(GoalObject goal) {
                Intent intent = new Intent(getContext(), GNewEditGoal.class);

                intent.putExtra(GContract.EXTRA_ID, goal.getId());
                intent.putExtra(GContract.EXTRA_TITLE, goal.getTitle());
                intent.putExtra(GContract.EXTRA_DESCRIPTION, goal.getDescription());
                intent.putExtra(GContract.EXTRA_DATE, goal.getDate());
                intent.putExtra(GContract.EXTRA_TIME, goal.getTime());
                intent.putExtra(GContract.EXTRA_TITLE, goal.getTitle());
                intent.putExtra(GContract.EXTRA_REPEAT, goal.getRepeat());
                intent.putExtra(GContract.EXTRA_MC, goal.getMarkedComplete());
                intent.putExtra(GContract.EXTRA_REQUEST_CODE, goal.getAlarmRequestCode());
                intent.putExtra(GContract.EXTRA_UPDATE, GContract.EXTRA_UPDATE);

                getActivity().startActivity(intent);
            }


            /*If the user wants to delete a specific goal this onClick is called
            * A dialog box is shown to confirm that the user wants to delete the specific goal*/
            @Override
            public void onDeleteClick(GoalObject goal) {
                AlertDialog diaBox = AskOption(goal);
                diaBox.show();
            }
        });

        return rootView;
    }

    /**This AlertDialog box is created when the user indicates they want to delete a goal. The check is done
     * to confirm they want to delete that goal. So 2 options are generated, if the user chooses to cancel then the
     * dialog box is dismissed. If the user confirms they want to delete then the logic is triggered to delete the goal,
     * this involves deleting the alarm associated to it and deleting the goal from the database.
     *
     * @param - goal - the goal that is to be deleted*/
    private AlertDialog AskOption(final GoalObject goal){
        AlertDialog myDeletingDialogBox = new AlertDialog.Builder(getContext())
                .setTitle("Delete")
                .setMessage("Are you sure you want to delete?")
                .setIcon(R.drawable.ic_delete_black)
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
                        Intent alarmIntent = new Intent(getContext(), AlertReceiver.class);

                        /*Passing the context, the request code needs to be unique - so pass the id of the goal, the intent and any flags (which is 0)*/
                        PendingIntent sender = PendingIntent.getBroadcast(getContext(), goal.getAlarmRequestCode(), alarmIntent, 0 );

                        alarmManager.cancel(sender);

                        goalViewModel.delete(goal);
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();

        return myDeletingDialogBox;
    }

    /**When the user clicks on the '+' button on the Goals Fragment this function is called
     * It sends an intent to the 'GNewGoals' Activity where the user can define a new goal
     *
     * @param v - the function is parsed the view */
    public void onClick(View v){
        /*Define the intent that will be sent from the MainActivity to the New Goal Page*/
        Intent i_new_goal = new Intent(getContext(), GNewEditGoal.class);
        startActivity(i_new_goal);
    }
}
