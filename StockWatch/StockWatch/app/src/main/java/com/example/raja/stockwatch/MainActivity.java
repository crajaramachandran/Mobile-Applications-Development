package com.example.raja.stockwatch;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener{

    private static final String TAG = "MyTag2";
    private ArrayList<String[]> tempList=new ArrayList<>();
    private List<Stock> stockList = new ArrayList<>();
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swiper;
    private StockAdapter stockAdapter;
    private StockDBAdapter dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
        * DB Adapter initialize and test dummy data
        * */
        dbHandler=new StockDBAdapter(this);
        //dummy data
//        stockList.add(new Stock("AAPL","Apple Inc.",135.72,0.38,0.28));
//        stockList.add(new "AMZN","Amazon.com,Inc.",845.07,0.93,0.11));
//        stockList.add(new Stock("GOOG","Alphabet Inc",828.07,3.91,0.47));
//        stockList.add(new Stock("IBM","International Business Machines Corporation",180.67,-0.76,-0.42));
//        stockList.add(new Stock("MSFT","Microsoft Corporation",64.62,0.10,0.16));
//        for(Stock s:stockList){
//            dbHandler.addStock(s);
//        }
        //dbHandler.checkLoadStocks();
        //dbHandler.testSQL();
        dbHandler.dumpLog();

        boolean checkNtwk=checkNetwork();
            if(checkNtwk) {
                tempList = dbHandler.loadStocks();
                for (String[] s : tempList) {
                    doDataAsync(s[0], s[1]);
                }
            }
        else{
                noNetwork();
            }
        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        stockAdapter=new StockAdapter(stockList,this);
        recyclerView.setAdapter(stockAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        swiper = (SwipeRefreshLayout) findViewById(R.id.swiper);
        swiper.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                doRefresh();
            }
        });

        /*
        * add dummy
        * */

    }

    public void viewInit(){
        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        stockAdapter=new StockAdapter(stockList,this);
        recyclerView.setAdapter(stockAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        swiper = (SwipeRefreshLayout) findViewById(R.id.swiper);
        swiper.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                doRefresh();
            }
        });
        stockAdapter.notifyDataSetChanged();
    }

    public void doRefresh(){

        boolean netCheck=checkNetwork();
        if(netCheck) {
            tempList = dbHandler.loadStocks();
            stockList.clear();
            for (String[] s : tempList) {
                doDataAsync(s[0], s[1]);
            }
            swiper.setRefreshing(false);
            Toast.makeText(this, "Updating stocks", Toast.LENGTH_SHORT).show();
        }
        else{
            noNetwork();
            swiper.setRefreshing(false);
        }
    }

    public boolean checkNetwork(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()){
            return true;
        }
        else
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem){

        switch(menuItem.getItemId()) {
            case R.id.add_Stock:
                /**
                 * adding dummy stocks temporarily
                 ***/
                symbolDialog();


                return true;


            default:
            return super.onOptionsItemSelected(menuItem);
        }
    }

    public void symbolDialog() {
        boolean cNtwk = checkNetwork();
        if (!cNtwk) {
            noNetwork();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            final EditText editText = new EditText(this);
            editText.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
            builder.setView(editText);
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    String symbolInput = editText.getText().toString();
                    TestSymbolAsyncTask testSymbolAsyncTask = new TestSymbolAsyncTask(MainActivity.this);
                    testSymbolAsyncTask.execute("Stock Symbol", symbolInput);
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            });
            builder.setMessage("Please enter a Stock Symbol");
            builder.setTitle("Stock Selection");

            AlertDialog dialog = builder.create();
            dialog.show();


        }
    }

    public void doDataAsync(String symbolRet,String nameRet){
        TestSymbolAsyncTask testSymbolAsyncTask=new TestSymbolAsyncTask(this);
        testSymbolAsyncTask.execute(symbolRet,nameRet);
    }

    public void addStockFinal(HashMap<String, String> wData){
        String symbol=wData.get("SYMBOL");
        if(checksymbolExists(symbol)){
            displayDuplicateDialog(symbol);
        } else {
            String name = wData.get("COMPANY NAME");
            String price = wData.get("PRICE");
            String priceChange = wData.get("PRICE CHANGE AMT");
            String priceChangeAmt = wData.get("PRICE CHANGE PCT");

                Stock s = new Stock(symbol, name, price, priceChange, priceChangeAmt);
                stockList.add(s);
                //sort list
                Collections.sort(stockList, new Comparator<Stock>() {
                    @Override
                    public int compare(Stock s1, Stock s2) {
                        return s1.getStockSymbol().compareToIgnoreCase(s2.getStockSymbol());
                    }
                });
                //sort list done
                //add stock to db
                dbHandler.addStock(s);
                //done adding stock to db

            viewInit();
        }

    }

    public boolean checksymbolExists(String symbol){
        for(Stock s:stockList){
            if(s.getStockSymbol().equals(symbol)){
                return true;
            }
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        int pos = recyclerView.getChildLayoutPosition(v);
        Stock s = stockList.get(pos);
        Toast.makeText(v.getContext(), "Short" + s.getStockSymbol(), Toast.LENGTH_SHORT).show();
        try{
        Intent browserIntent = new Intent(Intent.ACTION_VIEW,Uri.parse("http://www.marketwatch.com/investing/stock/"+s.getStockSymbol()));
        startActivity(browserIntent);
        }
        catch(ActivityNotFoundException e){
            Toast.makeText(this,"No browser found.",Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

    }

    @Override
    public boolean onLongClick(View v) {
        final int pos = recyclerView.getChildLayoutPosition(v);
        Stock s = stockList.get(pos);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dbHandler.deleteStock(stockList.get(pos).getStockSymbol());
                stockList.remove(pos);
                stockAdapter.notifyDataSetChanged();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.setMessage("Delete Stock " + stockList.get(pos).getStockSymbol() + "?");
        builder.setTitle("Delete Stock");

        AlertDialog dialog = builder.create();
        dialog.show();


        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbHandler.close();
    }

    public void updateSymbolTemp(ArrayList<String[]> listData) {

        final AlertDialog.Builder choiceDialog = new AlertDialog.Builder(MainActivity.this);
        int size = listData.size();
        if (size == 0){
            Toast.makeText(this,"No stocks found for the provided symbol",Toast.LENGTH_SHORT).show();
        }
        if (size == 1) {
            String[] st = listData.get(0);
            doDataAsync(st[0],st[1]);
        } else if(size>1){
            final ArrayAdapter<String> choiceMenu = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1);
            for (String[] st : listData) {
                choiceMenu.add(st[0] + "-" + st[1]);
            }
            choiceDialog.setAdapter(choiceMenu, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String s = choiceMenu.getItem(which);
                    String parts[] = s.split("-", 2);
                    doDataAsync(parts[0], parts[1]);
                }
            });
            choiceDialog.setTitle("Make a selection");
            choiceDialog.show();

        }
    }

    public void symbolNotFound(String s){
        final AlertDialog.Builder symbolNFDialog=new AlertDialog.Builder(MainActivity.this);
        symbolNFDialog.setTitle("Symbol not found:"+s);
        symbolNFDialog.setMessage("Data for stock symbol");
        symbolNFDialog.show();
    }

    public void displayDuplicateDialog(String symbol){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Stock Symbol "+symbol+" is already displayed");
        builder.setTitle("Stock Selection");
        builder.setIcon(R.drawable.alert);
        builder.show();

    }

    public void noNetwork(){
        final AlertDialog.Builder noNet=new AlertDialog.Builder(MainActivity.this);
        noNet.setTitle("No Network Connection");
        noNet.setMessage("Stocks Cannot Be Added Without A Network Connection");
        noNet.show();
    }
}
