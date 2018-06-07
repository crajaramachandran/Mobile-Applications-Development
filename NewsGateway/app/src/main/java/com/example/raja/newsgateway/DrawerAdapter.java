package com.example.raja.newsgateway;

import android.content.Context;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by admin on 5/4/2017.
 */

public class DrawerAdapter extends ArrayAdapter<Source> {

    public DrawerAdapter(Context context, int resource, List<Source> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v=convertView;
        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.drawer_list_item, null);
        }
        Source s = getItem(position);
        TextView tv=(TextView) v.findViewById(R.id.textViewDraw);
        tv.setText(s.getName());
        return v;
    }
}
