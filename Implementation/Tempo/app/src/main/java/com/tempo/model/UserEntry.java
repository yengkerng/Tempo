package com.tempo.model;

/**
 * Created by andrewcofano on 3/15/17.
 */

public class UserEntry {
    public String text;
    public boolean check;

    public UserEntry(String text, boolean check) {
        this.text = text;
        this.check = check;
    }

    public boolean equals(Object user2) {
        if (user2.getClass() == UserEntry.class) {
            return (this.text == ((UserEntry)user2).text);
        }
        else {
            return false;
        }
    }
}
