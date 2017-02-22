package com.tempo.Presenter;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tempo.R;

public class MyCalendarActivity extends Activity {

    private CalendarManager manager;
    private ViewGroup root;
    private View month, week, day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_calendar);

        renderCalendar();
        displayEvents();

    }

    private void renderCalendar() {
        LayoutInflater inflater = getLayoutInflater();
        root  = (ViewGroup) findViewById(R.id.root);
        month = inflater.inflate(R.layout.calendar_view, null);
        week  = inflater.inflate(R.layout.calendar_view, null);
        day   = inflater.inflate(R.layout.calendar_view, null);
        root.addView(month);
    }

    private void displayEvents() {

    }

}
