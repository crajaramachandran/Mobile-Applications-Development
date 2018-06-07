package com.example.raja.stockwatch;

/**
 * Created by admin on 3/12/2017.
 */

public class Stock {

    private String stockSymbol;
    private String stockName;
    private String stockPrice;
    private String stockPriceChange;
    private String stockPricePercentage;

    public Stock(String stockSymbol,String stockName,String stockPrice,String stockPriceChange,String stockPricePercentage) {
        this.stockSymbol=stockSymbol;
        this.stockName=stockName;
        this.stockPrice=stockPrice;
        this.stockPriceChange=stockPriceChange;
        this.stockPricePercentage=stockPricePercentage;
    }

    public String getStockSymbol() {
        return stockSymbol;
    }

    public String getStockName() {
        return stockName;
    }

    public String getStockPrice() { return stockPrice+"";}

    public String getStockPriceChange(){ return stockPriceChange+"("+stockPricePercentage+"%)";}


    @Override
    public String toString() {
        return stockSymbol + stockName+ stockPrice + stockPriceChange + stockPricePercentage ;
    }
}
