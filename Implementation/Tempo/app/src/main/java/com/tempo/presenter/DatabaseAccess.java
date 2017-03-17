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
import java.util.Set;

class DatabaseAccess {

    private static String group = "groups";
    private static String user = "users";

    static void createGroup(final String name, final List<String> members) {

        new DatabaseAccessTask(new DatabaseAccessCallback() {
            @Override
            public void call() throws DatabaseAccessException {

                FirebaseDatabase.getInstance().getReference().child(group).child(name)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                if (dataSnapshot.getChildrenCount() > 0) {
                                    return;
                                }

                                DatabaseReference db = FirebaseDatabase.getInstance().getReference();
                                DatabaseReference newGroup = db.child(group).child(name);

                                for (String member : members) {
                                    DatabaseReference groups = db.child(user).child(member).child(group);
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
                DatabaseReference groupRef = db.child(group).child(groupName);
                DatabaseReference userRef = db.child(user).child(userName).child(group);

                groupRef.push().setValue(userName);
                userRef.push().setValue(groupName);
            }
        }).execute();

    }

    static void addEventToGroup(final String groupName, final CalendarEvent event, final SimpleCallback<Void> callback) {

        new DatabaseAccessTask(new DatabaseAccessCallback() {
            @Override
            public void call() throws DatabaseAccessException {

                final DatabaseReference db = FirebaseDatabase.getInstance().getReference();

                db.addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        GenericTypeIndicator<HashMap<String, String>> type = new GenericTypeIndicator<HashMap<String, String>>() {};
                        HashMap<String, String> groupRef = dataSnapshot.child(group).child(groupName).getValue(type);

                        for (String groupMember : groupRef.values()) {
                            GenericTypeIndicator<List<CalendarEvent>> type2 = new GenericTypeIndicator<List<CalendarEvent>>() {};
                            List<CalendarEvent> memberEvents = dataSnapshot.child(user).child(groupMember).child("events").getValue(type2);
                            memberEvents.add(event);
                            db.child(user).child(groupMember).child("events").setValue(memberEvents);
                        }

                        callback.callback(null);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }

                });

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
                        HashMap<String, String> groupRef = dataSnapshot.child(group).child(groupName).getValue(t1);
                        HashMap<String, String> userRef = dataSnapshot.child(user).child(userName).child(group).getValue(t1);
                        if (groupRef != null && userRef != null) {
                            db.child(group).child(groupName).setValue(groupRef);
                            db.child(user).child(userName).child(group).setValue(userRef);
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
                final DatabaseReference userGroupsRef = db.child(user).child(userName).child(group);

                userGroupsRef.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                        String group = dataSnapshot.getValue(String.class);

                        if (group != null && !groupList.contains(group)) {
                            groupList.add(group);
                        }
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
                        HashMap<String, String> groupMembers = dataSnapshot.child(group).child(name).getValue(t1);

                        if (groupMembers != null) {
                            for (String member : groupMembers.values()) {
                                GenericTypeIndicator<HashMap<String, String>> t2 = new GenericTypeIndicator<HashMap<String, String>>() {
                                };
                                HashMap<String, String> userGroups = dataSnapshot.child(user).child(member).child(group).getValue(t2);
                                while (userGroups.values().remove(name)) ;
                                db.child(user).child(member).child(group).setValue(userGroups);
                            }

                            db.child(group).child(name).removeValue();
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




    static void getGroupMembersWithCallback(@NonNull final SimpleCallback<List<String>> finishedCallback, final String groupName) {
        final List<String> members = new ArrayList<String>();
        new DatabaseAccessTask(new DatabaseAccessCallback() {
            @Override
            public void call() throws DatabaseAccessException {

                DatabaseReference db = FirebaseDatabase.getInstance().getReference();
                DatabaseReference theGroup = db.child(group).child(groupName);

                theGroup.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                        //GenericTypeIndicator<HashMap<String, String>> t = new GenericTypeIndicator<HashMap<String, String>>() {};
                        String snapShotMember = dataSnapshot.getValue(String.class);
                        //for(String member : snapShotMembers.values()) {
                        if (!members.contains(snapShotMember)) {
                            members.add(snapShotMember);
                        }
                        //}
                        // execute callback function
                        finishedCallback.callback(members);
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {
                        String snapShotMember = dataSnapshot.getValue(String.class);
                        members.remove(snapShotMember);
                        finishedCallback.callback(members);
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

    static void getUsersNotInGroup(@NonNull final SimpleCallback<List<String>> finishedCallback, final String groupName) {
        final List<String> users = new ArrayList<String>();
        new DatabaseAccessTask(new DatabaseAccessCallback() {
            @Override
            public void call() throws DatabaseAccessException {

                DatabaseReference db = FirebaseDatabase.getInstance().getReference();

                db.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        GenericTypeIndicator<HashMap<String, String>> t = new GenericTypeIndicator<HashMap<String, String>>() {};
                        HashMap<String,String> snapShotGroups = dataSnapshot.child(group).child(groupName).getValue(t);
                        GenericTypeIndicator<HashMap<String, Object>> t2 = new GenericTypeIndicator<HashMap<String, Object>>() {};
                        Set<String> snapShotUsers = dataSnapshot.child(user).getValue(t2).keySet();
                        for (String userName: snapShotGroups.values()) {
                            snapShotUsers.remove(userName);
                        }
                        for (String user: snapShotUsers) {
                            users.add(user);
                        }
                        // execute callback function
                        finishedCallback.callback(users);
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
                DatabaseReference member = db.child(user).child(userName).child("events");

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
                        HashMap<String,String> members = dataSnapshot.child(group).child(groupName).getValue(t);

                        for (String user : members.values()) {
                            GenericTypeIndicator<ArrayList<CalendarEvent>> t2 = new GenericTypeIndicator<ArrayList<CalendarEvent>>() {};
                            List<CalendarEvent> userCalendar = dataSnapshot.child(user).child(user).child("events").getValue(t2);
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
