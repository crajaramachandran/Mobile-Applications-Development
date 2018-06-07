package com.example.raja.stockwatch;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;


public class TestSymbolAsyncTask extends AsyncTask<String, Void, ArrayList<String>> {

    private static final String TAG = "AsyncTAG";
    private String str="";
    private MainActivity mainActivity;
    private ArrayList<String[]> symbolList=new ArrayList<>();
    private HashMap<String, String> wData = new HashMap<>();

    private final String symbolURL = "http://d.yimg.com/aq/autoc";
    private final String dataURL = "https://api.iextrading.com/1.0/stock/";

    public TestSymbolAsyncTask(MainActivity ma){
        mainActivity=ma;
    }

    @Override
    protected void onPostExecute(ArrayList<String> s){

        if(s.get(0).equals("Stock Symbol")) {
            parseJSONSymbol(s);
            mainActivity.updateSymbolTemp(symbolList);
        }
        else if(s.get(0).equals("Data")){
            parseJSONData(s);
            mainActivity.addStockFinal(wData);
        }
        else if(s.equals("noInternet")){
            mainActivity.noNetwork();
        }
        else {
            mainActivity.symbolNotFound(s.get(0));
        }



    }

    @Override
    protected ArrayList<String> doInBackground(String... params) {
        ArrayList<String> retString=new ArrayList<>();
        String urlToUse="";
        if(params[0].equals("Stock Symbol")){
            Uri.Builder buildURL = Uri.parse(symbolURL).buildUpon();
            buildURL.appendQueryParameter("region", "US");
            buildURL.appendQueryParameter("lang", "en-US");
            buildURL.appendQueryParameter("query", params[1]);
            urlToUse = buildURL.build().toString();
            Log.d("Debug",urlToUse);
        }
        else if(!params[0].equals("Stock Symbol")){
            String fullDataURL = dataURL+params[0]+"/"+"quote";
            Uri.Builder buildURL = Uri.parse(fullDataURL).buildUpon();
            urlToUse = buildURL.build().toString();
            Log.d("Debug",urlToUse);

        }
        StringBuilder sb = new StringBuilder();
        try {
            URL url = new URL(urlToUse);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                InputStream is = conn.getInputStream();
                BufferedReader reader = new BufferedReader((new InputStreamReader(is)));

                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append('\n');
                }

        } catch (Exception e) {
            retString.add(params[1]);
            return retString;
        }
        str=sb.toString();

        if(!params[0].equals("Stock Symbol")){
            retString.add("Data");
        }
            retString.add(params[0]);
            retString.add(params[1]);
            retString.add(str);
            return retString;

    }


    public void parseJSONSymbol(ArrayList<String> list){

        String symbol=list.get(1);
        String JString=list.get(2);

        try {
            JSONObject result = new JSONObject(JString);
            String resultSetStr = result.getString("ResultSet");
            JSONObject resultSet = new JSONObject(resultSetStr);
            String resultSetAllStr = resultSet.getString("Result");
            Log.d("Debug",resultSetAllStr);
            JSONArray stockJSON=new JSONArray(resultSetAllStr);
            int count=stockJSON.length();
            for(int i=0;i<count;i++){
                JSONObject jStock=(JSONObject) stockJSON.get(i);
                String st[]=new String[2];
                st[0]=jStock.getString("symbol");
                st[1]=jStock.getString("name");
                String type = jStock.getString("type");
                String period[] = st[0].split(Pattern.quote("."));
                if(type.equals("S") && period.length<2) {
                    symbolList.add(st);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void parseJSONData(ArrayList<String> list){
        String symbol=list.get(1);
        String name=list.get(2);
        String JString=list.get(3);

        try {
            JSONObject jStock=new JSONObject(JString);
            String symbolStr=jStock.getString("symbol");
            wData.put("SYMBOL",symbol);
            wData.put("COMPANY NAME",name);
            wData.put("PRICE", jStock.getString("latestPrice"));
            wData.put("PRICE CHANGE AMT", jStock.getString("change"));
            wData.put("PRICE CHANGE PCT", jStock.getString("changePercent"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
