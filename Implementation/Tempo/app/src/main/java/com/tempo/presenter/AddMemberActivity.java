package com.tempo.presenter;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.tempo.model.UserEntry;

import java.util.ArrayList;
import java.util.List;


public class AddMemberActivity extends Activity {

    private ArrayList<UserEntry> userList;
    private ListView listView;
    private UserListAdapter adapter;
    private String groupName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_member);
        groupName = getIntent().getExtras().getString("groupName");
        fillListWithUsers();
        setConfirmAddMemberListener();

    }

    private void fillListWithUsers() {
        listView = (ListView) findViewById(R.id.databaseUsersList);
        userList = (ArrayList<UserEntry>)getLastCustomNonConfigurationInstance();
        if (userList == null) {
            userList = new ArrayList<>();

        }
        adapter = new UserListAdapter(userList);
        DatabaseAccess.getUsersNotInGroup(new SimpleCallback<List<String>>() {
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


    }


    private void setConfirmAddMemberListener() {

        Button addMemberConfirm = (Button) findViewById(R.id.addMemberSubmit);

        addMemberConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addMembersToGroup();
                closeActivity();
            }
        });

    }

    private void closeActivity() {
        listView = null;
        userList.clear();
        this.finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
        listView = null;
        userList.clear();
    }

    private void addMembersToGroup() {

        for (UserEntry user: userList) {
            if (user.isCheck()){
                DatabaseAccess.addUserToGroup(user.getText(), groupName);
            }
        }
    }


    public List<UserEntry> getLastCustomNonConfigurationInstance() {
        return userList;
    }
}
