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
 * Created by Jessie on 3/5/17.
 * This will test integration of the user and the group classes
 */


public class IntegrationTest_1 {

        @Test
        public void testGroupUserIntegrationName() {
            User member = new User("Jessie", "smithygirl@gmail.com");
            ArrayList<User> members = new ArrayList<User>();
            ArrayList<Group> groups = new ArrayList<Group>();
            members.add(member);
            Group testGroup = new Group("Study Group", member, null, null);
            groups.add(testGroup);
            member.createNewGroup(testGroup.getName());
            assertEquals(testGroup.getName(), member.getGroups().get(0).getName());
        }
        @Test
        public void testGroupUserIntegrationMembers() {
            User member = new User("Jessie", "smithygirl@gmail.com");
            ArrayList<User> members = new ArrayList<User>();
            members.add(member);
            Group testGroup = new Group("Study Group", member, null, null);
            member.createNewGroup(testGroup.getName());
            assertEquals(members, member.getGroups().get(0).getMembers());
        }

}

