package com.example.diss_cbt_application.JournalFeature;

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

import com.example.diss_cbt_application.R;

import java.util.ArrayList;

/**
 * */
public class RVAJournalFragement extends RecyclerView.Adapter<RVAJournalFragement.ViewHolder>{

    private static final String TAG = "RVAJournalFragement";

    /*Definition of the local ArrayLists in this class, the Arraylists are set in the constructor*/
    private ArrayList<String> mEntryNames = new ArrayList<>();
    private ArrayList<String> mJournalNames = new ArrayList<>();
    private ArrayList<String> mEntryTimes = new ArrayList<>();
    private ArrayList<String> mEntryDates = new ArrayList<>();
    private ArrayList<Integer> mIDs = new ArrayList<>();
    private ArrayList<Integer> mJournalColour = new ArrayList<>();
    private Context mContext;


    /**
     * This constructor receives all of the ArrayLists as parameters and sets the local arraylists to the values parse
     * */
    public RVAJournalFragement(ArrayList<Integer> mIDs, ArrayList<String> mEntryNames, ArrayList<String> mJournalNames,
                               ArrayList<String> mEntryTimes, ArrayList<String> mEntryDates, ArrayList<Integer> mJournalColour,  Context mContext) {
        this.mIDs = mIDs;
        this.mEntryNames = mEntryNames;
        this.mJournalNames = mJournalNames;
        this.mEntryTimes = mEntryTimes;
        this.mEntryDates = mEntryDates;
        this.mContext = mContext;
        this.mJournalColour = mJournalColour;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Responsible for inflating the view

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_journal_fragment, parent, false);
        ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder called");

        holder.entryName.setText(mEntryNames.get(position));
        holder.entryName.setTextSize(19);
        holder.entryName.setTextColor(Color.BLACK);

        holder.journalName.setText(mJournalNames.get(position));
        holder.journalName.setTextSize(15);
        holder.journalName.setTextColor(mJournalColour.get(position));

        holder.time.setText(mEntryTimes.get(position));
        holder.date.setText(mEntryDates.get(position));

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, mEntryNames.get(position), Toast.LENGTH_SHORT).show();

                Log.d("Diss", "Value of id: " + mIDs.get(position));
                //Log.d("Diss", "Value of entryName: " + mEntryNames.get(position));
                //Log.d("Diss", "Value of journalName: " +  mJournalNames.get(position));

                Intent i_entry = new Intent(mContext, JournalEntryData.class);

                Bundle entryBundle = new Bundle();
                entryBundle.putInt(JournalContract._ID, mIDs.get(position));
                entryBundle.putString(JournalContract.ENTRY_NAME, mEntryNames.get(position));
                entryBundle.putString(JournalContract.JOURNAL_NAME, mJournalNames.get(position));
                entryBundle.putString(JournalContract.ENTRY_DATE, mEntryDates.get(position));
                entryBundle.putString(JournalContract.ENTRY_TIME, mEntryTimes.get(position));
                entryBundle.putInt(JournalContract.JOURNAL_COLOUR, mJournalColour.get(position));
                i_entry.putExtras(entryBundle);

                Log.d("Diss", "Value of _id before sending: " + mIDs.get(position));

                mContext.startActivity(i_entry);

            }
        });

    }


    @Override
    public int getItemCount() {
        return mEntryNames.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView entryName, journalName, time, date;
        ConstraintLayout parentLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            entryName = itemView.findViewById(R.id.tv_entry_name);
            journalName = itemView.findViewById(R.id.tv_journal_name);
            time = itemView.findViewById(R.id.tv_time);
            date = itemView.findViewById(R.id.tv_date);
            parentLayout = itemView.findViewById(R.id.parent_layout);
        }
    }
}
