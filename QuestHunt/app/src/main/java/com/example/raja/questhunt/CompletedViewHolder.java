package com.example.raja.questhunt;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by admin on 3/26/2017.
 */

public class CompletedViewHolder extends RecyclerView.ViewHolder{
    public TextView com_questTV;


    public CompletedViewHolder(View itemView) {
        super(itemView);
        com_questTV=itemView.findViewById(R.id.com_questTV);
    }

}
