package com.example.diss_cbt_application.JournalFeature.JActivities_Fragments.MyJournals;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.diss_cbt_application.JournalFeature.JDatabase.JDTables.JournalObject;
import com.example.diss_cbt_application.JournalFeature.JDatabase.JDTables.JournalSingleEntryObject;
import com.example.diss_cbt_application.R;

import java.util.ArrayList;
import java.util.List;

/**This class handles the recycler view for when the user is choosing which entry they want to view
 * It is parsed the View Holder we created inside it*/
public class RVAMyJournals extends RecyclerView.Adapter<RVAMyJournals.ViewHolder>{


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

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_my_journal_fragment,
                parent,
                false);
        ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        /*Reference to the current journal at this position*/
        JournalObject currentJournal = journals.get(position);

        holder.journalName.setText(currentJournal.getJournalName());
        holder.journalName.setTextColor(currentJournal.getJournalColour());


    }

    /**Parse the onItemClick Listener we created in this class above
     * Assign the member variable listener to the listener that is parsed*/
    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    /**Declaring the onItemClick method
     * Anything that implements this interface has to implement this method*/
    public interface OnItemClickListener{
        void onItemClick(JournalObject entry);
    }
    /**This is the function that is called from the Activity
     * A list of journals is parsed (after being retrieved as live data
     * the local journals list is set to this parsed list so that the data can be represented on the recyclerview
     * Then notifyDataSetChanged (an android function) is called*/
    public void setJournals(List<JournalObject> journals){
        this.journals = journals;
        notifyDataSetChanged();
    }

    public int getItemCount(){
        return journals.size();
    }
    /**This class holds the views in the single recycler view items*/
    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView journalName, entryNumber;
        CardView parentLayout;

        /**Assign the text views and layout in this constructor*/
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            journalName = itemView.findViewById(R.id.tv_journal_name);
            entryNumber = itemView.findViewById(R.id.tv_entry_number);

            /*Parse the listener the entry at the position we are clicking*/
            itemView.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(listener != null){
                        listener.onItemClick(journals.get(position));
                    }
                }
            });
        }
    }
}
