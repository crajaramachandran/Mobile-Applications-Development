package com.example.raja.questhunt;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.List;

public class CompletedQuests extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {

    List<Quest> com_questList;
    RecyclerView recyclerView;
    CompletedRecyclerAdapter adapter;
    String skill = "";
    DataBaseWrapper db;
    TextView com_questsTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completed_quests);
        skill = getIntent().getStringExtra("SKILL");
        setTitle("Completed Quests");
        com_questsTV =findViewById(R.id.com_quests_skill);
        com_questsTV.setText(skill);
        db = new DataBaseWrapper(this);
        db.open();
        com_questList = db.getAllComQuests(skill);
        db.quest_disp();
        recyclerView = findViewById(R.id.com_recycler);
        adapter = new CompletedRecyclerAdapter(com_questList, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_in_activity,
                R.anim.fade_out_activity);
    }
}
