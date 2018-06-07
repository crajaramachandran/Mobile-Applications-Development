package com.example.raja.stockwatch;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;


public class MyViewHolder extends RecyclerView.ViewHolder {

    public TextView stockSymbolTV;
    public TextView stockNameTV;
    public TextView stockPriceTV;
    public TextView stockPriceChangeTV;

    public MyViewHolder(View view) {
        super(view);
        stockSymbolTV = (TextView) view.findViewById(R.id.stock_Symbol);
        stockNameTV = (TextView) view.findViewById(R.id.stock_Name);
        stockPriceTV = (TextView) view.findViewById(R.id.stock_Price);
        stockPriceChangeTV = (TextView) view.findViewById(R.id.stock_PriceChange);
    }

}
