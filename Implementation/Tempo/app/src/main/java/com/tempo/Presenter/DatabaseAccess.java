package com.tempo.Presenter;

import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.google.api.services.calendar.model.Event;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.tempo.Model.CalendarEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class DatabaseAccess {

    static void createGroup(final String name, final List<String> members) {

        new DatabaseAccessTask(new DatabaseAccessCallback() {
            @Override
            public void call() throws DatabaseAccessException {

                DatabaseReference db = FirebaseDatabase.getInstance().getReference();
                DatabaseReference newGroup = db.child("groups").child(name);

                for (String member : members) {
                    DatabaseReference groups = db.child("users").child(member).child("groups");
                    groups.push().setValue(name);
                    newGroup.push().setValue(member);
                }

            }
        }).execute();

    }

    static List<String> getUserGroups(final String userName) {
        final List<String> groupList = new ArrayList<>();

        new DatabaseAccessTask(new DatabaseAccessCallback() {
            @Override
            public void call() throws DatabaseAccessException {

                final DatabaseReference db = FirebaseDatabase.getInstance().getReference();
                final DatabaseReference userGroupsRef = db.child("users").child(userName).child("groups");

                userGroupsRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        GenericTypeIndicator<HashMap<String, String>> t1 = new GenericTypeIndicator<HashMap<String, String>>() {};
                        HashMap<String, String> groups = dataSnapshot.getValue(t1);

                        if (groups != null) { groupList.addAll(groups.values()); }
                        for (String group : groupList) {
                            System.out.println("THIS IS FROM THE FUNCTION: " + group);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // ...
                    }
                });
            }
        }).execute();

        return groupList;
    }

    static void deleteGroup(final String name) {

        new DatabaseAccessTask(new DatabaseAccessCallback() {
            @Override
            public void call() throws DatabaseAccessException {

                final DatabaseReference db = FirebaseDatabase.getInstance().getReference();

                db.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        GenericTypeIndicator<HashMap<String, String>> t1 = new GenericTypeIndicator<HashMap<String, String>>() {};
                        HashMap<String, String> groupMembers = dataSnapshot.child("groups").child(name).getValue(t1);

                        if (groupMembers != null) {
                            for (String member : groupMembers.values()) {
                                GenericTypeIndicator<HashMap<String, String>> t2 = new GenericTypeIndicator<HashMap<String, String>>() {
                                };
                                HashMap<String, String> userGroups = dataSnapshot.child("users").child(member).child("groups").getValue(t2);
                                while (userGroups.values().remove(name)) ;
                                db.child("users").child(member).child("groups").setValue(userGroups);
                            }

                            db.child("groups").child(name).removeValue();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // ...
                    }
                });
            }
        }).execute();

    }


    /*
  This was the method to get group members without a callback
     */

