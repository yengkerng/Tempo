package com.tempo.Presenter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tempo.Model.Group;
import com.tempo.Model.User;

import java.util.ArrayList;

/**
 * Created by andrewcofano on 3/13/17.
 */

public class MemberListAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<User> mDataSource;

    public MemberListAdapter(android.content.Context context, ArrayList<User> items) {
        mContext = context;
        mDataSource = items;
        mInflater = (LayoutInflater) mContext.getSystemService(android.content.Context.LAYOUT_INFLATER_SERVICE);


    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get view for row item
        View rowView = mInflater.inflate(R.layout.list_item_member, parent, false);

        TextView userDisplayName =
                (TextView) rowView.findViewById(R.id.usernameView);

        TextView userEmail =
                (TextView) rowView.findViewById(R.id.userEmailView);


        User user = (User) getItem(position);

        userDisplayName.setText(user.getUserName());
        userEmail.setText(user.getEmail());

        return rowView;
    }


    @Override
    public int getCount() {
        return mDataSource.size();
    }


    public void setmDataSource(ArrayList<User> mDataSource) {
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
