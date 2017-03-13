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
 * Created by Jessie on 2/25/17.
 */

public class TestCalendarEventThree {

    @Test
    public void TestGetEventOwner() {
        String name = "Running a Test";
        String description = "We are trying to run a JUnit Test";
        String location = "On the Computer";
        User owner = new User("Jessie", "smithygirl@gmail.com");
        long start = 0;
        long end = 1;
        ArrayList<User> attendees = null;
        CalendarEvent newEvent = new CalendarEvent(name, description, location, start, end, owner.getUserName());
        assertEquals(owner.getUserName(), newEvent.getOwner());
    }

    @Test
    public void TestSetEventOwner() {
        String name = "Running a Test";
        String description = "We are trying to run a JUnit Test";
        String location = "On the Computer";
        User randomUser = new User("Falessi", "dfalessi@calpoly.edu");
        User owner = new User("Jessie", "smithygirl@gmail.com");
        long start = 0;
        long end = 1;
        CalendarEvent newEvent = new CalendarEvent(name, description, location, start, end, null);
        newEvent.setOwner(owner.getUserName());
        assertEquals(owner.getUserName(), newEvent.getOwner());
    }
}
