package com.example.raja.questhunt;

/**
 * Created by Raja on 4/7/2018.
 */


import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;


/**
 * Created by admin on 3/26/2017.
 */

public class CompletedRecyclerAdapter extends RecyclerView.Adapter<CompletedViewHolder>{
    CompletedQuests comAct;
    private List<Quest> list;

    public CompletedRecyclerAdapter(List<Quest> list, CompletedQuests ca){
        this.list=list;
        this.comAct=ca;
    }


    @Override
    public CompletedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.com_quest_layout, parent, false);
        itemView.setOnClickListener(comAct);
        itemView.setOnLongClickListener(comAct);
        return new CompletedViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CompletedViewHolder holder, int position) {
        Quest quest=list.get(position);
        holder.com_questTV.setText(quest.getQuest_name());
        int difficulty = quest.getQuest_difficulty();
        if(difficulty == 0){
            holder.com_questTV.setTextColor(ContextCompat.getColor(comAct, R.color.easy));
        }
        else if(difficulty == 1){
            holder.com_questTV.setTextColor(ContextCompat.getColor(comAct, R.color.medium));
        }
        else if(difficulty == 2){
            holder.com_questTV.setTextColor(ContextCompat.getColor(comAct, R.color.hard));
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
