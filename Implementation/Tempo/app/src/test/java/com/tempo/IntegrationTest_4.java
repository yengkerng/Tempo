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
 * Created by Andrew on 3/5/17.
 * This will test more integration of the Group and the CalendarManager classes
 */


public class IntegrationTest_4 {
    /**
     * This method tests the basic integration of the Group and CalendarManager classes by
     * creating a new calendarManager without any null attributes and getting these attributes (specifically
     * the calendar attribute) from both the Group class and the manager itself to make sure they are being implemented
     * together correctly
     */
    @Test
    public void TestCalendarManagerGroupGetCal() {
        User admin = new User("Jessie", "smithygirl@gmail.com");
        long startDate = 0;
        Date start_date = new Date();
        long endDate = 1;
        Date end_date = new Date();
        ArrayList<User> members = new ArrayList<User>();
        members.add(admin);
        ArrayList<CalendarEvent> events = new ArrayList<CalendarEvent>();
        MonthView testView = new MonthView(events, start_date, end_date);
        CalendarManager manager = new CalendarManager(testView, admin);
        Group testGroup = new Group("Test Group", admin, members, manager);
        admin.createNewGroup(testGroup);
        CalendarEvent event1 = new CalendarEvent("Study", "Finish the Project", "Library", startDate, endDate, null);
        CalendarEvent event2 = new CalendarEvent("Study", "Finish the Project", "Library", startDate, endDate, null);
        events.add(event1);
        events.add(event2);
        assertEquals(manager, admin.getGroups().get(0).getCalendar());
    }
    /**
     * This method tests the basic integration of the Group and CalendarManager classes by
     * creating a new calendarManager without any null attributes and getting these attributes (specifically
     * the event name attribute) from both the Group class and the manager itself to make sure they are being implemented
     * together correctly
     */
    @Test
    public void TestCalendarManagerGroup() {
        User admin = new User("Jessie", "smithygirl@gmail.com");
        long startDate = 0;
        Date start_date = new Date();
        long endDate = 1;
        Date end_date = new Date();
        ArrayList<User> members = new ArrayList<User>();
        members.add(admin);
        ArrayList<CalendarEvent> events = new ArrayList<CalendarEvent>();
        MonthView testView = new MonthView(events, start_date, end_date);
        CalendarManager manager = new CalendarManager(testView, admin);
        Group testGroup = new Group("Test Group", admin, members, manager);
        admin.createNewGroup(testGroup);
        CalendarEvent event1 = new CalendarEvent("Study", "Finish the Project", "Library", startDate, endDate, null);
        CalendarEvent event2 = new CalendarEvent("Study", "Finish the Project", "Library", startDate, endDate, null);
        events.add(event1);
        events.add(event2);
        assertEquals(event1.getEventName(), manager.addEvent(event1.getEventName()));
    }

}