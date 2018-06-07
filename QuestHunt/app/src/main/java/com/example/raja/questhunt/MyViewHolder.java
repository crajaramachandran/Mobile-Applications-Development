package com.example.raja.questhunt;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by admin on 3/26/2017.
 */

public class MyViewHolder extends RecyclerView.ViewHolder{
    public TextView questTV;
    public TextView deadlineTV;


    public MyViewHolder(View itemView) {
        super(itemView);
        questTV=itemView.findViewById(R.id.questTV);
        deadlineTV = itemView.findViewById(R.id.deadlineTV);
    }

}
