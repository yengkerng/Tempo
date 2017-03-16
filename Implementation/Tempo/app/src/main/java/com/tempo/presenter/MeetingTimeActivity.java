package com.tempo.presenter;

import android.os.Bundle;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.google.api.client.util.DateTime;
import com.tempo.model.CalendarEvent;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MeetingTimeActivity extends Activity {

    private ViewGroup rootView;
    private View formView, meetingTimeView;
    private String group, userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_time);

        group = getIntent().getStringExtra("group");
        userEmail = Account.getInstance().userEmail;

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

                final String title = ((TextView) findViewById(R.id.meeting_time_title)).getText().toString();
                final String location = ((TextView) findViewById(R.id.meeting_time_location)).getText().toString();
                final long start = MeetingTimeActivity.this.millisFromDatePicker(R.id.meeting_time_start);
                final long end = MeetingTimeActivity.this.millisFromDatePicker(R.id.meeting_time_end);
                final long delta = (Integer.parseInt(((EditText) findViewById(R.id.meeting_time_hours)).getText().toString()) * 60
                    + Integer.parseInt(((EditText) findViewById(R.id.meeting_time_minutes)).getText().toString())) * 60 * 1000;

                DatabaseAccess.getAllMembersCalendarEventsWithCallback(new SimpleCallback<List<List<CalendarEvent>>>() {
                    @Override
                    public void callback(List<List<CalendarEvent>> data) {

                        final List<MeetingTimeAlgorithm.MeetingTime> meetingTimes =
                                MeetingTimeAlgorithm.run(data, start, end, delta);

                        rootView.removeView(formView);
                        rootView.addView(meetingTimeView);

                        ListView meetingTimeList = (ListView) findViewById(R.id.meeting_times);
                        MeetingTimeAdapter adapter = new MeetingTimeAdapter(MeetingTimeActivity.this, meetingTimes);

                        meetingTimeList.setAdapter(adapter);

                        meetingTimeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                CalendarEvent event = new CalendarEvent(title, "This event created by Tempo!", location, start, start + delta, userEmail);
                                DatabaseAccess.addEventToGroup(group, event, new SimpleCallback<Void>() {
                                    @Override
                                    public void callback(Void data) {
                                        finish();
                                    }
                                });
                            }
                        });

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
