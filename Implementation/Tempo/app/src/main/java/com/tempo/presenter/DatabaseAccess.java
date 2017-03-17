package com.tempo.presenter;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

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
    private static String events = "events";

    static void createGroup(final String name, final List<String> members) {

        new DatabaseAccessTask(new DatabaseAccessCallback() {
            @Override
            public void call() throws DatabaseAccessException {
                FirebaseDatabase.getInstance().getReference().child(group).child(name).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.getChildrenCount() > 0)
                                    return;
                                DatabaseReference db = FirebaseDatabase.getInstance().getReference();
                                DatabaseReference newGroup = FirebaseDatabase.getInstance().getReference().child(group).child(name);
                                for (String member : members) {
                                    (db.child(user).child(member).child(group)).push().setValue(name);
                                    newGroup.push().setValue(member);
                                }
                            }
                            @Override public void onCancelled(DatabaseError databaseError) { /* ...*/ }
                        });
            }
        }).execute();

    }

    static void addUserToGroup(final String userName, final String groupName) {

        new DatabaseAccessTask(new DatabaseAccessCallback() {
            @Override public void call() throws DatabaseAccessException {

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
            @Override public void call() throws DatabaseAccessException {
                final DatabaseReference db = FirebaseDatabase.getInstance().getReference();
                db.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override public void onDataChange(DataSnapshot dataSnapshot) {
                        HashMap<String, String> groupRef = dataSnapshot.child(group).child(groupName).getValue(new GenericTypeIndicator<HashMap<String, String>>() {});
                        for (String groupMember : groupRef.values()) {
                            List<CalendarEvent> memberEvents = dataSnapshot.child(user).child(groupMember).child(events).getValue(new GenericTypeIndicator<List<CalendarEvent>>() {});
                            memberEvents.add(event);
                            db.child(user).child(groupMember).child(events).setValue(memberEvents);
                        }
                        callback.callback(null);
                    }
                    @Override public void onCancelled(DatabaseError databaseError) {/*..*/}
                });
            }
        }).execute();
    }

    static void deleteUserFromGroup(final String userName, final String groupName) {

        new DatabaseAccessTask(new DatabaseAccessCallback() {
            @Override public void call() throws DatabaseAccessException {
                final DatabaseReference db = FirebaseDatabase.getInstance().getReference();
                db.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override public void onDataChange(DataSnapshot dataSnapshot) {
                        HashMap<String, String> groupRef = dataSnapshot.child(group).child(groupName).getValue(new GenericTypeIndicator<HashMap<String, String>>() {});
                        HashMap<String, String> userRef = dataSnapshot.child(user).child(userName).child(group).getValue(new GenericTypeIndicator<HashMap<String, String>>() {});
                        if (groupRef != null && userRef != null) {
                            groupRef.values().remove(userName);
                            userRef.values().remove(groupName);
                            db.child(group).child(groupName).setValue(groupRef);
                            db.child(user).child(userName).child(group).setValue(userRef);
                        }
                    }
                    @Override public void onCancelled(DatabaseError databaseError) { /*...*/}
                });}
        }).execute();
    }


    static List<String> getUserGroups(final String userName) {
        final List<String> groupList = new ArrayList<>();

        new DatabaseAccessTask(new DatabaseAccessCallback() {
            @Override public void call() throws DatabaseAccessException {
                final DatabaseReference userGroupsRef = (FirebaseDatabase.getInstance().getReference()).child(user).child(userName).child(group);
                userGroupsRef.addChildEventListener(new ChildEventListener() {
                    @Override public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                        String groupSnapshot = dataSnapshot.getValue(String.class);
                        if (groupSnapshot != null && !groupList.contains(groupSnapshot))
                            groupList.add(groupSnapshot);
                    }
                    @Override public void onChildChanged(DataSnapshot dataSnapshot, String s) { /*...*/}
                    @Override public void onChildRemoved(DataSnapshot dataSnapshot) {/*...*/}
                    @Override public void onChildMoved(DataSnapshot dataSnapshot, String s) {/*...*/}
                    @Override public void onCancelled(DatabaseError databaseError) {/*...*/}
                });}}).execute();
        return groupList;
    }

    static void getGroupMembersWithCallback(@NonNull final SimpleCallback<List<String>> finishedCallback, final String groupName) {
        final List<String> members = new ArrayList<>();
        new DatabaseAccessTask(new DatabaseAccessCallback() {
            @Override public void call() throws DatabaseAccessException {
                DatabaseReference theGroup = FirebaseDatabase.getInstance().getReference().child(group).child(groupName);
                theGroup.addChildEventListener(new ChildEventListener() {
                    @Override public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                        if (!members.contains(dataSnapshot.getValue(String.class)))
                            members.add(dataSnapshot.getValue(String.class));
                        finishedCallback.callback(members);
                    }
                    @Override public void onChildChanged(DataSnapshot dataSnapshot, String s) {/*...*/}
                    @Override public void onChildRemoved(DataSnapshot dataSnapshot) {
                        members.remove(dataSnapshot.getValue(String.class));
                        finishedCallback.callback(members);
                    }
                    @Override public void onChildMoved(DataSnapshot dataSnapshot, String s) {/*...*/}
                    @Override public void onCancelled(DatabaseError databaseError) {/*...*/}
                });}}).execute();
    }

    static void getUsersNotInGroup(@NonNull final SimpleCallback<List<String>> finishedCallback, final String groupName) {
        final List<String> users = new ArrayList<>();
        new DatabaseAccessTask(new DatabaseAccessCallback() {
            @Override public void call() throws DatabaseAccessException {
                DatabaseReference db = FirebaseDatabase.getInstance().getReference();
                db.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override public void onDataChange(DataSnapshot dataSnapshot) {
                        HashMap<String,String> snapShotGroups = dataSnapshot.child(group).child(groupName).getValue(new GenericTypeIndicator<HashMap<String, String>>() {});
                        Set<String> snapShotUsers = dataSnapshot.child(user).getValue(new GenericTypeIndicator<HashMap<String, Object>>() {}).keySet();
                        for (String userName: snapShotGroups.values()) {
                            snapShotUsers.remove(userName);
                        }
                        for (String member: snapShotUsers) {
                            users.add(member);
                        }
                        finishedCallback.callback(users);
                    }
                    @Override public void onCancelled(DatabaseError databaseError) {/*...*/}
                });
            }
        }).execute();
    }

    static void getUserEventListWithCallback(@NonNull final SimpleCallback<List<CalendarEvent>> finishedCallback,final String userName) {
        final List<CalendarEvent> userEvents = new ArrayList<>();
        new DatabaseAccessTask(new DatabaseAccessCallback() {
            @Override public void call() throws DatabaseAccessException {
                DatabaseReference db = FirebaseDatabase.getInstance().getReference();
                DatabaseReference member = db.child(user).child(userName).child(events);
                member.addChildEventListener(new ChildEventListener() {
                    @Override public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                        CalendarEvent data = dataSnapshot.getValue(CalendarEvent.class);
                        if (data != null && !userEvents.contains(data))
                            userEvents.add(data);
                        finishedCallback.callback(userEvents);
                    }
                    @Override public void onChildChanged(DataSnapshot dataSnapshot, String s) {/*...*/}
                    @Override public void onChildRemoved(DataSnapshot dataSnapshot) {/*...*/}
                    @Override public void onChildMoved(DataSnapshot dataSnapshot, String s) {/*...*/}
                    @Override public void onCancelled(DatabaseError databaseError) {/*...*/}
                });
            }
        }).execute();
    }

    static void getAllMembersCalendarEventsWithCallback(@NonNull final SimpleCallback<List<List<CalendarEvent>>> finishedCallback,final String groupName) {
        final List<List<CalendarEvent>> allEvents = new ArrayList<>();
        new DatabaseAccessTask(new DatabaseAccessCallback() {
            @Override public void call() throws DatabaseAccessException {
                DatabaseReference db = FirebaseDatabase.getInstance().getReference();
                db.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override public void onDataChange(DataSnapshot dataSnapshot) {
                        GenericTypeIndicator<HashMap<String, String>> t = new GenericTypeIndicator<HashMap<String, String>>() {};
                        HashMap<String,String> members = dataSnapshot.child(group).child(groupName).getValue(t);
                        for (String specificUser : members.values()) {
                            GenericTypeIndicator<ArrayList<CalendarEvent>> t2 = new GenericTypeIndicator<ArrayList<CalendarEvent>>() {};
                            List<CalendarEvent> userCalendar = dataSnapshot.child(user).child(specificUser).child(events).getValue(t2);
                            allEvents.add(userCalendar);
                        }
                        finishedCallback.callback(allEvents);
                    }
                    @Override public void onCancelled(DatabaseError databaseError) {/*...*/}
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

        @Override protected Void doInBackground(Void... voids) {
            try {
                inBackgroundThread.call();
            }
            catch (DatabaseAccessException e) {
                Log.getStackTraceString(e);
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
