package com.example.raja.newsgateway;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by admin on 5/3/2017.
 */

public class SourceDownloader extends AsyncTask<String,Void,String> {

    private String API_KEY="54395bcdf1de45cf98d6c90b7d415c6d";
    private String apiURL="https://newsapi.org/v1/sources";
    private String str="";
    private MainActivity mainActivity;
    private String TAG="MyTag";
    private HashMap<String,List<Source>> sourcesHashMap=new HashMap<>();
    private String categoryStr;
    public SourceDownloader(MainActivity ma,String category){
        mainActivity=ma;
        categoryStr=category;
    }

    @Override
    protected void onPostExecute(String retString){

        Log.d(TAG,retString);
        try{
            JSONObject obj=new JSONObject(retString);
            String sourceStr=obj.optString("sources");
            List<Source> allList=new ArrayList<>();
            if(!sourceStr.equals("")){
                JSONArray sourceArr=new JSONArray(sourceStr);
                int sourceArrSize=sourceArr.length();
                for(int i=0;i<sourceArrSize;i++){
                    JSONObject sourceObj=(JSONObject) sourceArr.get(i);
                    String idStr=sourceObj.optString("id");
                    String nameStr=sourceObj.optString("name");
                    String urlStr=sourceObj.optString("url");
                    String catStr=sourceObj.optString("category");
                    Source s=new Source(idStr,nameStr,urlStr,catStr);
                    List<Source> sList=sourcesHashMap.get(catStr);
                    if(sList==null){
                        sList=new ArrayList<Source>();
                    }
                    sList.add(s);
                    allList.add(s);
                    sourcesHashMap.put(catStr,sList);
                }
                sourcesHashMap.put("all",allList);
            }

        }catch(Exception e){
            e.printStackTrace();
        }
        mainActivity.setSources(sourcesHashMap);

    }
    @Override
    protected String doInBackground(String... params) {

        String urlToUse="";
        String catQuery=categoryStr;
        if(catQuery.equals("all")){
            catQuery="";
        }
        Uri.Builder buildURL=Uri.parse(apiURL).buildUpon();
        buildURL.appendQueryParameter("language", "en");
        buildURL.appendQueryParameter("country", "us");
        buildURL.appendQueryParameter("category",catQuery);
        buildURL.appendQueryParameter("apiKey",API_KEY);
        urlToUse = buildURL.build().toString();

        StringBuilder sb = new StringBuilder();
        try{
            URL url = new URL(urlToUse);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            InputStream is = conn.getInputStream();
            BufferedReader reader = new BufferedReader((new InputStreamReader(is)));
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }

        }catch(Exception e){
            e.printStackTrace();
        }
        str=sb.toString();
        return str;
    }
}
