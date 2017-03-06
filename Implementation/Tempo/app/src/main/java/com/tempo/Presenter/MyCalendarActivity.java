package com.tempo.Presenter;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tempo.Model.User;

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

        //new PutItemTask(FirebaseDatabase.getInstance().getReference()).execute();

    }

    private void renderCalendar() {

        LayoutInflater inflater = getLayoutInflater();

        rootView  = (ViewGroup) findViewById(R.id.calendarRoot);

        monthView = inflater.inflate(R.layout.calendar_month, null);
        currentView = monthView;
        currentCalendar = CalendarType.MONTH;
        rootView.addView(currentView);

        weekView  = inflater.inflate(R.layout.calendar_week, null);
        dayView   = inflater.inflate(R.layout.calendar_day, null);

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

    private class PutItemTask extends AsyncTask<Void, Void, Void> {

        private DatabaseReference db;

        PutItemTask(DatabaseReference db) {
            this.db = db;
        }

        @Override
        protected Void doInBackground(Void... voids) {

            User user = new User("Brandon", "14bmkelley@gmail.com");

            try {
                db.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user);
            }
            catch (Exception e) {
                e.printStackTrace();
            }

            try {
                db.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .addValueEventListener(new ValueEventListener() {

                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                System.out.println(dataSnapshot.getValue(User.class).getUserName());
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }

                        });
            }
            catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

    }

}
