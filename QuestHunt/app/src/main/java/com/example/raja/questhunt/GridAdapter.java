package com.example.raja.questhunt;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Raja on 5/8/2018.
 */

public class GridAdapter extends BaseAdapter {

    private Context context;
    List<Skill> skillsList;

    public GridAdapter(Context c, List<Skill> skills){
        context = c;
        skillsList = skills;
    }

    @Override
    public int getCount() {
        return skillsList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(convertView == null) {
            convertView = inflater.inflate(R.layout.grid_item_layout, null);
        }
            TextView skill_name = (TextView) convertView.findViewById(R.id.skill_name);
            TextView skill_level = (TextView) convertView.findViewById(R.id.skill_level);
            TextView skill_xp = (TextView) convertView.findViewById(R.id.skill_xp);
            String skillnameStr =skillsList.get(i).getSkill_name();
            skill_name.setText(skillnameStr);
            String levelText = "Level:";
            String XPText = "XP:";
            skill_level.setText(levelText+skillsList.get(i).getSkill_level());
            skill_xp.setText(XPText+skillsList.get(i).getSkill_xp());
            return convertView;
    }
}
