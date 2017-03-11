package com.tempo.Presenter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tempo.Model.CalendarEvent;
import com.tempo.Model.Group;

import java.util.ArrayList;

/**
 * Created by andrewcofano on 3/11/17.
 */

public class GroupListAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<Group> mDataSource;

    public GroupListAdapter(Context context, ArrayList<Group> items) {
        mContext = context;
        mDataSource = items;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get view for row item
        View rowView = mInflater.inflate(R.layout.list_item_group, parent, false);

        TextView nameOfGroup =
                (TextView) rowView.findViewById(R.id.nameOfGroup);

        TextView nameOfAdmin =
                (TextView) rowView.findViewById(R.id.nameOfAdmin);

        TextView groupMemberCount =
                (TextView) rowView.findViewById(R.id.groupMemberCount);


        Group group = (Group) getItem(position);

        nameOfGroup.setText(group.getName());
        nameOfAdmin.setText(group.getAdmin().getUserName());
        //groupMemberCount.setText(group.getMemberCount());

        return rowView;
    }


    @Override
    public int getCount() {
        return mDataSource.size();
    }


    public void setmDataSource(ArrayList<Group> mDataSource) {
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
