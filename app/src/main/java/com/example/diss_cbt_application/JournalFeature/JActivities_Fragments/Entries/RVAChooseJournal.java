package com.example.diss_cbt_application.JournalFeature.JActivities_Fragments.Entries;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.diss_cbt_application.JournalFeature.JDatabase.JDTables.JournalObject;
import com.example.diss_cbt_application.R;

import java.util.ArrayList;
import java.util.List;

/**This class handles the recycler view for when the user is choosing which journal they want to complete and entry for
 * It is parsed the View Holder we created inside it*/
public class RVAChooseJournal extends RecyclerView.Adapter<RVAChooseJournal.ViewHolder>{


    /*Member variable for the list of journals*/
    private List<JournalObject> journals = new ArrayList<>();

    /*Member variable for the interface onItemClickListener*/
    private OnItemClickListener listener;


    /**This class is where we create and return a view holder
    *
    * @return - viewholder */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Responsible for inflating the view

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_choose_journal,
                parent,
                false);

        ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    /**This class is where we take care of getting the data from the Journal Structure Objects
    * and populate the TextFields with data*/
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {


        /*Reference to the current journal at this position*/
        JournalObject currentJournal = journals.get(position);

        holder.entryName.setText(currentJournal.getJournalName());
        holder.entryName.setTextColor(currentJournal.getJournalColour());

    }

    /**This is the function that is called from the Activity
     * A list of journals is parsed (after being retrieved as live data
     * the local journals list is set to this parsed list so that the data can be represented on the recyclerview
     * Then notifyDataSetChanged (an android function) is called*/
    public void setJournals(List<JournalObject> journals){
        this.journals = journals;
        notifyDataSetChanged();
    }

    /**Declaring the onItemClick method
     * Anything that implements this interface has to implement this method*/
    public interface OnItemClickListener{
        void onItemClick(JournalObject journal);
    }


    /**Parse the onItemClick Listener we created in this class above
     * Assign the member variable listener to the listener that is parsed*/
    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    @Override
    public int getItemCount() {
        return journals.size();
    }

    /**This class holds the views in the single recycler view items*/
    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView entryName;
        RelativeLayout parentLayout;

        /**Assign the text views and layout in this constructor*/
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            entryName = itemView.findViewById(R.id.tv_main);
            parentLayout = itemView.findViewById(R.id.parent_layout);

                /*Parse the listener the journal at the position we are clicking*/
                itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(listener != null){//Check to avoid crash is listener is not set
                        listener.onItemClick(journals.get(position));
                    }
                }
            });
        }
    }
}
