package com.tempo.Presenter;

import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
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
 * Last edit: Jessie 2/20/2017.
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
        /*
        // List all user events from the primary calendar starting one month in past and going until one month in advance.
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -1);
        DateTime oneMonthInPast = cal.getTime();
        cal.add(Calendar.MONTH, 1);
        String eventName;
        String eventDescription;
        String location;
        Date startTime;
        Date endTime;
        DateTime oneMonthInFuture = cal.getTime();
        List<CalendarEvent> userEvents = new ArrayList<CalendarEvent>();
        Events events = mService.events().list("primary")
                .setTimeMin(oneMonthInPast)
                .setTimeMax(oneMonthInFuture)
                .setOrderBy("startTime")
                .setSingleEvents(true)
                .execute();
        List<Event> items = events.getItems();

        for (Event event : items) {
            DateTime start = event.getStart().getDateTime();
            if (start == null) {
                // All-day events don't have start times, so just use
                // the start date.
                start = event.getStart().getDate();
            }
            eventName = event.getName();
            eventDescription = event.getDescription();
            location = event.getLocation();
            startTime = event.getStartTime();
            endTime = event.getEndTime();
            CalendarEvent currentEvent = new CalendarEvent(eventName, eventDescription, location, startTime, endTime, null, null);
            userEvents.add(currentEvent);
        }
        return userEvents;
        */
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
