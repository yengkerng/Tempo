package com.tempo.Model;

import java.util.ArrayList;

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

    public void addUserToGroup(User userName) {
        members.add(userName);
    }

    public void deleteUserFromGroup(String userName) {
    }

    public String[] getMeetingTimes() {
        return null;
    }

    public void displayMeetingTimes() {
    }

    public int getMemberCount() {
        return members.size();
    }
}
