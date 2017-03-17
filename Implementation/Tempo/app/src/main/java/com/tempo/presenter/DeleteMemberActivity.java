package com.tempo.presenter;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.tempo.model.UserEntry;

import java.util.ArrayList;
import java.util.List;

public class DeleteMemberActivity extends Activity {

    private static ArrayList<UserEntry> userList;
    private List<String> databaseUsers;
    private ListView listView;
    private UserListAdapter adapter;
    private String groupName;

    private Button deleteMemberConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_member);
        groupName = getIntent().getExtras().getString("groupName");
        fillListWithUsers();
        setConfirmDeleteMemberListener();

    }

    private void fillListWithUsers() {
        listView = (ListView) findViewById(R.id.databaseUsersList);
        userList = (ArrayList<UserEntry>) getLastCustomNonConfigurationInstance();
        if (userList == null) {
            userList = new ArrayList<UserEntry>();

        }
        adapter = new UserListAdapter(userList);
        DatabaseAccess.getGroupMembersWithCallback(new SimpleCallback<List<String>>() {
            @Override
            public void callback(List<String> data) {
                for (String user: data) {
                    if (!userList.contains(new UserEntry(user, false))) {
                        userList.add(new UserEntry(user, false));
                    }
                }
                adapter.setmDataSource(userList);
                listView.setAdapter(adapter);
            }
        }, groupName);

        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();


    }


    private void setConfirmDeleteMemberListener() {

        deleteMemberConfirm = (Button) findViewById(R.id.deleteMemberConfirm);

        deleteMemberConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Got clicked");
                deleteMembersFromGroup();
                closeActivity();
            }
        });

    }

    private void closeActivity() {
        userList.clear();
        adapter.setmDataSource(userList);
        this.finish();
    }

    private void deleteMembersFromGroup() {

        for (UserEntry user: userList) {
            if (user.isCheck()){
                DatabaseAccess.deleteUserFromGroup(user.getText(), groupName);

            }
        }
    }


    public ArrayList<UserEntry> getLastCustomNonConfigurationInstance() {
        return userList;
    }
}
