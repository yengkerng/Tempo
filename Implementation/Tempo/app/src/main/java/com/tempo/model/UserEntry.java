package com.tempo.model;
import java.util.*;

/**
 * Created by andrewcofano on 3/15/17.
 */

public class UserEntry {
    private String text;
    private boolean check;

    public UserEntry(String text, boolean check) {
        this.text = text;
        this.check = check;
    }

    @Override
    public boolean equals(Object user2) {
        if (user2 != null) {
            if (user2.getClass() == UserEntry.class) {
                return this.text.equals(((UserEntry) user2).getText());
            } else {
                return false;
            }
        }
        else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return 0;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public String getText() {
        return text;
    }

}
