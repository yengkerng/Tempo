package com.tempo.presenter;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tempo.model.Group;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by andrewcofano on 3/11/17.
 */

public class GroupListAdapter extends BaseAdapter {

    private ArrayList<String> mDataSource;

    public GroupListAdapter(ArrayList<String> items) {
        mDataSource = items;

    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get view for row item

        if (convertView == null) {
            //rowView = mInflater.inflate(R.layout.list_item_group, parent, false);
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            convertView = inflater.inflate(R.layout.list_item_group, parent, false);
        }

        TextView nameOfGroup = (TextView) convertView.findViewById(R.id.nameOfGroup);
        String group = (String) getItem(position);
        nameOfGroup.setText(group);
        return convertView;
    }


    @Override
    public int getCount() {
        return mDataSource.size();
    }


    public void setmDataSource(ArrayList<String> mDataSource) {
        this.mDataSource = mDataSource;
    }



    @Override
    public Object getItem(int position) {
        return mDataSource.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

}
