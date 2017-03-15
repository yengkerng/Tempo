package com.tempo.presenter;

import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.tempo.model.CalendarEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

class DatabaseAccess {

    static void createGroup(final String name, final List<String> members) {

        new DatabaseAccessTask(new DatabaseAccessCallback() {
            @Override
            public void call() throws DatabaseAccessException {

                FirebaseDatabase.getInstance().getReference().child("groups").child(name)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                if (dataSnapshot.getChildrenCount() > 0) {
                                    return;
                                }

                                DatabaseReference db = FirebaseDatabase.getInstance().getReference();
                                DatabaseReference newGroup = db.child("groups").child(name);

                                for (String member : members) {
                                    DatabaseReference groups = db.child("users").child(member).child("groups");
                                    groups.push().setValue(name);
                                    newGroup.push().setValue(member);
                                }

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

            }
        }).execute();

    }

    static void addUserToGroup(final String userName, final String groupName) {

        new DatabaseAccessTask(new DatabaseAccessCallback() {
            @Override
            public void call() throws DatabaseAccessException {

                DatabaseReference db = FirebaseDatabase.getInstance().getReference();
                DatabaseReference groupRef = db.child("groups").child(groupName);
                DatabaseReference userRef = db.child("users").child(userName).child("groups");

                groupRef.push().setValue(userName);
                userRef.push().setValue(groupName);
            }
        }).execute();

    }

    static void deleteUserFromGroup(final String userName, final String groupName) {

        new DatabaseAccessTask(new DatabaseAccessCallback() {
            @Override
            public void call() throws DatabaseAccessException {

                final DatabaseReference db = FirebaseDatabase.getInstance().getReference();

                db.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        GenericTypeIndicator<HashMap<String, String>> t1 = new GenericTypeIndicator<HashMap<String, String>>() {};
                        HashMap<String, String> groupRef = dataSnapshot.child("groups").child(groupName).getValue(t1);
                        HashMap<String, String> userRef = dataSnapshot.child("users").child(userName).child("groups").getValue(t1);
                        if (groupRef != null && userRef != null) {
                            while(groupRef.values().remove(userName));
                            while(userRef.values().remove(groupName));
                            db.child("groups").child(groupName).setValue(groupRef);
                            db.child("users").child(userName).child("groups").setValue(userRef);
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


    static List<String> getUserGroups(final String userName) {
        final List<String> groupList = new ArrayList<>();

        new DatabaseAccessTask(new DatabaseAccessCallback() {
            @Override
            public void call() throws DatabaseAccessException {

                final DatabaseReference db = FirebaseDatabase.getInstance().getReference();
                final DatabaseReference userGroupsRef = db.child("users").child(userName).child("groups");

                userGroupsRef.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                        String group = dataSnapshot.getValue(String.class);

                        if (group != null && !groupList.contains(group)) { groupList.add(group); }
                     }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

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
                        GenericTypeIndicator<HashMap<String, String>> t = new GenericTypeIndicator<HashMap<String, String>>() {};
                        HashMap<String,String> snapShotMembers = dataSnapshot.getValue(t);
                        for(String member : snapShotMembers.values()) {
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

    static void getUserEventListWithCallback(@NonNull final SimpleCallback<List<CalendarEvent>> finishedCallback,final String userName, long startTime, long endTime) {
        final List<CalendarEvent> userEvents = new ArrayList<CalendarEvent>();
        new DatabaseAccessTask(new DatabaseAccessCallback() {
            @Override
            public void call() throws DatabaseAccessException {

                DatabaseReference db = FirebaseDatabase.getInstance().getReference();
                DatabaseReference member = db.child("users").child(userName).child("events");

                member.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                        CalendarEvent data = dataSnapshot.getValue(CalendarEvent.class);
                        if (data != null && !userEvents.contains(data)) {
                            userEvents.add(data);
                        }
                        // execute callback function
                        finishedCallback.callback(userEvents);
                    }


                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

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

                        GenericTypeIndicator<HashMap<String, String>> t = new GenericTypeIndicator<HashMap<String, String>>() {};
                        HashMap<String,String> members = dataSnapshot.child("groups").child(groupName).getValue(t);

                        for (String user : members.values()) {
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
