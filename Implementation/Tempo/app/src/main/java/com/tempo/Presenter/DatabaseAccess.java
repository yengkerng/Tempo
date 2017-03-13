package com.tempo.Presenter;

import android.os.AsyncTask;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
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

    static List<String> getGroupMembers(final String groupName) {

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
                            System.out.println("FROM DATABASE ACCESS: member" + member);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // ...
                    }
                });
            }

        }).execute();
        return members;
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
