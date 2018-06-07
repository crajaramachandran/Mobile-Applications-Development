package com.example.admin.broadcastwithservice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    static final String ACTION_MSG_TO_SERVICE = "ACTION_MSG_TO_SERVICE";
    static final String ACTION_NEWS_STORY = "ACTION_NEWS_STORY";


    private List<Fragment> fragments;
    private MyPageAdapter pageAdapter;
    private ViewPager pager;

    private NewsReceiver newsReceiver;

    private SourceDownloader sourceDownloader;
    private ArrayList<String> categoryList = new ArrayList<>();

    private Set<String> categorySetList = new TreeSet<>();
    private ArrayList<String> sourceList = new ArrayList<>();
    private List<Source> sourceObjectsList = new ArrayList<>();
    private HashMap<String, List<Source>> mapSourceCategory = new HashMap<>();
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerAdapter drawerAdapter;

    //ArrayList<Story> storyList = new ArrayList<>();

    private String currentSourceName="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //START SERVICE
        Intent intent = new Intent(MainActivity.this, NewsService.class);
        startService(intent);

        //Create Intent Filter for Action News Story and register newsReceiver(Broadcast receiver)
        newsReceiver = new NewsReceiver();
        IntentFilter filter2 = new IntentFilter(ACTION_NEWS_STORY);
        registerReceiver(newsReceiver, filter2);

        //set drawer,toggle,adapter and actionbar
        //DRAWER INIT
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        drawerAdapter = new DrawerAdapter(this, R.layout.drawer_list_item, sourceObjectsList);

        mDrawerList.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectItem(position);
            }
        });

        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
        );

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


        //pageviewer and adapter

        fragments = getFragments();
        pageAdapter = new MyPageAdapter(getSupportFragmentManager());
        pager = (ViewPager) findViewById(R.id.viewpager);
        pager.setAdapter(pageAdapter);


        //START SOURCE DOWNLOADER
        sourceDownloader=new SourceDownloader(this,"");
        sourceDownloader.execute();

    }

    public void setSources(HashMap<String, List<Source>> catSourceList) {
        mapSourceCategory.putAll(catSourceList);
        categorySetList.addAll(catSourceList.keySet());
        categoryList.addAll(categorySetList);

        //get all the sources
        List<Source> temp=new ArrayList<>();
        temp=catSourceList.get("all");
        for(Source s:temp){
            Source source=new Source(s.getId(),s.getName(),s.getUrl(),s.getCategory());
            sourceObjectsList.add(source);
        }
//            for(Source s:sList){
//                sourceList.add(s.getName());
//            }


        //add sources to the adapter
        drawerAdapter = new DrawerAdapter(this, R.layout.drawer_list_item, sourceObjectsList);
        mDrawerList.setAdapter(drawerAdapter);
        invalidateOptionsMenu();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        int catSize = categoryList.size();
        if (catSize > 0) {
            for (int i = 0; i < catSize; i++) {
                menu.add(menu.NONE, i, menu.NONE, categoryList.get(i));
            }
        }
        return true;
    }


    //DRAWER FUNCTIONS

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        String str = categoryList.get(item.getItemId());
        sourceObjectsList.removeAll(sourceObjectsList);
        List<Source> tempList = mapSourceCategory.get(str);
        sourceObjectsList.addAll(tempList);
        drawerAdapter.notifyDataSetChanged();
//            mDrawerList.setAdapter(new ArrayAdapter<>(this,
//                    R.layout.drawer_list_item, sourceList));

        return super.onOptionsItemSelected(item);
    }

    private void selectItem(int position) {
        Toast.makeText(this, "You picked " + sourceObjectsList.get(position).getName(), Toast.LENGTH_SHORT).show();
        //1.set bavckground null
        pager.setBackgroundDrawable(null);
        //2.set currentSourceId to the selected source
        String currentSourceId = sourceObjectsList.get(position).getId();
        currentSourceName=sourceObjectsList.get(position).getName();
        //3.create an intent with action ACTION_MSG_TO_SVC
        Intent intent = new Intent();
        intent.setAction(ACTION_MSG_TO_SERVICE);
        //4.add source name as an extra
        intent.putExtra("Source", currentSourceId);
        //5.broadcast the intent
        sendBroadcast(intent);
        //6.close the drawer
        mDrawerLayout.closeDrawer(mDrawerList);
        //reDoFragments(position);
    }


    //Page Adapter and Fragment functions

    private List<Fragment> getFragments() {
        List<Fragment> fList = new ArrayList<Fragment>();
        return fList;
    }

    private void reDoFragments(ArrayList<Story> passedList) {

        //1.set title to the current Source Name
        setTitle(currentSourceName);
        //2.For each fragment in the PageAdapter,notify change in position
        int pageCount=pageAdapter.getCount();
        for (int i = 0; i < pageCount; i++)
            pageAdapter.notifyChangeInPosition(i);
        //3.clear the fragments list
        fragments.clear();
        //4.For each article in the list,make a new fragment
        int sCount=0;
        int sSize=passedList.size();
        for(Story s:passedList){
            fragments.add(MyFragment.newInstance(s,sCount+1,sSize));
            sCount++;
        }
        //5.notify adapter is changed
        pageAdapter.notifyDataSetChanged();
        pager.setCurrentItem(0);



    }

    //PageAdapter class
    private class MyPageAdapter extends FragmentPagerAdapter {
        private long baseId = 0;

        public MyPageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public long getItemId(int position) {
            // give an ID different from position when position has been changed
            return baseId + position;
        }

        public void notifyChangeInPosition(int n) {
            // shift the ID returned by getItemId outside the range of all previous fragments
            baseId += getCount() + n;
        }


    }

    //BroadCast Receiver  NewsReceiver class
    class NewsReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            switch (intent.getAction()) {
                case ACTION_NEWS_STORY:
                    //1.get the article list from the intent
                    ArrayList<Story> tempList=intent.getParcelableArrayListExtra("storylist");
                    //2.call reDoFragments passing the list of articles
                    reDoFragments(tempList);
                    break;
            }
        }
    }




}


