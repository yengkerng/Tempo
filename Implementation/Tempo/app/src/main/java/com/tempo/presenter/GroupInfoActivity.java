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

        memberList = new ArrayList<>();

        memberListAdapter = new MemberListAdapter(this, memberList);
        memberListView.setAdapter(memberListAdapter);


    }


}
