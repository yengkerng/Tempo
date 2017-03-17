package com.tempo.presenter;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;


import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;
import com.google.firebase.database.FirebaseDatabase;
import com.tempo.model.CalendarEvent;
import com.tempo.model.User;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.tempo.presenter.DatabaseAccess.parseAccountName;

public class MyCalendarActivity extends Activity {

    private static final long ONEDAY = ((long)(1000)) * 60 * 60 * 24;

    private View settingsView;

    public enum CalendarType {
        MONTH, WEEK, DAY
    }

    public enum TabType {
        HOME, GROUPS, SETTINGS
    }

    private TableRow calendarTab;
    private ViewGroup rootView;
    private ViewGroup tabRootView;
    private View monthView;
    private View dayView;
    private View currentView;
    private View currentTabView;
    private View homeView;
    private View groupsView;
    private ListView dayEventsList;
    private EventListAdapter eventListAdapter;
    private CalendarType currentCalendar;
    private TabType currentTab;
    private CalendarView monthlyCalendar;

    private String dateString;
    private ArrayList<String> groupList;
    private EditText newGroupEdit;
    private String userEmail;
    private String userDisplayName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Bundle userInfo = getIntent().getExtras();
        userEmail = userInfo.getString("userEmail");
        userDisplayName = userInfo.getString("username");
        Account.getInstance().userEmail = userEmail;
        setContentView(R.layout.activity_my_calendar);

        renderTabs();
        renderCalendar();
        setTabTransitions();
        setCalendarTransitions();

