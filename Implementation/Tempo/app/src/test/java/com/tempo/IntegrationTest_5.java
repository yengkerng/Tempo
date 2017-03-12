package com.tempo;

import com.google.android.gms.tasks.Task;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.EventDateTime;
import com.tempo.Model.CalendarEvent;
import com.tempo.Model.Group;
import com.tempo.Model.User;
import com.tempo.Presenter.CalendarManager;
import com.tempo.Presenter.CalendarView;
import com.tempo.Presenter.MonthView;

import org.junit.Test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Yeng on 3/5/17.
 * This will test more integration of the User and the Group classes with CalendarManager
 */


public class IntegrationTest_5 {
    /**
     * This method tests the basic integration of the Group, User, and CalendarManager classes by
     * creating a new calendarManager without any null attributes and testing to make sure that
     * when a new group is created from the user, that group's members can be accessed within the group and
     * the calendarManager object associated with that user's group as well.
     */
    @Test
    public void TestIntegrationUserGroupAddUser() {
        User admin = new User("Jessie", "smithygirl@gmail.com");
        EventDateTime startDate = new EventDateTime();
        Date start_date = new Date();
        EventDateTime endDate = new EventDateTime();
        Date end_date = new Date();
        ArrayList<User> members = new ArrayList<User>();
        members.add(admin);
        ArrayList<CalendarEvent> events = new ArrayList<CalendarEvent>();
        MonthView testView = new MonthView(events, start_date, end_date);
        CalendarManager manager = new CalendarManager(testView, admin);
        User member1 = new User("Yeng", "ytan@calpoly.edu");
        User member2 = new User("Falessi", "dfalessi@calpoly.edu");
        members.add(member1);
        Group testGroup = new Group("Test Group", member1, members, null);
        member1.createNewGroup(testGroup);
        assertEquals(false, member1.getGroups().get(0).getMembers().contains(member2));
        testGroup.addUserToGroup(member2);
        assertEquals(true, member1.getGroups().get(0).getMembers().contains(member2));
    }
    /**
     * This method tests the basic integration of the Group, User, and CalendarManager classes by
     * creating a new calendarManager without any null attributes and testing to make sure that
     * when a new group is created from the user, that group is created within the calendarManager
     * object associated with that user's group as well.
     */
    @Test
    public void TestIntegrationUserGroupGetGroups() {
        User admin = new User("Jessie", "smithygirl@gmail.com");
        EventDateTime startDate = new EventDateTime();
        Date start_date = new Date();
        EventDateTime endDate = new EventDateTime();
        Date end_date = new Date();
        ArrayList<User> members = new ArrayList<User>();
        members.add(admin);
        ArrayList<CalendarEvent> events = new ArrayList<CalendarEvent>();
        MonthView testView = new MonthView(events, start_date, end_date);
        CalendarManager manager = new CalendarManager(testView, admin);
        Group testGroup = new Group("Test Group", admin, members, manager);
        admin.createNewGroup(testGroup);
        assertEquals(true, admin.getGroups().contains(testGroup));
    }
}