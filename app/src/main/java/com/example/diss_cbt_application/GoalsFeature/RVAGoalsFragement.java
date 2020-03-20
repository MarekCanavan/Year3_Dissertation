package com.example.diss_cbt_application.GoalsFeature;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.diss_cbt_application.R;

import java.util.ArrayList;
import java.util.List;

/**This class is the RecyclerViewAdapter for the Goals Fragment hence (RVAGoalsFragment)
 * Handles the inflating of the view, setting the text and data onto the cards/lists
 **/
public class RVAGoalsFragement extends RecyclerView.Adapter<RVAGoalsFragement.ViewHolder>{


    /*Member variables for list of all goals*/
    private List<GoalObject> goals = new ArrayList<>();

    /*Member variable for the interface onItemClickListener*/
    private OnItemClickListener listener;

    /**This class is where we create and return a view holder
     *
     * @return - viewholder */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Responsible for inflating the view

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_goals_fragment,
                parent,
                false);
        ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    /**This class is where we take care of getting the data from the Goal Objects
     * and populate the textfields with data*/
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        GoalObject currentGoal = goals.get(position);

        holder.gTitle.setText(currentGoal.getTitle());
        holder.time.setText(currentGoal.getTime());
        holder.date.setText(currentGoal.getDate());

    }

    @Override
    public int getItemCount() {
        return goals.size();
    }

    /**This is the function that is called from the Activity
     * A list of goals is parsed (after being retrieved as live data
     * the local goals list is set to this parsed list so that the data can be represented on the recyclerview
     * Then notifyDataSetChanged (an android function) is called*/
    public void setGoals(List<GoalObject> goals){
        this.goals = goals;
        notifyDataSetChanged();//used now for simplicity
    }

    /**Declaring the onItemClick method
     * Anything that implements this interface has to implement this method*/
    public interface OnItemClickListener{
        void onItemClick(GoalObject goal);
        void onDeleteClick(GoalObject goal);
    }

    /**Parse the onItemClick Listener we created in this class above
     * Assign the member variable listener to the listener that is parsed*/
    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    /**This class holds the views in the single recycler view items*/
    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView gTitle, time, date;
        ConstraintLayout parentLayout;
        ImageView mDeleteImage;

        /**Assign the text views and layout in this constructor*/
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            gTitle = itemView.findViewById(R.id.tv_goal_title);
            time = itemView.findViewById(R.id.tv_goal_time);
            date = itemView.findViewById(R.id.tv_goal_date);
            parentLayout = itemView.findViewById(R.id.parent_layout);
            mDeleteImage = itemView.findViewById(R.id.image_delete);

            /*Parse the listener the goal at the position we are clicking*/
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(listener != null && position != RecyclerView.NO_POSITION){
                        listener.onItemClick(goals.get(position));
                    }
                }
            });

            mDeleteImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(listener != null && position != RecyclerView.NO_POSITION){
                        listener.onDeleteClick(goals.get(position));
                    }
                }
            });
        }
    }

}
