package com.example.admin.broadcastwithservice;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

//Service Class NewsService
public class NewsService extends Service {
    public class ServiceReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            switch (intent.getAction()) {
                case ACTION_MSG_TO_SERVICE:
                    //1.if intent's action type is ACTION_MSG_TO_SERVICE,get source name from intent
                    String sourceId = intent.getStringExtra("Source");
                    //2.create news article downloader asynctask
                    NewsArticleDownloader newsArticleDownloader = new NewsArticleDownloader(NewsService.this,sourceId);
                    newsArticleDownloader.execute();

                    break;
            }
        }


    }


    static final String ACTION_MSG_TO_SERVICE = "ACTION_MSG_TO_SERVICE";
    static final String ACTION_NEWS_STORY = "ACTION_NEWS_STORY";
    private static final String TAG = "NewsService";
    private boolean running = true;
    private ServiceReceiver serviceReceiver;
    ArrayList<Story> newsStoryList=new ArrayList<>();

    public NewsService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //Creating new thread for my service
        //Always write your long running tasks in a separate thread, to avoid ANR

        //1.create intentfilter and register
        serviceReceiver=new ServiceReceiver();
        IntentFilter filter1 = new IntentFilter(ACTION_MSG_TO_SERVICE);
        registerReceiver(serviceReceiver, filter1);

        //2.start thread
        new Thread(new Runnable() {
            @Override
            public void run() {

                while (running) {

                    if (!newsStoryList.isEmpty()) {
                        //1.create intent with action ACTION_NEWS_STORY
                        Intent intent=new Intent();
                        intent.setAction(ACTION_NEWS_STORY);
                        //2.add storylist extra to intent
                        intent.putParcelableArrayListExtra("storylist",newsStoryList);
                        //3.broadcast intent
                        sendBroadcast(intent);
                        //4.clear storylist
                        newsStoryList.clear();
                    } else {
                        try {
                            Thread.sleep(250);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                Log.d(TAG, "Loop ends");
            }
        }).start();

        //return start_sticky
        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_SHORT).show();
        running = false;
        super.onDestroy();
    }

    public void setArticles(List<Story> retStoryList){
        //1.clear storyList
        newsStoryList.clear();
        //2.fill storyList with passed list
        newsStoryList.addAll(retStoryList);
    }




}
