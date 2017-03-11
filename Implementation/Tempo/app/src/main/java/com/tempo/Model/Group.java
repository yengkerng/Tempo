package com.tempo.Model;

import com.tempo.Presenter.CalendarManager;

import java.util.ArrayList;
import com.tempo.Model.User;

/**
 * Created by andrewcofano on 2/2/17.
 */

public class Group {
    protected String name;
    private User admin;

    private ArrayList<User> members;
    private CalendarManager calendar;

    public Group(String name, User admin, ArrayList<User> members, CalendarManager calendar) {
        this.name = name;
        this.admin = admin;
        this.members = members;
        this.calendar = calendar;
    }

    public Group(String name, User admin, ArrayList<User> members) {
        this.name = name;
        this.admin = admin;
        this.members = members;
        this.calendar = calendar;
    }


    public void addUserToGroup(User userName) {
        members.add(userName);
    }

    public boolean deleteUserFromGroup(User thisUser) {
        boolean returnVal = false;
        for(int i = 0; i < members.size(); i++) {
            System.out.println("Size of members array: " + members.size());
            System.out.println("This userName" + thisUser.getUserName());
            System.out.println("Members current username at " + i + members.get(i).getUserName());
            if(members.get(i).getUserName().equals(thisUser.getUserName())) {
                members.remove(i);
                returnVal = true;
            }
        }
        return returnVal;
    }

    public ArrayList<User> getMembers() {
        return members;
    }

    public String getName() {
        return name;
    }

    public String[] getMeetingTimes() {
        return null;
    }

    public void displayMeetingTimes() {
    }

    public int getMemberCount() {
        return members.size();
    }

    public User getAdmin() {
        return admin;
    }
}
