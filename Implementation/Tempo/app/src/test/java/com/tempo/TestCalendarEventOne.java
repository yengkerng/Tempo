package com.tempo;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.EventDateTime;
import com.tempo.model.CalendarEvent;
import com.tempo.model.User;

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
        long start = 0;
        long end = 1;
        ArrayList<User> attendees = null;
        CalendarEvent newEvent = new CalendarEvent(name, description, location, start, end, null);
        assertEquals(name, newEvent.getEventName());
    }

    @Test
    public void TestGetEventDescriptionAccurateEvent() {
        String name = "Running a Test";
        String description = "We are trying to run a JUnit Test";
        String location = "On the Computer";
        long start = 0;
        long end = 2;
        ArrayList<User> attendees = null;
        CalendarEvent newEvent = new CalendarEvent(name, description, location, start, end, null);
        assertEquals(description, newEvent.getEventDescription());
    }
}
