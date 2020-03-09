package com.example.diss_cbt_application.JournalFeature;

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
import androidx.recyclerview.widget.RecyclerView;

import com.example.diss_cbt_application.JournalFeature.JActivities_Fragments.JournalCompleteEntryActivity;
import com.example.diss_cbt_application.JournalFeature.JDatabase.JDTables.JournalObject;
import com.example.diss_cbt_application.R;

import java.util.ArrayList;
import java.util.List;

public class RVAChooseJournal extends RecyclerView.Adapter<RVAChooseJournal.ViewHolder>{

    private static final String TAG = "RVAChooseJournal";

    private ArrayList<String> mJournalNames = new ArrayList<>();
    private ArrayList<Integer> mIDs = new ArrayList<>();
    private ArrayList<Integer> mJournalColours = new ArrayList<>();
    private Context mContext;

    private List<JournalObject> journals = new ArrayList<>();
    int LAUNCH_JOURNAL_CHOOSE_ACTIVITY = 1;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Responsible for inflating the view

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_choose_journal,
                parent, false);

        ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder called");

        JournalObject currentJournal = journals.get(position);

        holder.entryName.setText(currentJournal.getJournalName());
        holder.entryName.setTextColor(currentJournal.getJournalColour());


        Log.d("Diss", "Value of Text: " + currentJournal.getJournalName());
        Log.d("Diss", "Value of Colour: " + currentJournal.getJournalColour());



        /*holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, mJournalNames.get(position), Toast.LENGTH_SHORT).show();

                Log.d("Diss", "Value of if: " + mIDs.get(position));
                Intent i_complete_journal = new Intent(mContext, JournalCompleteEntryActivity.class);

                Bundle journalBundle = new Bundle();
                journalBundle.putInt(JournalContract._ID, mIDs.get(position));
                journalBundle.putString("mJournalNames", mJournalNames.get(position));
                journalBundle.putInt(JournalContract.JOURNAL_COLOUR, mJournalColours.get(position));
                i_complete_journal.putExtras(journalBundle);

                mContext.startActivity(i_complete_journal);

            }
        });*/

    }

    public void setJournals(List<JournalObject> journals){
        this.journals = journals;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return journals.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView entryName;
        RelativeLayout parentLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            entryName = itemView.findViewById(R.id.tv_main);
            parentLayout = itemView.findViewById(R.id.parent_layout);

        }
    }
}
