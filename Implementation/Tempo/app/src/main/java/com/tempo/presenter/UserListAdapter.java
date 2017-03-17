package com.tempo.presenter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.tempo.model.UserEntry;

import java.util.List;

/**
 * Created by andrewcofano on 3/15/17.
 */

public class UserListAdapter extends BaseAdapter {

    private List<UserEntry> mUsers;

    public UserListAdapter(List<UserEntry> users) {
        mUsers = users;
    }


    @Override
    public int getCount() {
        return mUsers.size();
    }

    @Override
    public Object getItem(int position) {
        return mUsers.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View newView = inflater.inflate(R.layout.list_item_user, parent, false);

        TextView textView = (TextView) newView.findViewById(R.id.userNameView);
        CheckBox checkBox = (CheckBox) newView.findViewById(R.id.checkbox);
        final UserEntry userEntry = (UserEntry) getItem(position);

        textView.setText(userEntry.getText());

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                userEntry.setCheck(isChecked);
            }
        });

        checkBox.setChecked(userEntry.isCheck());

        return newView;
    }

    public void setmDataSource(List<UserEntry> mDataSource) {
        this.mUsers = mDataSource;
    }
}


