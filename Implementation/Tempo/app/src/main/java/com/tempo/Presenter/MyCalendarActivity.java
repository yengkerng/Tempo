package com.tempo.Presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import android.graphics.PorterDuff.Mode;

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
import com.tempo.Model.Group;
import com.tempo.Model.User;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class MyCalendarActivity extends Activity {


    public enum CalendarType {
        MONTH, WEEK, DAY
    }

    public enum TabType {
        HOME, GROUPS, SETTINGS
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
    private ViewGroup tabRootView;
    private View monthView, weekView, dayView, currentView, currentTabView, homeView, groupsView;
    private ListView dayEventsList, groupListView;
    private EventListAdapter eventListAdapter;
    private GroupListAdapter groupListAdapter;
    private CalendarType currentCalendar;
    private TabType currentTab;
    private CalendarView monthlyCalendar;
    private WeekView weeklyCalendar;
    private TextView dayDateText;



    private EventDateTime currentDate = null;
    private long dateInMillis;
    private String dateString;

    private ArrayList<CalendarEvent> currentDayEventList;
    private ArrayList<Group> groupList;

    private static Context context;

    private EditText newGroupEdit;
    private Button createGroupButton;
    private String userEmail, userDisplayName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Bundle userInfo = getIntent().getExtras();
        userEmail = userInfo.getString("userEmail");
        userDisplayName = userInfo.getString("username");
        MyCalendarActivity.context = getApplicationContext();
        setContentView(R.layout.activity_my_calendar);

        renderTabs();
        renderCalendar();
        displayEvents();
        setTabTransitions();
        setCalendarTransitions();

        new SyncCalendarTask(Account.getInstance().googleCred).execute();
        /*
        DatabaseAccess.createGroup("MyGroupppp", Arrays.asList(new String[] { "14bmkelley", "bitsbots3812", "jessieemail" }));

        SimpleCallback<List<String>> cb = new SimpleCallback<List<String>>() {
            @Override
            public void callback(List<String> data1) {
                if (data1 != null) {
                    for (String member : data1) {
                        System.out.println("THIS MEMBER!!: " + member);
                    }
                } else {
                    System.out.println("!!!!!!!!!!!!!THE MEMBER ARRAY IS NULL!!!!!!!!!!!!");
                }
            }
        };
        DatabaseAccess.getGroupMembersWithCallback(cb, "Brandon's Group");


        SimpleCallback<List<CalendarEvent>> cb2 = new SimpleCallback<List<CalendarEvent>>() {
            @Override
            public void callback(List<CalendarEvent> data2) {
                if (data2 != null) {
                    for (CalendarEvent thisEvent : data2) {
                        System.out.println("THIS EVENT: " + thisEvent);
                    }
                } else {
                    System.out.println("!!!!!!!!!!!!!THE EVENT ARRAY IS NULL!!!!!!!!!!!!");
                }
            }
        };
        DatabaseAccess.getUserEventListWithCallback(cb2, "14bmkelley");

        SimpleCallback<List<List<CalendarEvent>>> cb3 = new SimpleCallback<List<List<CalendarEvent>>>() {
            @Override
            public void callback(List<List<CalendarEvent>> data3) {
                if (data3 != null) {
                    int i = 0;
                    for (List<CalendarEvent> calendar : data3) {
                        System.out.println("THIS IS CALENDAR #" + i);
                        i += 1;
                        for (CalendarEvent thisEvent : calendar) {
                            System.out.println("    THIS EVENT IN THE CALENDAR: " + thisEvent);
                        }
                        System.out.print("\n");
                    }
                } else {
                    System.out.println("!!!!!!!!!!!!!THE GROUP'S EVENT ARRAYS ARE NULL!!!!!!!!!!!!");
                }
            }
        };
        DatabaseAccess.getAllMembersCalendarEventsWithCallback(cb3, "Brandon's Group");
*/
    }


    public static Context getAppContext() {
        return MyCalendarActivity.context;
    }

    private void renderTabs() {
        LayoutInflater inflater = getLayoutInflater();

        rootView  = (ViewGroup) findViewById(R.id.tabRoot);
        homeView = inflater.inflate(R.layout.home_tab, null);
        currentTabView = homeView;
        currentTab = TabType.HOME;
        rootView.addView(homeView);

        //monthlyCalendar = (CalendarView) findViewById(R.id.monthlyCalendar);


        groupsView   = inflater.inflate(R.layout.group_tab, null);

    }

    private void renderCalendar() {

        LayoutInflater inflater = getLayoutInflater();

        //rootView  = (ViewGroup) findViewById(R.id.tabRoot);
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
            }
        });

        dayView   = inflater.inflate(R.layout.calendar_day, null);

    }

    private void setDateString(String newDate) {
        dateString = newDate;
    }


    private void displayEvents() {

    }


    private void setTabTransitions() {
        // Set listener for switching to home tab
        findViewById(R.id.home).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyCalendarActivity.this.setCurrentTab(TabType.HOME);
                Resources res = getAppContext().getResources();
                final ImageView image = (ImageView) findViewById(R.id.home);
                final int color = res.getColor(R.color.currentTabWhite);
                image.setColorFilter(color, Mode.SRC_ATOP);

            }
        });

        // Set listener for switching to group tab
        findViewById(R.id.groups).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyCalendarActivity.this.setCurrentTab(TabType.GROUPS);
                Resources res = getAppContext().getResources();
                final ImageButton image = (ImageButton) findViewById(R.id.home);
                //final int color = res.getColor(R.color.currentTabWhite);
                image.setColorFilter(Color.argb(255, 255, 255, 255));
            }
        });
    }


    public void setCurrentTab(TabType current) {

        ArrayList<User> tempArray;

        if (currentTab == current) {
            return;
        }

        rootView.removeView(currentTabView);

        switch (current) {

            case HOME:
                rootView.addView(homeView);
                currentTabView = homeView;
                currentTab = TabType.HOME;
                break;

            // case WEEK:
                /*rootView.addView(weekView);
                currentView = weekView;
                currentCalendar = CalendarType.WEEK;
                break;*/

            case GROUPS:
                rootView.addView(groupsView);
                currentTabView = groupsView;
                currentTab = TabType.GROUPS;


                groupListView = (ListView) findViewById(R.id.groupList);
                groupList = new ArrayList<>();
                tempArray = new ArrayList<>();
                tempArray.add(new User("Alex BOId", "alex@teamlead.com"));
                tempArray.add(new User("Alex BOId", "alex@teamlead.com"));
                tempArray.add(new User("Alex BOId", "alex@teamlead.com"));
                tempArray.add(new User("Alex BOId", "alex@teamlead.com"));

                groupList.add(new Group("Falessi's Italianos", new User("Alex BOId", "alex@teamlead.com"), tempArray));
                groupList.add(new Group("Other team", new User("Alex BOId", "alex@teamlead.com"), tempArray));
                groupList.add(new Group("Other other team", new User("Alex BOId", "alex@teamlead.com"), tempArray));


                groupListAdapter = new GroupListAdapter(this, groupList);
                groupListView.setAdapter(groupListAdapter);

                groupListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
                        Group selectedGroup = groupList.get(position);
                        startGroupInfoActivity(selectedGroup);

                    }
                });


                setUpGroupTabInteraction();

        }
    }

    private void setUpGroupTabInteraction() {

        setUpNewGroupAbility();
    }

    private void setUpNewGroupAbility() {

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
                        createNewGroup(users, userEmailList);
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
                createNewGroup(users, userEmailList);
            }
        });

    }

    public void createNewGroup(List<User> users, List<String> userEmailList) {
        String groupName = newGroupEdit.getText().toString();
        if (groupName.length() != 0) {

            groupList.add(new Group(groupName, new User(userDisplayName, userEmail), (ArrayList<User>) users));
            //groupListAdapter.setmDataSource(groupList);
            DatabaseAccess.createGroup(groupName, userEmailList);
            newGroupEdit.setText("");
        }
    }

    private void startGroupInfoActivity(Group selectedGroup) {
        Intent i = new Intent(this, GroupInfoActivity.class);
        i.putExtra("groupName", selectedGroup.getName());
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

        if (calendar == currentCalendar) {
            return;
        }

        tabRootView.removeView(currentView);

        switch (calendar) {

            case MONTH:
                tabRootView.addView(monthView);
                currentView = monthView;
                currentCalendar = CalendarType.MONTH;
                break;

           // case WEEK:
                /*rootView.addView(weekView);
                currentView = weekView;
                currentCalendar = CalendarType.WEEK;
                break;*/

            case DAY:
                tabRootView.addView(dayView);
                currentView = dayView;

                dayDateText = (TextView) findViewById(R.id.dayDate);
                dayDateText.setText(dateString);
                dayEventsList = (ListView) findViewById(R.id.eventList);

                final MyCalendarActivity that = this;

                DatabaseAccess.getUserEventListWithCallback(new SimpleCallback<List<CalendarEvent>>() {
                    @Override
                    public void callback(List<CalendarEvent> data) {
                        eventListAdapter = new EventListAdapter(that, data);
                        if (dayEventsList != null) {
                            dayEventsList.setAdapter(eventListAdapter);
                        }
                    }
                }, DatabaseAccess.parseAccountName(userEmail));

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
                List<CalendarEvent> calendarEvents = new ArrayList<CalendarEvent>();
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
                    startTime = thisItem.getStart().getDateTime().getValue() + thisItem.getStart().getDateTime().getTimeZoneShift();
                    endTime = thisItem.getEnd().getDateTime().getValue() + thisItem.getStart().getDateTime().getTimeZoneShift();
                    creator = thisItem.getCreator().getEmail();
                    thisCalendarEvent = new CalendarEvent(eventName, eventDescription, location, startTime, endTime, creator);
                    calendarEvents.add(thisCalendarEvent);
                }
                FirebaseDatabase.getInstance().getReference().child("users")
                        .child(DatabaseAccess.parseAccountName(credential.getSelectedAccountName()))
                        .child("events").setValue(calendarEvents);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
