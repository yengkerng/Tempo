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
    private long startTime;
    private long endTime;
    private String owner;

    public CalendarEvent() {

    }

    public CalendarEvent(String eventName, String eventDescription, String location,
                         long startTime, long endTime, String owner) {
        this.eventName = eventName;
        this.eventDescription = eventDescription;
        this.location = location;
        this.startTime = startTime;
        this.endTime = endTime;
        this.owner = owner;
    }


    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {this.eventName = eventName;}

    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {this.eventDescription = eventDescription;}

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {this.location = location;}

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {this.startTime = startTime;}

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {this.endTime = endTime;}

    public String getOwner() { return owner;}

    public void setOwner(String owner) {this.owner = owner;}

}
