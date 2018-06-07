package com.example.raja.questhunt;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class GroupActivity extends AppCompatActivity {

    List<Skill> skillsList;
    DataBaseWrapper db;
    GridAdapter gridAdapter;
    GridView gridview;
    static final int REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        setTitle("Skills");
        skillsList = new ArrayList<>();
        db = new DataBaseWrapper(this);
        db.open();
        db.testSQL();
        skillsList = db.getAllSkills();
//        skillsList.add(new Skill("MAD",50,1));
//        skillsList.add(new Skill("WORKOUT",75,1));
//        skillsList.add(new Skill("GUITAR",40,1));
//        skillsList.add(new Skill("MISC",30,1));

        gridview = (GridView) findViewById(R.id.gridView);
        gridAdapter = new GridAdapter(this, skillsList);
        gridview.setAdapter(gridAdapter);
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Intent intent = new Intent(GroupActivity.this, StandardActivity.class);
                Skill skill = skillsList.get(position);
                intent.putExtra("SKILL", skill.getSkill_name());
                System.out.println("Skill in GroupActivity:"+skill.getSkill_name());
                intent.putExtra("SKILL_UID", skill.getSkill_uid());
                intent.putExtra("SKILL_POS",position);
                intent.putExtra("SKILL_XP",skill.getSkill_xp());
                intent.putExtra("SKILL_LEVEL",skill.getSkill_level());
                startActivityForResult(intent,REQUEST_CODE);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });

        gridview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                deleteDialog(position);
                return true;
            }
        });
    }

    public void deleteDialog(final int i){
        final AlertDialog.Builder builder=new AlertDialog.Builder(GroupActivity.this);
        builder.setTitle("Delete skill and quests?");
        builder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog
                        dialog.cancel();
                    }
                });
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Skill skill = skillsList.get(i);
                db.skill_delete(skill.getSkill_name());
                skillsList.remove(skill);
                gridAdapter.notifyDataSetChanged();
            }
        });
        builder.show();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        db.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.group_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){

    switch(item.getItemId()){
        case R.id.add_skill:
            skillDialog();
            break;
    }
       return false;
    }

    public void skillDialog(){
        final AlertDialog.Builder builder=new AlertDialog.Builder(GroupActivity.this);
        builder.setTitle("Enter skill:");
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setGravity(Gravity.CENTER_HORIZONTAL);
        final EditText editText = new EditText(this);
        editText.setSingleLine(true);
        editText.setGravity(Gravity.CENTER);
        editText.setBackgroundResource(R.drawable.button_bg);
        editText.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
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
        builder.setPositiveButton("Ok",new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                //PASS SKILL NAME
                String skill_name=editText.getText().toString();
                if(skill_name.matches(""))
                {
                    ////System.out.println("null");
                    Toast.makeText(GroupActivity.this,"Skill name cannot be empty!", Toast.LENGTH_LONG).show();
                    skillDialog();
                }
                else
                {
                    db.skill_insert(skill_name);
                    skillsList.add(new Skill(skill_name,0,0,0));
                    gridAdapter.notifyDataSetChanged();

                }

            }
        });
        AlertDialog dialog = builder.create();
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
        dialog.show();

    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        overridePendingTransition(android.R.anim.fade_in,
                android.R.anim.fade_out);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == REQUEST_CODE) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                int skill_xp = data.getIntExtra("SKILL_XP",0);
                int skill_level = data.getIntExtra("SKILL_LEVEL",0);
                int skill_uid = data.getIntExtra("SKILL_UID",0);
                int position = data.getIntExtra("SKILL_POS",0);
                Skill skill = skillsList.get(position);
                skill.setSkill_xp(skill_xp);
                skill.setSkill_level(skill_level);
                skill.setSkill_uid(skill_uid);
                gridAdapter.notifyDataSetChanged();

            }
        }
    }


}
