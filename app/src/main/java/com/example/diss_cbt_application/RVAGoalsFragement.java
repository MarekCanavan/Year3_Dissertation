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
import java.util.List;

public class RVAGoalsFragement extends RecyclerView.Adapter<RVAGoalsFragement.ViewHolder>{

    private static final String TAG = "RVAGoalsFragement";

    /*Definition of the local ArrayLists in this class, the Arraylists are set in the constructor*/
    private ArrayList<Integer> mIDs = new ArrayList<>();
    private ArrayList<String> mTitle = new ArrayList<>();
    private ArrayList<String> mDate= new ArrayList<>();
    private ArrayList<String> mTime = new ArrayList<>();

    private List<GoalObject> goals = new ArrayList<>();
    private OnItemClickListener listener;

    private Context mContext;



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

        GoalObject currentGoal = goals.get(position);


        holder.gTitle.setText(currentGoal.getTitle());
        holder.time.setText(currentGoal.getTime());
        holder.date.setText(currentGoal.getDate());



    }


    @Override
    public int getItemCount() {
        return goals.size();
    }

    public void setGoals(List<GoalObject> goals){
        this.goals = goals;
        notifyDataSetChanged();//used now for simplicity
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

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(listener != null && position != RecyclerView.NO_POSITION){
                        listener.onItemClick(goals.get(position));
                    }
                }
            });
        }
    }

    public interface OnItemClickListener{
        void onItemClick(GoalObject goal);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }
}
