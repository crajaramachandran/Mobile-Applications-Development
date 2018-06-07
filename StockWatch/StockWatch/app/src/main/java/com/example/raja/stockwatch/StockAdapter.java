package com.example.raja.stockwatch;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class StockAdapter extends RecyclerView.Adapter<MyViewHolder>{
    private List<Stock> stockList;
    private MainActivity mainAct;

    public StockAdapter(List<Stock> stockList, MainActivity ma) {
        this.stockList = stockList;
        mainAct = ma;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.stock_layout, parent, false);
        itemView.setOnClickListener(mainAct);
        itemView.setOnLongClickListener(mainAct);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Stock stock=stockList.get(position);
        String price_change=stock.getStockPriceChange();
        if(price_change.charAt(0)=='-')
        {
            int color=R.color.StockDown;
            holder.stockSymbolTV.setText(stock.getStockSymbol());
            holder.stockNameTV.setText(stock.getStockName());
            holder.stockPriceTV.setText(stock.getStockPrice());
            holder.stockSymbolTV.setTextColor(mainAct.getResources().getColor(R.color.StockDown));
            holder.stockNameTV.setTextColor(mainAct.getResources().getColor(R.color.StockDown));
            holder.stockPriceTV.setTextColor(mainAct.getResources().getColor(R.color.StockDown));
            holder.stockPriceChangeTV.setTextColor(mainAct.getResources().getColor(R.color.StockDown));
            Drawable draw=mainAct.getResources().getDrawable(R.drawable.down);
            draw.setBounds(0,0,40,30);
            holder.stockPriceChangeTV.setCompoundDrawables(draw,null,null,null);
            holder.stockPriceChangeTV.setText(price_change);
        }
        else {
            if(price_change.charAt(0)=='+') {
                price_change = price_change.substring(1);
            }
            holder.stockSymbolTV.setTextColor(mainAct.getResources().getColor(R.color.StockTitle));
            holder.stockNameTV.setTextColor(mainAct.getResources().getColor(R.color.StockUp));
            holder.stockPriceTV.setTextColor(mainAct.getResources().getColor(R.color.StockUp));
            holder.stockPriceChangeTV.setTextColor(mainAct.getResources().getColor(R.color.StockTitle));
            Drawable draw=mainAct.getResources().getDrawable(R.drawable.up);
            draw.setBounds(0,0,40,30);
            holder.stockPriceChangeTV.setCompoundDrawables(draw,null,null,null);
            holder.stockSymbolTV.setText(stock.getStockSymbol());
            holder.stockNameTV.setText(stock.getStockName());
            holder.stockPriceTV.setText(stock.getStockPrice());
            holder.stockPriceChangeTV.setText(price_change);
        }
    }

    @Override
    public int getItemCount() {
        return stockList.size();
    }
}
