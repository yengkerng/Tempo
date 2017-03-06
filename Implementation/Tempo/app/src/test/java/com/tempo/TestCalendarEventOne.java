package com.tempo;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.EventDateTime;
import com.tempo.Model.CalendarEvent;
import com.tempo.Model.User;

import org.junit.Test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Costin on 2/25/17.
 */

public class TestCalendarEventOne {

    @Test
    public void TestGetEventNameAccurateEvent() {
        String name = "Running a Test";
        String description = "We are trying to run a JUnit Test";
        String location = "On the Computer";
        EventDateTime start = new EventDateTime();
        EventDateTime end = new EventDateTime();
        EventDateTime notificationTime = new EventDateTime();
        DateTime startDate = new DateTime(5);
        DateTime endDate = new DateTime(30);
        DateTime notTime = new DateTime(1);
        ArrayList<User> attendees = null;
        start.setDate(startDate);
        end.setDate(endDate);
        notificationTime.setDate(notTime);
        CalendarEvent newEvent = new CalendarEvent(name, description, location, start, end, attendees, notificationTime);
        assertEquals(name, newEvent.getEventName());
    }

    @Test
    public void TestGetEventDescriptionAccurateEvent() {
        String name = "Running a Test";
        String description = "We are trying to run a JUnit Test";
        String location = "On the Computer";
        EventDateTime start = new EventDateTime();
        EventDateTime end = new EventDateTime();
        EventDateTime notificationTime = new EventDateTime();
        DateTime startDate = new DateTime(5);
        DateTime endDate = new DateTime(30);
        DateTime notTime = new DateTime(1);
        ArrayList<User> attendees = null;
        start.setDate(startDate);
        end.setDate(endDate);
        notificationTime.setDate(notTime);
        CalendarEvent newEvent = new CalendarEvent(name, description, location, start, end, attendees, notificationTime);
        assertEquals(description, newEvent.getEventDescription());
    }
}
