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

public class StandardRecyclerAdapter extends RecyclerView.Adapter<MyViewHolder>{
    StandardActivity stdAct;
    private List<Quest> list;

    public StandardRecyclerAdapter(List<Quest> list, StandardActivity sa){
        this.list=list;
        this.stdAct=sa;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.quest_layout, parent, false);
        itemView.setOnClickListener(stdAct);
        itemView.setOnLongClickListener(stdAct);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Quest quest=list.get(position);
        holder.questTV.setText(quest.getQuest_name());
        holder.deadlineTV.setText(quest.getQuest_deadline());
        int difficulty = quest.getQuest_difficulty();
        if(difficulty == 0){
            holder.questTV.setTextColor(ContextCompat.getColor(stdAct, R.color.easy));
        }
        else if(difficulty == 1){
            holder.questTV.setTextColor(ContextCompat.getColor(stdAct, R.color.medium));
        }
        else if(difficulty == 2){
            holder.questTV.setTextColor(ContextCompat.getColor(stdAct, R.color.hard));
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
