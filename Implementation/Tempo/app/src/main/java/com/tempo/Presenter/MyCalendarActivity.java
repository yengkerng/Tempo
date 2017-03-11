package com.tempo.Presenter;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.Toast;

import com.alamkanak.weekview.MonthLoader;
import com.alamkanak.weekview.WeekView;

import com.alamkanak.weekview.WeekViewEvent;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.model.Events;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tempo.Model.CalendarEvent;
import com.tempo.Model.User;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class MyCalendarActivity extends Activity {

    public enum CalendarType {
        MONTH, WEEK, DAY
    }

    public class MyDate {
        public int year;
        public int month;
        public int day;

        public MyDate(int year, int month, int day) {
            this.year = year;
            this.month = month;
            this.day = day;
        }
    }

    private CalendarManager manager;
    private ViewGroup rootView;
    private View monthView, weekView, dayView, currentView;
    private ListView dayEventsList;
    private EventListAdapter eventListAdapter;
    private CalendarType currentCalendar;
    private CalendarView monthlyCalendar;
    private WeekView weeklyCalendar;

    private EventDateTime currentDate = null;
    private long dateInMillis;
    private String dateString;

    private ArrayList<CalendarEvent> currentDayEventList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_calendar);

        renderCalendar();
        displayEvents();
        setCalendarTransitions();

        //new SyncCalendarTask(Account.getInstance().googleCred).execute();
        DatabaseAccess.createGroup("MyGroupppp", Arrays.asList(new String[] { "14bmkelley", "bitsbots3812" }));

    }

    private void renderCalendar() {

        LayoutInflater inflater = getLayoutInflater();

        rootView  = (ViewGroup) findViewById(R.id.calendarRoot);
        monthView = inflater.inflate(R.layout.calendar_month, null);
        currentView = monthView;
        currentCalendar = CalendarType.MONTH;
        rootView.addView(monthView);

        monthlyCalendar = (CalendarView) findViewById(R.id.monthlyCalendar);
        dateInMillis = monthlyCalendar.getDate();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        dateString = formatter.format(new Date(dateInMillis));
        monthlyCalendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView calendarView, int year, int month, int day) {
                Toast.makeText(getApplicationContext(), day + "/" + month + "/" + year, Toast.LENGTH_LONG).show();
                setDateString(day + "/" + month + "/" + year);
            }
        });

        dayView   = inflater.inflate(R.layout.calendar_day, null);

    }

    private void setDateString(String newDate) {
        dateString = newDate;
    }

    private List<WeekViewEvent> getEvents(int newYear, int newMonth) {
        List<WeekViewEvent> list = new ArrayList<>();
        list.add(new WeekViewEvent());
        return list;
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

           // case WEEK:
                /*rootView.addView(weekView);
                currentView = weekView;
                currentCalendar = CalendarType.WEEK;
                break;*/

            case DAY:
                rootView.addView(dayView);
                currentView = dayView;
                dayEventsList = (ListView) findViewById(R.id.eventList);
                currentDayEventList = new ArrayList<>();
                currentDayEventList.add(new CalendarEvent("Test 1", "Testing", "The Den", new EventDateTime(), new EventDateTime(), new ArrayList<User>(), new EventDateTime()));
                currentDayEventList.add(new CalendarEvent("Test 2", "Testing", "The Den", new EventDateTime(), new EventDateTime(), new ArrayList<User>(), new EventDateTime()));
                currentDayEventList.add(new CalendarEvent("Test 3", "Testing", "The Den", new EventDateTime(), new EventDateTime(), new ArrayList<User>(), new EventDateTime()));
                currentDayEventList.add(new CalendarEvent("Test 4", "Testing", "The Den", new EventDateTime(), new EventDateTime(), new ArrayList<User>(), new EventDateTime()));

                eventListAdapter = new EventListAdapter(this, currentDayEventList);
                if (dayEventsList != null) {
                    dayEventsList.setAdapter(eventListAdapter);

                }
                currentCalendar = CalendarType.DAY;

        }

    }

    private class SyncCalendarTask extends AsyncTask<Void, Void, Void> {

        private GoogleAccountCredential credential;

        SyncCalendarTask(GoogleAccountCredential credential) {
            this.credential = credential;
        }

        @Override
        protected Void doInBackground(Void... params) {
            HttpTransport transport = AndroidHttp.newCompatibleTransport();
            JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
            Calendar calendar = new Calendar.Builder(
                    transport, jsonFactory, credential)
                    .setApplicationName("Tempo")
                    .build();
            try {
                java.util.Calendar cal = java.util.Calendar.getInstance();
                cal.setTime(new Date());
                cal.add(java.util.Calendar.MONTH, -1);
                Date oneMonthInPastDate = cal.getTime();
                DateTime oneMonthInPast = new DateTime(oneMonthInPastDate);
                java.util.Calendar cal2 = java.util.Calendar.getInstance();
                cal2.setTime(new Date());
                cal2.add(java.util.Calendar.MONTH, 1);
                Date oneMonthInFutureDate = cal2.getTime();
                DateTime oneMonthInFuture = new DateTime(oneMonthInFutureDate);
                Events events = calendar.events().list("primary")
                        .setTimeMin(oneMonthInPast)
                        .setTimeMax(oneMonthInFuture)
                        .setOrderBy("startTime")
                        .setSingleEvents(true)
                        .execute();
                List<Event> items = events.getItems();
                FirebaseDatabase.getInstance().getReference().child("users")
                        .child(DatabaseAccess.parseAccountName(credential.getSelectedAccountName()))
                        .child("events").setValue(items);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

    }

}
