package com.example.diss_cbt_application;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RVAGoalsFragement extends RecyclerView.Adapter<RVAGoalsFragement.ViewHolder>{

    private static final String TAG = "RVAGoalsFragement";

    /*Definition of the local ArrayLists in this class, the Arraylists are set in the constructor*/
    private ArrayList<Integer> mIDs = new ArrayList<>();
    private ArrayList<String> mTitle = new ArrayList<>();
    private ArrayList<String> mDate= new ArrayList<>();
    private ArrayList<String> mTime = new ArrayList<>();

    private Context mContext;


    /**
     * This constructor receives all of the ArrayLists as parameters and sets the local arraylists to the values parse
     * */
    public RVAGoalsFragement(ArrayList<Integer> mIDs, ArrayList<String> mTitle, ArrayList<String> mDate,
                             ArrayList<String> mTime, Context mContext) {

        this.mIDs = mIDs;
        this.mTitle = mTitle;
        this.mDate = mDate;
        this.mTime = mTime;


    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Responsible for inflating the view

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_goals_fragment, parent, false);
        ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder called");

        holder.gTitle.setText(mTitle.get(position));
        holder.time.setText(mTime.get(position));
        holder.date.setText(mDate.get(position));



        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, mTitle.get(position), Toast.LENGTH_SHORT).show();

                Log.d("Diss", "Value of id: " + mIDs.get(position));
                //Log.d("Diss", "Value of entryName: " + mEntryNames.get(position));
                //Log.d("Diss", "Value of journalName: " +  mJournalNames.get(position));

                //Intent i_goal = new Intent(mContext,);

                //Bundle goalsBundle = new Bundle();

                //mContext.startActivity();

            }
        });

    }


    @Override
    public int getItemCount() {
        return mIDs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView gTitle, time, date;
        ConstraintLayout parentLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            gTitle = itemView.findViewById(R.id.tv_goal_title);
            time = itemView.findViewById(R.id.tv_goal_time);
            date = itemView.findViewById(R.id.tv_goal_date);
            parentLayout = itemView.findViewById(R.id.parent_layout);
        }
    }
}
