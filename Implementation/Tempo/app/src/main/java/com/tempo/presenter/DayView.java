package com.tempo.presenter;

import com.tempo.model.CalendarEvent;

import java.util.Date;
import java.util.List;

/**
 * Created by Alex on 2/3/2017.
 */

public abstract class DayView extends CalendarView {

    public DayView(List<CalendarEvent> events, Date startDate, Date endDate) {
        super(events, startDate, endDate);
    }

    @Override
    public abstract void display();

}
