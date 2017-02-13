package com.tempo.Presenter;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;
import com.tempo.Model.CalendarEvent;
import com.tempo.Model.User;
import com.tempo.Model.Group;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Alex on 2/3/2017.
 * Last edit: Jessie 2/12/2017.
 */

public class CalendarManager {
    private CalendarView view;
    private User userOwner;
    private Group groupOwner;

    public CalendarManager(CalendarView view, User userOwner) {
        this.view = view;
        this.userOwner = userOwner;
    }

    public CalendarManager(CalendarView view, Group groupOwner) {
        this.view = view;
        this.groupOwner = groupOwner;
    }

    private List<CalendarEvent> getUserEventsFromApi() throws IOException {
        // List the next 10 events from the primary calendar.
        DateTime now = new DateTime(System.currentTimeMillis());
        List<String> eventStrings = new ArrayList<String>();
        Events events = null; /*mService.events().list("primary")
                .setTimeMin(now)
                .setOrderBy("startTime")
                .setSingleEvents(true)
                .execute();*/
        List<Event> items = events.getItems();

        for (Event event : items) {
            DateTime start = event.getStart().getDateTime();
            if (start == null) {
                // All-day events don't have start times, so just use
                // the start date.
                start = event.getStart().getDate();
            }
            //Below will be adding the events to the CalendarEvent List once the CalendarEvent class has been made
           // eventStrings.add(
                   // String.format("%s (%s)", event.getSummary(), start));
        }
        return null;
    }

    public CalendarEvent addEvent(String name) {
        return null;
    }

    public void deleteEvent(String name) {
        return;
    }

    public List<CalendarEvent> getUserEvents(String email) {
        return null;
    }
}
