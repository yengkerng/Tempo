package com.tempo.presenter;

import com.tempo.model.CalendarEvent;

import java.util.Date;
import java.util.List;

/**
 * Created by Alex on 2/3/2017.
 */

public class WeekView extends CalendarView {
    public WeekView(List<CalendarEvent> events, Date startDate, Date endDate) {
        super(events, startDate, endDate);
    }

    public void display(){

    }
}
