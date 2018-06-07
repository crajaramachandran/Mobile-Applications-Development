package com.example.raja.knowyourgovernment;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by admin on 3/26/2017.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<MyViewHolder>{
    MainActivity mainAct;
    private ArrayList<Official> list;

    public RecyclerAdapter(ArrayList<Official> list, MainActivity ma){
        this.list=list;
        this.mainAct=ma;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card, parent, false);
        itemView.setOnClickListener(mainAct);
        itemView.setOnLongClickListener(mainAct);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Official official=list.get(position);
        //String officeTemp=holder.officeTV.getText()+" "+dummy_official.getOffice();
        //String nameTemp=holder.nameTV.getText()+" "+dummy_official.getName()+" ("+dummy_official.getParty()+")";
        holder.officeTV.setText(official.getOffice());
        holder.nameTV.setText(official.getName()+" ("+official.getParty()+")");
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
