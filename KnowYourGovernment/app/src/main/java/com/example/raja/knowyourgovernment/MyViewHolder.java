package com.example.raja.knowyourgovernment;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
/**
 * Created by admin on 3/26/2017.
 */

public class MyViewHolder extends RecyclerView.ViewHolder{
    public TextView officeTV;
    public TextView nameTV;


    public MyViewHolder(View itemView) {
        super(itemView);
        officeTV=(TextView) itemView.findViewById(R.id.office);
        nameTV=(TextView) itemView.findViewById(R.id.name);
    }
}
