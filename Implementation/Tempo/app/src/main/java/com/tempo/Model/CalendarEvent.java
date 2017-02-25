package com.tempo.Model;

import com.google.api.services.calendar.model.EventDateTime;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by andrewcofano on 2/13/17.
 */

public class CalendarEvent {

    private String eventName;
    private String eventDescription;
    /**
     * The location of the event in the form of a standard address
     */
    private String location;
    private EventDateTime startTime;
    private EventDateTime endTime;

    private Group group;
    private ArrayList<User> attendees;
    private EventDateTime eventNotification;


    public CalendarEvent(String eventName, String eventDescription, String location,
                         EventDateTime startTime, EventDateTime endTime, ArrayList<User> attendees, EventDateTime eventNotification) {
        this.eventName = eventName;
        this.eventDescription = eventDescription;
        this.location = location;
        this.startTime = startTime;
        this.endTime = endTime;
        this.attendees = attendees;
        this.eventNotification = eventNotification;
    }


    public String getEventName() {
        return eventName;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public String getLocation() {
        return location;
    }

    public EventDateTime getStartTime() {
        return startTime;
    }

    public EventDateTime getEndTime() {
        return endTime;
    }

    public ArrayList<User> getAttendees() {
        return attendees;
    }

    public EventDateTime getEventNotification() {
        return eventNotification;
    }

    public void addUserToEvent(User user) {
        this.attendees.add(user);
    }

    
}
