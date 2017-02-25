package com.tempo.Model;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jessie on 2/4/17.
 */

public class User {
    private String userName;
    private String email;
    private ArrayList<Group> groups;

    public User() {}

    public User(String userName, String email) {
        this.userName = userName;
        this.email = email;
    }

    public void lookUpUser(String email) {
    }

    public Group createNewGroup(String groupName, User admin) {
        ArrayList<User> members = new ArrayList<User>();
        members.add(admin);
        Group newGroup = new Group(groupName, admin, members, null);
        newGroup.name = groupName;
        admin.groups.add(newGroup);
        return newGroup;
    }

    public String getUserName() {
        return this.userName;
    }

    public boolean deleteGroup(String groupName) {
        for(int i = 0; i < this.groups.size(); i++) {
            if(this.groups.get(i).name.equals(groupName)) {
                this.groups.remove(i);
                return true;
            }
        }
        //Throw an exception here or somehow display to user that they aren't in a group with that name
        return false;
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

    public ArrayList<Group> getGroups () {
        return groups;
    }

    public void changeLanguage(String Language) {
    }

}
