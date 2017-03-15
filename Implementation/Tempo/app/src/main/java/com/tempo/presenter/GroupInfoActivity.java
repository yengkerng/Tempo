package com.tempo.presenter;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.tempo.model.Group;
import com.tempo.model.User;

import java.util.ArrayList;
import java.util.List;


public class GroupInfoActivity extends Activity {

    private String groupName;
    private TextView groupNameView;

    private ListView memberListView;
    private MemberListAdapter memberListAdapter;
    private ArrayList<User> memberList;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_info);

        groupName = getIntent().getExtras().getString("groupName");

        groupNameView = (TextView) findViewById(R.id.groupNameView);
        groupNameView.setText(groupName);
        memberListView = (ListView) findViewById(R.id.memberList);

        inflateUserList();



    }

    public void inflateUserList() {
        memberListAdapter = new MemberListAdapter(this, new ArrayList<String>());

        DatabaseAccess.getGroupMembersWithCallback(new SimpleCallback<List<String>>() {
            @Override
            public void callback(List<String> data) {
                memberListAdapter.setmDataSource((ArrayList<String>)data);
                memberListView.setAdapter(memberListAdapter);
            }
        }, groupName);

    }


}
