package com.tempo.Presenter;

import com.tempo.Model.CalendarEvent;

import java.util.Date;
import java.util.List;

/**
 * Created by Alex on 2/3/2017.
 */

public class DayView extends CalendarView {
    public DayView(List<CalendarEvent> events, Date startDate, Date endDate) {
        super(events, startDate, endDate);
    }

    public void display(){

    }
}