//    static List<String> getGroupMembers(final String groupName) {
//        final List<String> members = new ArrayList<String>();
//        new DatabaseAccessTask(new DatabaseAccessCallback() {
//            @Override
//            public void call() throws DatabaseAccessException {
//                DatabaseReference db = FirebaseDatabase.getInstance().getReference();
//                DatabaseReference theGroup = db.child("groups").child(groupName);
//                theGroup.addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        GenericTypeIndicator<ArrayList<String>> t = new GenericTypeIndicator<ArrayList<String>>() {};
//                        ArrayList<String> snapShotMembers = dataSnapshot.getValue(t);
//                        for(String member : snapShotMembers) {
//                            members.add(member);
//                            System.out.println("FROM DATABASE ACCESS: member" + member);
//
//                        }
//                    }
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//                        // ...
//                    }
//                });
//            }
//
//        }).execute();
//        return members;
//    }

    static void getGroupMembersWithCallback(@NonNull final SimpleCallback<List<String>> finishedCallback, final String groupName) {
        final List<String> members = new ArrayList<String>();
        new DatabaseAccessTask(new DatabaseAccessCallback() {
            @Override
            public void call() throws DatabaseAccessException {

                DatabaseReference db = FirebaseDatabase.getInstance().getReference();
                DatabaseReference theGroup = db.child("groups").child(groupName);

                theGroup.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        GenericTypeIndicator<ArrayList<String>> t = new GenericTypeIndicator<ArrayList<String>>() {};
                        ArrayList<String> snapShotMembers = dataSnapshot.getValue(t);
                        for(String member : snapShotMembers) {
                            members.add(member);
                        }
                        // execute callback function
                        finishedCallback.callback(members);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // ...
                    }
                });
            }

        }).execute();
    }

    static void getUserEventListWithCallback(@NonNull final SimpleCallback<List<CalendarEvent>> finishedCallback,final String userName) {
        final List<CalendarEvent> userEvents = new ArrayList<CalendarEvent>();
        new DatabaseAccessTask(new DatabaseAccessCallback() {
            @Override
            public void call() throws DatabaseAccessException {

                DatabaseReference db = FirebaseDatabase.getInstance().getReference();
                DatabaseReference member = db.child("users").child(userName).child("events");

                member.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        GenericTypeIndicator<ArrayList<CalendarEvent>> t = new GenericTypeIndicator<ArrayList<CalendarEvent>>() {};
                        ArrayList<CalendarEvent> snapShotEvents = dataSnapshot.getValue(t);
                        for(CalendarEvent thisEvent : snapShotEvents) {
                            userEvents.add(thisEvent);
                        }
                        // execute callback function
                        finishedCallback.callback(userEvents);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // ...
                    }
                });
            }

        }).execute();
    }

    static void getAllMembersCalendarEventsWithCallback(@NonNull final SimpleCallback<List<List<CalendarEvent>>> finishedCallback,final String groupName) {
        final List<List<CalendarEvent>> allEvents = new ArrayList<List<CalendarEvent>>();
        new DatabaseAccessTask(new DatabaseAccessCallback() {
            @Override
            public void call() throws DatabaseAccessException {

                DatabaseReference db = FirebaseDatabase.getInstance().getReference();

                db.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        GenericTypeIndicator<ArrayList<String>> t1 = new GenericTypeIndicator<ArrayList<String>>() {};
                        List<String> members = dataSnapshot.child("groups").child(groupName).getValue(t1);

                        for (String user : members) {
                            GenericTypeIndicator<ArrayList<CalendarEvent>> t2 = new GenericTypeIndicator<ArrayList<CalendarEvent>>() {};
                            List<CalendarEvent> userCalendar = dataSnapshot.child("users").child(user).child("events").getValue(t2);

                            allEvents.add(userCalendar);
                        }

                        // execute callback function
                        finishedCallback.callback(allEvents);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // ...
                    }
                });
            }

        }).execute();
    }


    /*
    This was the method to get user event list without a callback
     */
//    static List<CalendarEvent> getUserEventList(final String userName) {
//        final List<CalendarEvent> userEvents = new ArrayList<CalendarEvent>();
//        new DatabaseAccessTask(new DatabaseAccessCallback() {
//            @Override
//            public void call() throws DatabaseAccessException {
//                DatabaseReference db = FirebaseDatabase.getInstance().getReference();
//                DatabaseReference member = db.child("users").child(userName).child("events");
//                member.addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        GenericTypeIndicator<ArrayList<CalendarEvent>> t = new GenericTypeIndicator<ArrayList<CalendarEvent>>() {};
//                        ArrayList<CalendarEvent> snapShotEvents = dataSnapshot.getValue(t);
//                        for(CalendarEvent thisEvent : snapShotEvents) {
//                            userEvents.add(thisEvent);
//                            System.out.println("FROM DATABASE ACCESS: event" + thisEvent);
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//                        // ...
//                    }
//                });
//            }
//
//        }).execute();
//        return userEvents;
//    }


    static String parseAccountName(String accountNameString) {
        int atIndex = accountNameString.indexOf('@');
        return accountNameString.substring(0, atIndex)
                .replace("[\\.\\[\\]]", "-");
    }

    private static class DatabaseAccessTask extends AsyncTask<Void, Void, Void> {

        private DatabaseAccessCallback inBackgroundThread;

        DatabaseAccessTask(DatabaseAccessCallback inBackgroundThread) {
            this.inBackgroundThread = inBackgroundThread;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                inBackgroundThread.call();
            }
            catch (DatabaseAccessException e) {
                e.printStackTrace();
            }
            return null;
        }

    }

    private interface DatabaseAccessCallback {
        void call() throws DatabaseAccessException;
    }

    private static class DatabaseAccessException extends Exception {

        DatabaseAccessException(String message) {
            super("A database error occurred: " + message);
        }

    }

}
