package com.tempo.presenter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by andrewcofano on 3/11/17.
 */

public class GroupListAdapter extends BaseAdapter {

    private List<String> mDataSource1;

    public GroupListAdapter(List<String> items) {
        mDataSource1 = items;

    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get view for row item

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View newView = inflater.inflate(R.layout.list_item_group, parent, false);

        TextView nameOfGroup = (TextView) newView.findViewById(R.id.nameOfGroup);
        String group = (String) getItem(position);
        nameOfGroup.setText(group);
        return newView;
    }

    @Override
    public int getCount() {
        return mDataSource1.size();
    }

    public void setmDataSource(List<String> mDataSource1) {
        this.mDataSource1 = mDataSource1;
    }

    @Override
    public Object getItem(int position) {
        return mDataSource1.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

}
