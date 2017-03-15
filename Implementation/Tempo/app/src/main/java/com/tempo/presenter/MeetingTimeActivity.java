package com.tempo.presenter;

import android.os.Bundle;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;

import com.google.api.client.util.DateTime;
import com.tempo.model.CalendarEvent;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MeetingTimeActivity extends Activity {

    private ViewGroup rootView;
    private View formView, meetingTimeView;
    private String group;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_time);

        group = getIntent().getStringExtra("group");

        rootView = (ViewGroup) findViewById(R.id.activity_meeting_time);

        LayoutInflater inflater = getLayoutInflater();
        formView = inflater.inflate(R.layout.meeting_time_form, null);
        meetingTimeView = inflater.inflate(R.layout.meeting_times, null);

        rootView.addView(formView);

        setSubmitListener();

    }

    private void setSubmitListener() {

        findViewById(R.id.meeting_time_form_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final long start = MeetingTimeActivity.this.millisFromDatePicker(R.id.meeting_time_start);
                final long end = MeetingTimeActivity.this.millisFromDatePicker(R.id.meeting_time_end);
                final long minuteDelta = Integer.parseInt(((EditText) findViewById(R.id.meeting_time_hours)).getText().toString()) * 60
                    + Integer.parseInt(((EditText) findViewById(R.id.meeting_time_minutes)).getText().toString());
                DatabaseAccess.getAllMembersCalendarEventsWithCallback(new SimpleCallback<List<List<CalendarEvent>>>() {
                    @Override
                    public void callback(List<List<CalendarEvent>> data) {
                        List<MeetingTimeAlgorithm.MeetingTime> meetingTimes =
                                MeetingTimeAlgorithm.run(data, start, end, minuteDelta * 60 * 1000);
                        for (MeetingTimeAlgorithm.MeetingTime meetingTime : meetingTimes) {
                            System.out.println("A meeting for " + meetingTime.getAttendance() + " people" +
                                " is available at " + new DateTime(meetingTime.getTime()).toString());
                        }
                    }
                }, group);
            }
        });

    }

    public long millisFromDatePicker(int view) {
        Calendar calendar = Calendar.getInstance();
        DatePicker dateView = (DatePicker) findViewById(view);
        calendar.set(dateView.getYear(), dateView.getMonth(), dateView.getDayOfMonth());
        return calendar.getTimeInMillis();
    }

}
