package com.tempo.Model;
import java.util.ArrayList;
/**
 * Created by Jessie on 2/4/17.
 */

public class User {
    private String userName;
    private String email;
    private ArrayList<Group> groups;
    public User(String userName, String email) {
        this.userName = userName;
        this.email = email;
    }

    private void lookUpUser(String email) {

    }

    private Group createNewGroup(String groupName, User admin) {
        ArrayList<User> members = new ArrayList<User>();
        members.add(admin);
        Group newGroup = new Group(groupName, admin, members, null);
        newGroup.name = groupName;
        admin.groups.add(newGroup);
        return newGroup;
    }

    private boolean deleteGroup(String groupName) {
        for(int i = 0; i < this.groups.size(); i++) {
            if(this.groups.get(i).name.equals(groupName)) {
                this.groups.remove(i);
                return true;
            }
        }
        //Throw an exception here or somehow display to user that they aren't in a group with that name
        return false;
    }

    private boolean leaveGroup(String groupName) {
        for(int i = 0; i < this.groups.size(); i++) {
            if(this.groups.get(i).name.equals(groupName)) {
                this.groups.remove(i);
                return true;
            }
        }
        //Throw an exception here or somehow display to user that they aren't in a group with that name
        return false;

    }

    private void changeLanguage(String Language) {

    }

}
