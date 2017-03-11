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
 * Created by Brandon on 3/5/17.
 * This will test more integration of the CalendarEvent and the CalendarManager classes
 */


public class IntegrationTest_3 {
    @Test
    public void TestCalendarEventandManagerUserEvents() {
        EventDateTime startDate = new EventDateTime();
        Date start_date = new Date();
        EventDateTime endDate = new EventDateTime();
        Date end_date = new Date();
        CalendarEvent thisEvent = new CalendarEvent("Study", "Finish the Project", "Library", startDate, endDate, null, null);
        ArrayList<CalendarEvent> events = new ArrayList<CalendarEvent>();
        MonthView testView = new MonthView(events, start_date, end_date);
        User admin = new User("Jessie", "smithygirl@gmail.com");
        CalendarManager manager = new CalendarManager(testView, admin);
        System.out.println("events: " + manager.getUserEvents("smithygirl@gmail.com"));
        System.out.println("events array: " + events);
        assertEquals(events, manager.getUserEvents("smithygirl@gmail.com"));
    }

    @Test
    public void TestCalendarEventandManager() {
        EventDateTime startDate = new EventDateTime();
        Date start_date = new Date();
        EventDateTime endDate = new EventDateTime();
        Date end_date = new Date();
        CalendarEvent thisEvent = new CalendarEvent("Study", "Finish the Project", "Library", startDate, endDate, null, null);
        ArrayList<CalendarEvent> events = new ArrayList<CalendarEvent>();
        MonthView testView = new MonthView(events, start_date, end_date);
        User admin = new User("Jessie", "smithygirl@gmail.com");
        CalendarManager manager = new CalendarManager(testView, admin);
        System.out.println("events: " + manager.getUserEvents("smithygirl@gmail.com"));
        System.out.println("events array: " + events);
        assertEquals(events, manager.getUserEvents("smithygirl@gmail.com"));
    }
}