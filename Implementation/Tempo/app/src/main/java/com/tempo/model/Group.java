package com.tempo.model;

import com.tempo.presenter.CalendarManager;

import java.util.*;

/**
 * Created by andrewcofano on 2/2/17.
 */

public class Group {
    protected String name;
    private User admin;

    private List<User> members;
    private CalendarManager calendar;

    public Group(String name, User admin, List<User> members, CalendarManager calendar) {
        this.name = name;
        this.admin = admin;
        this.members = members;
        this.calendar = calendar;
    }

    public Group(String name, User admin, List<User> members) {
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
            if(members.get(i).getUserName().equals(thisUser.getUserName())) {
                members.remove(i);
                returnVal = true;
            }
        }
        return returnVal;
    }

    public List<User> getMembers() {
        return members;
    }

    public String getName() {
        return name;
    }


    public CalendarManager getCalendar () {
        return calendar;
    }

    public int getMemberCount() {
        return members.size();
    }

    public User getAdmin() {
        return admin;
    }
}
