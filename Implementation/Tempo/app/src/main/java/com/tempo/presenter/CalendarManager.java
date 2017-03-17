package com.tempo.presenter;

import android.os.AsyncTask;
import android.util.Log;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;
import com.tempo.model.CalendarEvent;
import com.tempo.model.User;
import com.tempo.model.Group;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Calendar;
import java.util.Date;

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

    public List<String> calendarEventListToString(List<CalendarEvent> eventList) {
        StringBuilder currentEvent = new StringBuilder();
        List<String> returnList = new ArrayList<>();
        for(int i = 0; i < eventList.size(); i++) {
            currentEvent.append(eventList.get(i).getEventName());
            currentEvent.append(eventList.get(i).getEventDescription());
            currentEvent.append(eventList.get(i).getLocation());
            returnList.add(currentEvent.toString());
            currentEvent = new StringBuilder();
        }
        return returnList;
    }

    public CalendarView getView() {
        return view;
    }

    public User getUserOwner() {
        return userOwner;
    }

    public void setUserOwner(User userOwner) {
        this.userOwner = userOwner;
    }

    public Group getGroupOwner() {
        return groupOwner;
    }

    /**
     * An asynchronous task that handles the Google Calendar API call.
     * Placing the API calls in their own task ensures the UI stays responsive.
     */
    class MakeRequestTask extends AsyncTask<Void, Void, List<String>> {
        private com.google.api.services.calendar.Calendar mService = null;

        public MakeRequestTask(GoogleAccountCredential credential) {
            HttpTransport transport = AndroidHttp.newCompatibleTransport();
            JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
            mService = new com.google.api.services.calendar.Calendar.Builder(
                    transport, jsonFactory, credential)
                    .setApplicationName("Google Calendar API Android Quickstart")
                    .build();
        }

        /**
         * Background task to call Google Calendar API.
         *
         * @param params no parameters needed for this task.
         */
        @Override
        protected List<String> doInBackground(Void... params) {
            try {
                return calendarEventListToString(getUserEventsFromApi());
            } catch (Exception e) {
                Log.getStackTraceString(e);
                cancel(true);
                return Collections.emptyList();
            }
        }


        // List all user events from the primary calendar starting one month in past and going until one month in advance.
        private List<CalendarEvent> getUserEventsFromApi() throws IOException {
           //Get calendar DateTimes for one month in past and one month in future
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date());
            cal.add(Calendar.MONTH, -1);
            Date oneMonthInPastDate = cal.getTime();
            DateTime oneMonthInPast = new DateTime(oneMonthInPastDate);
            Calendar cal2 = Calendar.getInstance();
            cal2.setTime(new Date());
            cal2.add(Calendar.MONTH, 1);
            Date oneMonthInFutureDate = cal2.getTime();
            DateTime oneMonthInFuture = new DateTime(oneMonthInFutureDate);
            //Instantiate all other CalendarEvent attributes
            String eventName;
            String eventDescription;
            String location;
            long startTime;
            long endTime;
            String owner;
            //Create Calendar Event list array
            List<CalendarEvent> userEvents = new ArrayList<>();
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
                    event.getStart().getDate();
                }
                //Get event information from user's google calendar and save as calendar event object
                eventName = event.getId();
                eventDescription = event.getDescription();
                location = event.getLocation();
                startTime = event.getStart().getDateTime().getValue() + event.getStart().getDateTime().getTimeZoneShift();
                endTime = event.getEnd().getDateTime().getValue() + event.getStart().getDateTime().getTimeZoneShift();
                owner = event.getCreator().getEmail();
                CalendarEvent currentEvent = new CalendarEvent(eventName, eventDescription, location, startTime, endTime, owner);
                //Add current user event to the user's Calendar Event array list
                userEvents.add(currentEvent);
            }
            return userEvents;
        }
    }

    public String addEvent(String name) {
        return name;
    }


    public List<CalendarEvent> getUserEvents() {
        return new ArrayList<>();
    }

}
