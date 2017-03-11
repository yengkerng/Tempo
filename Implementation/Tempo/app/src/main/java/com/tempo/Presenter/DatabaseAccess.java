package com.tempo.Presenter;

import android.os.AsyncTask;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

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
