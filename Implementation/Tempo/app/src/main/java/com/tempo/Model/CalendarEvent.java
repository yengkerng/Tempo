package com.tempo.Model;

import java.util.ArrayList;
import java.util.Date;

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
    private Date startTime;
    private Date endTime;

    private Group group;
    private ArrayList<User> attendees;
    private Date eventNotification;


    public CalendarEvent(String eventName, String eventDescription, String location,
                         Date startTime, Date endTime, ArrayList<User> attendees, Date eventNotification) {
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

    public Date getStartTime() {
        return startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public ArrayList<User> getAttendees() {
        return attendees;
    }

    public Date getEventNotification() {
        return eventNotification;
    }


    public boolean isValidEventTime() {
        int comparison = this.endTime.compareTo(this.getStartTime());
        if (comparison > 0) {
            return true;
        }
        else {
            return false;
        }
    }

    public void addUserToEvent(User user) {
        this.attendees.add(user);
    }

    
}
