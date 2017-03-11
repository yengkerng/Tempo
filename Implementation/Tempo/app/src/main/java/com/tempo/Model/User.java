package com.tempo.Model;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jessie on 2/4/17.
 * Edited by Alex on 3/5/17.
 */

public class User {
    private String userName;
    private String email;
    private List<Group> groups;

    public User() {}

    public User(String userName, String email) {
        this.userName = userName;
        this.email = email;
        this.groups = new ArrayList<>();
    }

    public void lookUpUser(String email) {
    }

    public Group createNewGroup(String groupName) {
        ArrayList<User> members = new ArrayList<User>();
        members.add(this);
        Group newGroup = new Group(groupName, this, members, null);
        newGroup.name = groupName;
        groups.add(newGroup);
        return newGroup;
    }

    public Group createNewGroup(Group newGroup) {
        groups.add(newGroup);
        return newGroup;
    }

    public String getUserName() {
        return this.userName;
    }

    public boolean leaveGroup(String groupName) {
        for(int i = 0; i < this.groups.size(); i++) {
            if(this.groups.get(i).name.equals(groupName)) {
                this.groups.remove(i);
                return true;
            }
        }
        //Throw an exception here or somehow display to user that they aren't in a group with that name
        return false;
    }

    public List<Group> getGroups () {
        return groups;
    }

    public void changeLanguage(String Language) {
    }

}
