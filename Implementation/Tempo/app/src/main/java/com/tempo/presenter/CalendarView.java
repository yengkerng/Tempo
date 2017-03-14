package com.tempo.presenter;

import com.tempo.model.CalendarEvent;

import java.util.Date;
import java.util.List;

/**
 * Created by Alex on 2/3/2017.
 */
public abstract class CalendarView {
    private List<CalendarEvent> events;
    private Date startDate;
    private Date endDate;

    public CalendarView(List<CalendarEvent> events, Date startDate, Date endDate) {
        this.events = events;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public abstract void display();
}
