package com.tempo.model;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

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

    @Override
    public boolean equals(Object user2) {
        if (user2.getClass() == UserEntry.class) {
                return this.text == ((UserEntry) user2).text;
        }
        else {
            return false;
        }
    }
}
