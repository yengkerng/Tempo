package com.tempo.Presenter;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MyCalendarActivity extends Activity {

    public enum CalendarType {
        MONTH, WEEK, DAY
    }

    private CalendarManager manager;
    private ViewGroup rootView;
    private View monthView, weekView, dayView, currentView;
    private CalendarType currentCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_calendar);

        renderCalendar();
        displayEvents();
        setCalendarTransitions();

    }

    private void renderCalendar() {

        LayoutInflater inflater = getLayoutInflater();

        rootView  = (ViewGroup) findViewById(R.id.calendarRoot);

        monthView = inflater.inflate(R.layout.calendar_view, rootView);
        currentView = monthView;
        currentCalendar = CalendarType.MONTH;

        weekView  = inflater.inflate(R.layout.calendar_view, null);
        dayView   = inflater.inflate(R.layout.calendar_view, null);

    }

    private void displayEvents() {

    }

    private void setCalendarTransitions() {

        // Set listener for switching to month view
        findViewById(R.id.monthView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyCalendarActivity.this.setCurrentCalendar(CalendarType.MONTH);
            }
        });

        // Set listener for switching to week view
        findViewById(R.id.weekView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyCalendarActivity.this.setCurrentCalendar(CalendarType.WEEK);
            }
        });

        // Set listener for switching to day view
        findViewById(R.id.dayView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyCalendarActivity.this.setCurrentCalendar(CalendarType.DAY);
            }
        });

    }

    public void setCurrentCalendar(CalendarType calendar) {

        if (calendar == currentCalendar) {
            return;
        }

        rootView.removeView(currentView);

        switch (calendar) {

            case MONTH:
                rootView.addView(monthView);
                currentView = monthView;
                currentCalendar = CalendarType.MONTH;
                break;

            case WEEK:
                rootView.addView(weekView);
                currentView = weekView;
                currentCalendar = CalendarType.WEEK;
                break;

            case DAY:
                rootView.addView(dayView);
                currentView = dayView;
                currentCalendar = CalendarType.DAY;

        }

    }

}
