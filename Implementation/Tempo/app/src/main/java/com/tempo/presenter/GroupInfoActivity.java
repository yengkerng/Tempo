package com.tempo.presenter;

import android.app.Activity;
import android.content.Intent;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_info);

        groupName = getIntent().getExtras().getString("groupName");

        groupNameView = (TextView) findViewById(R.id.groupNameView);
        groupNameView.setText(groupName);
        memberListView = (ListView) findViewById(R.id.memberList);

        inflateUserList();

        setMeetingTimesListener();
        setAddMemberListener();
        setDeleteMemberListener();

    }

    private void setDeleteMemberListener() {
        findViewById(R.id.deleteMemberButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GroupInfoActivity.this, DeleteMemberActivity.class);
                intent.putExtra("groupName", groupName);
                startActivity(intent);
            }
        });
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

    private void setAddMemberListener() {
        findViewById(R.id.addMemberButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GroupInfoActivity.this, AddMemberActivity.class);
                intent.putExtra("groupName", groupName);
                startActivity(intent);
            }
        });
    }

    private void setMeetingTimesListener() {
        findViewById(R.id.findMeetingTimes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GroupInfoActivity.this, MeetingTimeActivity.class);
                intent.putExtra("group", groupName);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        inflateUserList();

    }


}
