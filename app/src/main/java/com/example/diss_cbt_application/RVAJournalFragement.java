package com.example.diss_cbt_application;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class RVAJournalFragement extends RecyclerView.Adapter<RVAJournalFragement.ViewHolder>{

    private static final String TAG = "RVAJournalFragement";

    private ArrayList<String> mEntryNames = new ArrayList<>();
    private ArrayList<String> mJournalNames = new ArrayList<>();
    private ArrayList<String> mEntryTimes = new ArrayList<>();
    private ArrayList<Integer> mIDs = new ArrayList<>();
    private Context mContext;


    public RVAJournalFragement(ArrayList<Integer> mIDs, ArrayList<String> mEntryNames, ArrayList<String> mJournalNames,
                               ArrayList<String> mEntryTimes, Context mContext) {
        this.mIDs = mIDs;
        this.mEntryNames = mEntryNames;
        this.mJournalNames = mJournalNames;
        this.mEntryTimes = mEntryTimes;
        this.mContext = mContext;

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

        holder.journalName.setText(mJournalNames.get(position));

        holder.time.setText(mEntryTimes.get(position));

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Toast.makeText(mContext, mImageNames.get(position), Toast.LENGTH_SHORT).show();

                Log.d("Diss", "Value of if: " + mIDs.get(position));
                Intent i_complete_journal = new Intent(mContext, JournalCompleteEntryActivity.class);

                Bundle recipeBundle = new Bundle();
                recipeBundle.putInt("_id", mIDs.get(position));
                i_complete_journal.putExtras(recipeBundle);

                mContext.startActivity(i_complete_journal);*/

            }
        });

    }

    @Override
    public int getItemCount() {
        return mEntryNames.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView entryName, journalName, time;
        ConstraintLayout parentLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            entryName = itemView.findViewById(R.id.tv_entry_name);
            journalName = itemView.findViewById(R.id.tv_journal_name);
            time = itemView.findViewById(R.id.tv_time);
            parentLayout = itemView.findViewById(R.id.parent_layout);

        }
    }
}
