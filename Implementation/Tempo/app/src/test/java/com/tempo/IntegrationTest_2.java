package com.tempo;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.EventDateTime;
import com.tempo.Model.CalendarEvent;
import com.tempo.Model.Group;
import com.tempo.Model.User;

import org.junit.Test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Brandon on 3/5/17.
 * This will test more integration of the user and the group classes
 */


public class IntegrationTest_2 {
    @Test
    public void TestGroupUserIntegrationGetMembers() {
        User member1 = new User("Brandon", "bmkel@calpoly.edu");
        User member2 = new User("Falessi", "dfalessi@calpoly.edu");
        ArrayList<User> members = new ArrayList<>();
        members.add(member1);
        members.add(member2);
        Group testGroup = new Group("Test Group", member1, members, null);
        assertEquals(member1, testGroup.getMembers().get(0));
    }

    @Test
    public void TestGroupUserIntegrationLeaveGroup() {
        User member1 = new User("Brandon", "bmkel@calpoly.edu");
        User member2 = new User("Falessi", "dfalessi@calpoly.edu");
        ArrayList<User> members = new ArrayList<>();
        members.add(member1);
        members.add(member2);
        Group testGroup = new Group("Test Group", member1, members, null);
        member1.createNewGroup(testGroup);
        assertEquals(true, member1.getGroups().contains(testGroup));
        testGroup.getMembers().get(0).leaveGroup(testGroup.getName());
        assertEquals(false, member1.getGroups().contains(testGroup));
    }
}