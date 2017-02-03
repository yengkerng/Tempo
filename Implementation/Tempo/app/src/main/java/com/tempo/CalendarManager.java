package com.tempo;

/**
 * Created by Alex on 2/3/2017.
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

    public CalendarEvent addEvent(String name) {
        return new CalendarEvent();
    }

    public void deleteEvent(String name) {
        return;
    }

    public List<CalendarEvent> getUserEvents(String email) {
        return new List<CalendarEvent>();
    }
}
