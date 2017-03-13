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
 * Created by Yeng on 2/25/17.
 */

public class TestCalendarEventTwo {

    @Test
    public void TestGetLocationAccurateEvent() {
        String name = "Running a Test";
        String description = "We are trying to run a JUnit Test";
        String location = "On the Computer";
        User owner = new User("Jessie", "smithygirl@gmail.com");
        long start = 0;
        long end = 1;
        CalendarEvent newEvent = new CalendarEvent(name, description, location, start, end, owner.getUserName());
        assertEquals(location, newEvent.getLocation());
    }

    @Test
    public void TestGetStartTime() {
        String name = "Running a Test";
        String description = "We are trying to run a JUnit Test";
        String location = "On the Computer";
        long start = 0;
        long end = 1;
        CalendarEvent newEvent = new CalendarEvent(name, description, location, start, end, null);
        assertEquals(start, newEvent.getStartTime());
    }

    @Test
    public void TestGetEndTimeDate() {
        String name = "Running a Test";
        String description = "We are trying to run a JUnit Test";
        String location = "On the Computer";
        long start = 0;
        long end = 1;
        CalendarEvent newEvent = new CalendarEvent(name, description, location, start, end, null);
        assertEquals(end, newEvent.getEndTime());
    }
}
