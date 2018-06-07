package com.example.admin.broadcastwithservice;

import android.net.Uri;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 5/5/2017.
 */

public class NewsArticleDownloader extends AsyncTask<String,Void,String> {

    private String API_KEY="54395bcdf1de45cf98d6c90b7d415c6d";
    private String apiURL="https://newsapi.org/v1/articles";
    private String str="";
    private NewsService newsService;
    private String sourceId;
    private List<Story> storyList=new ArrayList<>();

    public NewsArticleDownloader(NewsService newsService,String sourceId){
        this.newsService=newsService;
        this.sourceId=sourceId;
    }

    @Override
    protected void onPostExecute(String retString){
        try{
            JSONObject obj=new JSONObject(retString);
            String articlesList=obj.optString("articles");
            if(!articlesList.equals("")){
                JSONArray storyArray=new JSONArray(articlesList);
                int storyArraySize=storyArray.length();
                for(int i=0;i<storyArraySize;i++){
                    JSONObject storyObj=(JSONObject) storyArray.get(i);
                    String author=storyObj.optString("author");
                    String title=storyObj.optString("title");
                    String description=storyObj.optString("description");
                    String urlToImage=storyObj.optString("urlToImage");
                    String publishedAt=storyObj.optString("publishedAt");
                    String url=storyObj.optString("url");
                    storyList.add(new Story(author,title,description,urlToImage,publishedAt,url));
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        newsService.setArticles(storyList);
    }

    @Override
    protected String doInBackground(String... params) {

        String urlToUse="";
        Uri.Builder buildURL=Uri.parse(apiURL).buildUpon();
        buildURL.appendQueryParameter("source",sourceId);
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
