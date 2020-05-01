package com.example.diss_cbt_application.JournalFeature.JActivities_Fragments.Entries;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.diss_cbt_application.JournalFeature.JDatabase.JDTables.JournalSingleEntryObject;
import com.example.diss_cbt_application.R;

import java.util.ArrayList;
import java.util.List;

/**This class handles the recycler view for when the user is choosing which entry they want to view
 * It is parsed the View Holder we created inside it*/
public class RVAJournalFragement extends RecyclerView.Adapter<RVAJournalFragement.ViewHolder>{

    /*Member variable for the list of all entries*/
    private List<JournalSingleEntryObject> entries = new ArrayList<>();

    /*Member variable for the interface onItemClickListener*/
    private RVAJournalFragement.OnItemClickListener listener;

    /**This class is where we create and return a view holder
     *
     * @return - viewholder */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Responsible for inflating the view

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_journal_fragment,
                parent,
                false);
        ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    /**This class is where we take care of getting the data from the JournalEntry Objects
     * and populate the TextFields with data*/
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        JournalSingleEntryObject currentEntry = entries.get(position);

        holder.entryName.setText(currentEntry.getEntryName());
        holder.entryName.setTextSize(19);
        holder.entryName.setTextColor(Color.BLACK);

        holder.journalName.setText(currentEntry.getJournalType());
        holder.journalName.setTextSize(15);
        holder.journalName.setTextColor(currentEntry.getJournalColour());

        holder.time.setText(currentEntry.getEntryTime());
        holder.date.setText(currentEntry.getEntryDate());

    }

    /**Declaring the onItemClick method
     * Anything that implements this interface has to implement this method*/
    public interface OnItemClickListener{
        void onItemClick(JournalSingleEntryObject entry);
    }

    /**Parse the onItemClick Listener we created in this class above
     * Assign the member variable listener to the listener that is parsed*/
    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    /**This is the function that is called from the Activity
     * A list of entries is parsed (after being retrieved as live data
     * the local entry list is set to this parsed list so that the data can be represented on the recyclerview
     * Then notifyDataSetChanged (an android function) is called*/
    public void setEntries(List <JournalSingleEntryObject> entries){
        this.entries = entries;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return entries.size();
    }

    /**This class holds the views in the single recycler view items*/
    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView entryName, journalName, time, date;
        CardView parentLayout;

        /**Assign the text views and layout in this constructor*/
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            entryName = itemView.findViewById(R.id.tv_entry_name);
            journalName = itemView.findViewById(R.id.tv_journal_name);
            time = itemView.findViewById(R.id.tv_time);
            date = itemView.findViewById(R.id.tv_date);
            parentLayout = itemView.findViewById(R.id.parent_layout);


            /*Parse the listener the entry at the position we are clicking*/
            itemView.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(listener != null){
                        listener.onItemClick(entries.get(position));
                    }
                }
            });
        }
    }
}