        new SyncCalendarTask(Account.getInstance().googleCred).execute();

    }


    private void renderTabs() {
        LayoutInflater inflater = getLayoutInflater();

        rootView  = (ViewGroup) findViewById(R.id.tabRoot);
        homeView = inflater.inflate(R.layout.home_tab, null);
        currentTabView = homeView;
        currentTab = TabType.HOME;
        rootView.addView(homeView);

        settingsView = inflater.inflate(R.layout.settings_tab, null);
        groupsView   = inflater.inflate(R.layout.group_tab, null);

    }

    private void renderCalendar() {

        long dateInMillis;

        LayoutInflater inflater = getLayoutInflater();

        tabRootView = (ViewGroup) findViewById(R.id.homeRoot);
        monthView = inflater.inflate(R.layout.calendar_month, null);
        currentView = monthView;
        currentCalendar = CalendarType.MONTH;
        tabRootView.addView(monthView);

        monthlyCalendar = (CalendarView) findViewById(R.id.monthlyCalendar);
        dateInMillis = monthlyCalendar.getDate();
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        dateString = formatter.format(new Date(dateInMillis));

        monthlyCalendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView calendarView, int year, int month, int day) {
                Toast.makeText(getApplicationContext(), (month + 1) + "/" + day + "/" + year, Toast.LENGTH_SHORT).show();
                setDateString((month + 1) + "/" + day + "/" + year);
                updateDayView();
            }
        });

        dayView   = inflater.inflate(R.layout.calendar_day, null);

    }

    private void updateDayView() {
        final MyCalendarActivity that = this;
        DatabaseAccess.getUserEventListWithCallback(new SimpleCallback<List<CalendarEvent>>() {
            @Override
            public void callback(List<CalendarEvent> data) {
                eventListAdapter = new EventListAdapter(that, data);
                if (dayEventsList != null) {
                    dayEventsList.setAdapter(eventListAdapter);
                }
            }
        }, parseAccountName(userEmail));

    }


    private void setDateString(String newDate) {
        dateString = newDate;
    }

    private void setTabTransitions() {
        // Set listener for switching to home tab
        findViewById(R.id.home).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyCalendarActivity.this.setCurrentTab(TabType.HOME);

            }
        });

        // Set listener for switching to group tab
        findViewById(R.id.groups).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyCalendarActivity.this.setCurrentTab(TabType.GROUPS);
            }
        });

        // Set listener for switching to settings tab
        findViewById(R.id.settingsButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyCalendarActivity.this.setCurrentTab(TabType.SETTINGS);
            }
        });

    }


    public void setCurrentTab(TabType current) {

        if (currentTab == current) {
            return;
        }

        rootView.removeView(currentTabView);

        if (current == TabType.HOME) {
            rootView.addView(homeView);
            currentTabView = homeView;
            currentTab = TabType.HOME;

            calendarTab = (TableRow) findViewById(R.id.calendarTabView);
            calendarTab.setVisibility(View.VISIBLE);
        }
        else if (current == TabType.GROUPS) {
            rootView.addView(groupsView);
            currentTabView = groupsView;
            currentTab = TabType.GROUPS;
            removeCalendarTabs();
            setUpGroupTabInteraction();
        }
        else if (current == TabType.SETTINGS) {
            rootView.addView(settingsView);
            currentTabView = settingsView;
            currentTab = TabType.SETTINGS;
            removeCalendarTabs();

        }
    }

    private void inflateGroupListView() {
        ListView groupListView;
        GroupListAdapter groupListAdapter;

        groupList = (ArrayList<String>)DatabaseAccess.getUserGroups(DatabaseAccess.parseAccountName(userEmail));
        groupListView = (ListView) findViewById(R.id.groupList);
        groupListAdapter = new GroupListAdapter(groupList);
        groupListView.setAdapter(groupListAdapter);
        groupListAdapter.notifyDataSetChanged();

        groupListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
                String selectedGroup = groupList.get(position);
                startGroupInfoActivity(selectedGroup);

            }
        });

    }

    private void removeCalendarTabs() {

        calendarTab = (TableRow) findViewById(R.id.calendarTabView);
        calendarTab.setVisibility(View.INVISIBLE);
    }

    private void setUpGroupTabInteraction() {

        setUpNewGroupAbility();
        inflateGroupListView();

    }

    private void setUpNewGroupAbility() {

        Button createGroupButton;

        newGroupEdit = (EditText) findViewById(R.id.newGroup);
        final List<User> users = new ArrayList<>();
        users.add(new User(userDisplayName, userEmail));

        final List<String> userEmailList = new ArrayList<>();
        userEmailList.add(userEmail.split("@")[0]);

        newGroupEdit.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    if (event.getAction() == KeyEvent.ACTION_DOWN) {
                        createNewGroup(userEmailList);
                    }
                    return true;
                }
                return false;
            }
        });


        createGroupButton = (Button) findViewById(R.id.newGroupSubmit);

        createGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewGroup(userEmailList);
            }
        });

    }

    public void createNewGroup(List<String> userEmailList) {
        String groupName = newGroupEdit.getText().toString();
        if (groupName.length() != 0) {

            DatabaseAccess.createGroup(groupName, userEmailList);
            newGroupEdit.setText("");
        }
    }

    private void startGroupInfoActivity(String selectedGroup) {
        Intent i = new Intent(this, GroupInfoActivity.class);
        i.putExtra("groupName", selectedGroup);
        startActivity(i);
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

        TextView dayDateText;

        if (calendar == currentCalendar) {
            return;
        }

        tabRootView.removeView(currentView);

        if (calendar == CalendarType.MONTH) {
            tabRootView.addView(monthView);
            currentView = monthView;
            currentCalendar = CalendarType.MONTH;

        }
        else if (calendar == CalendarType.DAY) {
            tabRootView.addView(dayView);
            currentView = dayView;

            dayDateText = (TextView) findViewById(R.id.dayDate);
            dayDateText.setText(dateString);
            updateDayView();
            dayEventsList = (ListView) findViewById(R.id.eventList);

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
                List<CalendarEvent> calendarEvents = new ArrayList<>();
                String eventName;
                String eventDescription;
                String location;
                long startTime;
                long endTime;
                CalendarEvent thisCalendarEvent;
                String creator;
                for(Event thisItem : items) {
                    eventName = thisItem.getSummary();
                    eventDescription = thisItem.getDescription();
                    location = thisItem.getLocation();
                    if (thisItem.getStart().getDateTime() == null && thisItem.getEnd().getDateTime() == null) {
                        continue;
                    }
                    startTime = thisItem.getStart().getDateTime().getValue() + thisItem.getStart().getDateTime().getTimeZoneShift();
                    endTime = thisItem.getEnd().getDateTime().getValue() + thisItem.getStart().getDateTime().getTimeZoneShift();
                    creator = thisItem.getCreator().getEmail();
                    thisCalendarEvent = new CalendarEvent(eventName, eventDescription, location, startTime, endTime, creator);
                    calendarEvents.add(thisCalendarEvent);
                }
                FirebaseDatabase.getInstance().getReference().child("users")
                        .child(parseAccountName(credential.getSelectedAccountName()))
                        .child("events").setValue(calendarEvents);
            }
            catch (Exception e) {
                Log.getStackTraceString(e);
            }
            return null;
        }
    }
}
