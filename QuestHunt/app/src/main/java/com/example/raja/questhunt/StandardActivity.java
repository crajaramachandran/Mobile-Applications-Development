package com.example.raja.questhunt;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class StandardActivity extends AppCompatActivity implements
        DatePickerDialog.OnDateSetListener, View.OnClickListener, View.OnLongClickListener{
    List<Quest> questList;
    RecyclerView recyclerView;
    StandardRecyclerAdapter adapter;
    String skill;
    Integer skill_pos;
    Integer skill_level;
    Integer skill_xp;
    Integer skill_uid;
    DataBaseWrapper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_standard);
        skill = getIntent().getStringExtra("SKILL");
        setTitle(skill);
        skill_uid = getIntent().getIntExtra("SKILL_UID",0);
        skill_pos = getIntent().getIntExtra("SKILL_POS",0);
        skill_xp = getIntent().getIntExtra("SKILL_XP",0);
        skill_level = getIntent().getIntExtra("SKILL_LEVEL",0);
        System.out.println("Skill in StandardActivity:"+skill);
        questList = new ArrayList<>();
        db = new DataBaseWrapper(this);
        db.open();
        questList = db.getAllQuests(skill);
        initView();
    }

    public void initView() {

        recyclerView = findViewById(R.id.recycler);
        adapter = new StandardRecyclerAdapter(questList, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ItemTouchHelper.SimpleCallback simpleCallback =
                new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
                    @Override
                    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                                          RecyclerView.ViewHolder target) {
                        return false;
                    }

                    @Override
                    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                        TextView tv = viewHolder.itemView.findViewById(R.id.questTV);
                        tv.setPaintFlags(tv.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                        doneDialog(viewHolder);
                    }

                    @Override
                    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                        //keeping this empty,since the actual onChildDraw would remove the item
                    }

                };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    public void doneDialog(final RecyclerView.ViewHolder viewHolder) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(StandardActivity.this);
        builder.setTitle("Done?");
        builder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        TextView tv = viewHolder.itemView.findViewById(R.id.questTV);
                        tv.setPaintFlags(tv.getPaintFlags() & ~(Paint.STRIKE_THRU_TEXT_FLAG));
                    }
                });
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final Quest quest = questList.get(viewHolder.getAdapterPosition());
                int quest_xp = quest.getQuest_xp();
                skill_stats_update(quest_xp);
                int ret = db.quest_completed_update(quest.getId(),skill);
                questList.remove(quest);
                TextView tv = viewHolder.itemView.findViewById(R.id.questTV);
                tv.setPaintFlags(tv.getPaintFlags() & ~(Paint.STRIKE_THRU_TEXT_FLAG));
                adapter.notifyItemRemoved(viewHolder.getAdapterPosition());
            }
        });
        builder.show();
    }

    public void skill_stats_update(int quest_xp){
        if(skill_xp + quest_xp>=100){
            skill_xp = skill_xp+quest_xp - 100;
            skill_level++;
        }
        else {
            skill_xp = skill_xp+quest_xp;
        }
        db.updateProgressSkill(skill,quest_xp);
    }

    public void testQuests() {
//        db.quest_insert(1,"Activities",1,10,"MAD");
//        db.quest_insert(2,"Layouts",1,10,"MAD");
//        db.quest_insert(3,"Intents",1,10,"MAD");
//        db.quest_insert(4,"Pushups",1,10,"WORKOUT");
//        db.quest_insert(5,"Pullups",1,10,"WORKOUT");
//        db.quest_insert(6,"Squats",1,10,"WORKOUT");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.standard_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){

        switch(item.getItemId()){
            case R.id.add_quest:
                createQuest();
                break;
            case R.id.completed_quests:
                Intent intent = new Intent(StandardActivity.this, CompletedQuests.class);
                intent.putExtra("SKILL", skill);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;

            case R.id.default_order:
                Collections.sort(questList,new rowIDSort());
                adapter.notifyDataSetChanged();
                break;
            case R.id.sort_by_deadline_ascending:
                Collections.sort(questList,new dateAscSort());
                adapter.notifyDataSetChanged();
                break;
            case R.id.sort_by_deadline_descending:
                Collections.sort(questList,new dateDescSort());
                adapter.notifyDataSetChanged();
                break;
            case R.id.sort_by_difficulty_ascending:
                Collections.sort(questList,new difficultyAscSort());
                adapter.notifyDataSetChanged();
                break;
            case R.id.sort_by_difficulty_descending:
                Collections.sort(questList,new difficultyDescSort());
                adapter.notifyDataSetChanged();
                break;
        }
        return false;
    }

    public void createQuest() {
        displayQuestDialog();
    }

    public void displayQuestDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(StandardActivity.this);
        builder.setTitle("Enter quest:");
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setGravity(Gravity.CENTER_HORIZONTAL);
        final EditText editText = new EditText(this);
        editText.setSingleLine(true);
        editText.setGravity(Gravity.CENTER);
        editText.setBackgroundResource(R.drawable.button_bg);
        layout.setPadding(10, 0, 10, 0);
        layout.addView(editText);
        builder.setView(layout);
        // Setting Negative "NO" Button
        builder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog
                        dialog.cancel();
                    }
                });
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                //PASS QUEST NAME
                String quest_name = editText.getText().toString();
                if (quest_name.matches("")) {
                    ////System.out.println("null");
                    Toast.makeText(StandardActivity.this, "Quest name cannot be null.Enter something you doofus!", Toast.LENGTH_LONG).show();
                    displayQuestDialog();
                } else {
                    ////System.out.println("not null");
                    displayDifficultyDialog(quest_name);
                }

            }
        });
        AlertDialog dialog = builder.create();
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
        dialog.show();
    }

    public void displayDifficultyDialog(String qname) {
        final String q = qname;
        final AlertDialog.Builder diffDialog = new AlertDialog.Builder(StandardActivity.this);
        final ArrayAdapter<String> diffMenu = new ArrayAdapter<String>(StandardActivity.this, android.R.layout.select_dialog_singlechoice);
        diffMenu.add("Easy");
        diffMenu.add("Medium");
        diffMenu.add("Hard");
        diffDialog.setAdapter(diffMenu, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                //PASS THE SELECTED DIFFICULTY(XP)
                int xp = 0;
                int diff = 0;
                if (which == 0) {
                    xp = 5;
                    diff = 0;
                } else if (which == 1) {
                    xp = 10;
                    diff = 1;
                } else if (which == 2) {
                    xp = 20;
                    diff = 2;
                }
                pickDate(q, xp, diff, skill);

            }
        });
        diffDialog.show();
    }

    public void pickDate(final String q, final int xp, final int diff, final String skill){
        Calendar calendar= Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String deadline;
                Long daysBetween = 0L;
                Calendar eDate = Calendar.getInstance();
                eDate.set(year,month,dayOfMonth);
                eDate = getDatePart(eDate);
                Calendar sDate = getDatePart(Calendar.getInstance()); //today

                while (sDate.before(eDate)) {
                    sDate.add(Calendar.DAY_OF_MONTH, 1);
                    daysBetween++;
                }
                if(daysBetween==0){
                    deadline = "Today";
                }
                else if(daysBetween==1){
                    deadline = "1 day";
                }
                else{
                    deadline = daysBetween.toString()+" days";
                }
                addQuest(q, xp, diff, skill,deadline);
            }
        },calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();

    }

    public static Calendar getDatePart(Calendar cal){
        cal.set(Calendar.HOUR_OF_DAY, 0);            // set hour to midnight
        cal.set(Calendar.MINUTE, 0);                 // set minute in hour
        cal.set(Calendar.SECOND, 0);                 // set second in minute
        cal.set(Calendar.MILLISECOND, 0);            // set millisecond in second

        return cal;                                  // return the date part
    }

    public void addQuest(String quest_title, int xp, int diff, String skill, String deadline) {
        db.quest_insert(skill_uid,quest_title, xp, diff, skill,deadline);
        Quest quest = new Quest(skill_uid, quest_title, xp, diff, skill,deadline);
        skill_uid++;
        db.set_skill_uid(skill,skill_uid);
        questList.add(quest);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        returnData();
        overridePendingTransition(R.anim.fade_in_activity,
                R.anim.fade_out_activity);
    }

    public void returnData(){
        Intent retdata=new Intent();
        retdata.putExtra("SKILL",skill);
        retdata.putExtra("SKILL_UID",skill_uid);
        retdata.putExtra("SKILL_POS",skill_pos);
        retdata.putExtra("SKILL_XP",skill_xp);
        retdata.putExtra("SKILL_LEVEL",skill_level);
        setResult(RESULT_OK,retdata);
        finish();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public boolean onLongClick(final View v) {
        final int pos = recyclerView.getChildLayoutPosition(v);
        PopupMenu popup = new PopupMenu(this,v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.quest_context_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.rename:
                        renameDialog(v,pos);
                        break;
                    case R.id.change_difficulty:
                        //handle menu2 click
                        break;
                    case R.id.change_deadline:
                        //handle menu3 click
                        break;
                }
                return false;
            }
        });
        popup.show();
        return false;
    }

    public void renameDialog(View v, final int pos){
        final TextView tv = v.findViewById(R.id.questTV);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Rename quest:");
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setGravity(Gravity.CENTER_HORIZONTAL);
        final EditText editText = new EditText(this);
        editText.setSingleLine(true);
        editText.setGravity(Gravity.CENTER);
        editText.setBackgroundResource(R.drawable.button_bg);
        layout.setPadding(10, 0, 10, 0);
        layout.addView(editText);
        builder.setView(layout);
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.setPositiveButton("Rename", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Quest quest = questList.get(pos);
                String newQuestName =editText.getText().toString();
                tv.setText(newQuestName);
                db.quest_name_update(newQuestName,quest.getId(),skill);
                quest.setQuest_name(newQuestName);
                adapter.notifyItemChanged(pos);
            }
        });
        AlertDialog dialog = builder.create();
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
        dialog.show();
    }

}
