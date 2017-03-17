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

    private List<String> mDataSource;

    public GroupListAdapter(List<String> items) {
        mDataSource = items;

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
        return mDataSource.size();
    }

    public void setmDataSource(List<String> mDataSource) {
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
