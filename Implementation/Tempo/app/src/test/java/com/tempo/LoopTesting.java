package com.tempo;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.EventDateTime;
import com.tempo.model.CalendarEvent;
import com.tempo.model.Group;
import com.tempo.model.User;

import org.junit.Test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.*;
import com.tempo.presenter.MeetingTimeAlgorithm;

public class LoopTesting {

    @Test
    public void testGroupLoopNoBody() {
        User user1 = new User("Jessie", null);
        User user2 = new User("Alex", null);
        User user3 = new User("Brandon", null);
        User user4 = new User("Yeng", null);
        User user5 = new User("Costin", null);
        User user6 = new User("Andrew", null);
        List<User> members = new ArrayList<User>();
        members.add(user1);
        members.add(user2);
        members.add(user3);
        members.add(user4);
        members.add(user5);
        Group testGroup = new Group("Test Group", user1, members);
        assertEquals(false, testGroup.deleteUserFromGroup(user6));
    }
    @Test
    public void testGroupLoopOnce() {
        User user1 = new User("Jessie", null);
        User user2 = new User("Alex", null);
        User user3 = new User("Brandon", null);
        User user4 = new User("Yeng", null);
        User user5 = new User("Costin", null);
        User user6 = new User("Andrew", null);
        List<User> members = new ArrayList<User>();
        members.add(user1);
        members.add(user2);
        members.add(user3);
        members.add(user4);
        members.add(user5);
        members.add(user6);
        Group testGroup = new Group("Test Group", user1, members);
        assertEquals(true, testGroup.deleteUserFromGroup(user1));
    }
    @Test
    public void testGroupLoopTwice() {
        User user1 = new User("Jessie", null);
        User user2 = new User("Alex", null);
        User user3 = new User("Brandon", null);
        User user4 = new User("Yeng", null);
        User user5 = new User("Costin", null);
        User user6 = new User("Andrew", null);
        List<User> members = new ArrayList<User>();
        members.add(user1);
        members.add(user2);
        members.add(user3);
        members.add(user4);
        members.add(user5);
        members.add(user6);
        Group testGroup = new Group("Test Group", user1, members);
        assertEquals(true, testGroup.deleteUserFromGroup(user2));
    }
    @Test
    public void testGroupLoopTypicalNumber() {
        User user1 = new User("Jessie", null);
        User user2 = new User("Alex", null);
        User user3 = new User("Brandon", null);
        User user4 = new User("Yeng", null);
        User user5 = new User("Costin", null);
        User user6 = new User("Andrew", null);
        List<User> members = new ArrayList<User>();
        members.add(user1);
        members.add(user2);
        members.add(user3);
        members.add(user4);
        members.add(user5);
        members.add(user6);
        Group testGroup = new Group("Test Group", user1, members);
        assertEquals(true, testGroup.deleteUserFromGroup(user4));
    }
    @Test
    public void testGroupLoopUpperBound() {
        User user1 = new User("Jessie", null);
        User user2 = new User("Alex", null);
        User user3 = new User("Brandon", null);
        User user4 = new User("Yeng", null);
        User user5 = new User("Costin", null);
        User user6 = new User("Andrew", null);
        List<User> members = new ArrayList<User>();
        members.add(user1);
        members.add(user2);
        members.add(user3);
        members.add(user4);
        members.add(user5);
        members.add(user6);
        Group testGroup = new Group("Test Group", user1, members);
        assertEquals(true, testGroup.deleteUserFromGroup(user6));
    }

    @Test
    public void testUserLoopNoBody() {
        User user1 = new User("Jessie", null);
        Group testGroup = new Group("Group 1", null, null);
        Group testGroup2 = new Group("Group 2", null, null);
        Group testGroup3 = new Group("Group 3", null, null);
        Group testGroup4 = new Group("Group 4", null, null);
        assertEquals(false, user1.leaveGroup("Non Existent Group"));
    }

    @Test
    public void testUserLoopOnce() {
        User user1 = new User("Jessie", null);
        Group testGroup = new Group("Group 1", null, null);
        Group testGroup2 = new Group("Group 2", null, null);
        Group testGroup3 = new Group("Group 3", null, null);
        Group testGroup4 = new Group("Group 4", null, null);
        user1.createNewGroup(testGroup);
        user1.createNewGroup(testGroup2);
        user1.createNewGroup(testGroup3);
        user1.createNewGroup(testGroup4);
        assertEquals(true, user1.leaveGroup("Group 1"));
    }
    @Test
    public void testUserLoopTwice() {
        User user1 = new User("Jessie", null);
        Group testGroup = new Group("Group 1", null, null);
        Group testGroup2 = new Group("Group 2", null, null);
        Group testGroup3 = new Group("Group 3", null, null);
        Group testGroup4 = new Group("Group 4", null, null);
        user1.createNewGroup(testGroup);
        user1.createNewGroup(testGroup2);
        user1.createNewGroup(testGroup3);
        user1.createNewGroup(testGroup4);
        assertEquals(true, user1.leaveGroup("Group 2"));
    }
    @Test
    public void testUserLoopTypicalNumber() {
        User user1 = new User("Jessie", null);
        Group testGroup = new Group("Group 1", null, null);
        Group testGroup2 = new Group("Group 2", null, null);
        Group testGroup3 = new Group("Group 3", null, null);
        Group testGroup4 = new Group("Group 4", null, null);
        user1.createNewGroup(testGroup);
        user1.createNewGroup(testGroup2);
        user1.createNewGroup(testGroup3);
        user1.createNewGroup(testGroup4);
        assertEquals(true, user1.leaveGroup("Group 3"));
    }
    @Test
    public void testUserLoopUpperBound() {
        User user1 = new User("Jessie", null);
        Group testGroup = new Group("Group 1", null, null);
        Group testGroup2 = new Group("Group 2", null, null);
        Group testGroup3 = new Group("Group 3", null, null);
        Group testGroup4 = new Group("Group 4", null, null);
        user1.createNewGroup(testGroup);
        user1.createNewGroup(testGroup2);
        user1.createNewGroup(testGroup3);
        user1.createNewGroup(testGroup4);
        assertEquals(true, user1.leaveGroup("Group 4"));
    }

    @Test
    public void testInitializeUserLoopNoBody() {
        HashMap<Long, Integer> usersFree = new HashMap<Long, Integer>();
        assertEquals(usersFree, MeetingTimeAlgorithm.initializeUserFreeMap(1, 0, 0));
    }
    @Test
    public void testInitializeUserLoopOnce() {
        HashMap<Long, Integer> usersFree = new HashMap<Long, Integer>();
        usersFree.put(0l, 0);
        assertEquals(usersFree, MeetingTimeAlgorithm.initializeUserFreeMap(0, 0, 0));
    }
    @Test
    public void testInitializeUserLoopTwice() {
        HashMap<Long, Integer> usersFree = new HashMap<Long, Integer>();
        usersFree.put(0l, 0);
        usersFree.put(5 * 60 * 1000l, 0);
        assertEquals(usersFree, MeetingTimeAlgorithm.initializeUserFreeMap(0, 5 * 60 * 1000, 0));
    }
    @Test
    public void testInitializeUserLoopTypicalNumber() {
]        HashMap<Long, Integer> usersFree = new HashMap<Long, Integer>();
        usersFree.put(0l, 0);
        usersFree.put(5 * 60 * 1000l, 0);
        usersFree.put(5 * 60 * 1000 * 2l, 0);
        assertEquals(usersFree, MeetingTimeAlgorithm.initializeUserFreeMap(0, 5 * 60 * 1000 * 2, 0));
    }

    @Test
    public void testCalculateFreeTimesLoopNoBody() {
        List<List<CalendarEvent>> userEventsList = new ArrayList<>();
        MeetingTimeAlgorithm.calculateFreeTimes(userEventsList, 0, null);
    }
    @Test
    public void testCalculateFreeTimesLoopOnce() {
        List<CalendarEvent> userEvents = new ArrayList<CalendarEvent>();
        HashMap<Long, Integer> usersFree = new HashMap<Long, Integer>();
        HashMap<Long, Integer> tester = new HashMap<Long, Integer>();
        List<List<CalendarEvent>> userEventsList = new ArrayList<>();
        userEventsList.add(userEvents);
        MeetingTimeAlgorithm.calculateFreeTimes(userEventsList, 0, usersFree);
        assertEquals(tester, usersFree);
    }
    @Test
    public void testCalculateFreeTimesLoopTwice() {
        List<CalendarEvent> userEvents = new ArrayList<CalendarEvent>();
        List<CalendarEvent> userEvents2 = new ArrayList<CalendarEvent>();
        HashMap<Long, Integer> usersFree = new HashMap<Long, Integer>();
        HashMap<Long, Integer> tester = new HashMap<Long, Integer>();
        List<List<CalendarEvent>> userEventsList = new ArrayList<>();
        userEventsList.add(userEvents);
        userEventsList.add(userEvents2);
        MeetingTimeAlgorithm.calculateFreeTimes(userEventsList, 0, usersFree);
        assertEquals(tester, usersFree);
    }
    @Test
    public void testCalculateFreeTimesLoopTypicalNumber() {
        List<CalendarEvent> userEvents = new ArrayList<CalendarEvent>();
        List<CalendarEvent> userEvents2 = new ArrayList<CalendarEvent>();
        List<CalendarEvent> userEvents3 = new ArrayList<CalendarEvent>();
        HashMap<Long, Integer> usersFree = new HashMap<Long, Integer>();
        HashMap<Long, Integer> tester = new HashMap<Long, Integer>();
        List<List<CalendarEvent>> userEventsList = new ArrayList<>();
        userEventsList.add(userEvents);
        userEventsList.add(userEvents2);
        userEventsList.add(userEvents3);
        MeetingTimeAlgorithm.calculateFreeTimes(userEventsList, 0, usersFree);
        assertEquals(tester, usersFree);
    }

    @Test
    public void testGetUniqueTimesNoBody() {
        List<CalendarEvent> userEvents = new ArrayList<CalendarEvent>();
        List<CalendarEvent> userEvents2 = new ArrayList<CalendarEvent>();
        List<CalendarEvent> userEvents3 = new ArrayList<CalendarEvent>();
        HashMap<Long, Integer> usersFree = new HashMap<Long, Integer>();
        HashMap<Long, Integer> tester = new HashMap<Long, Integer>();
        List<List<CalendarEvent>> userEventsList = new ArrayList<>();
        MeetingTimeAlgorithm.getUniqueTime(0, usersFree, userEvents);
        assertEquals(tester, usersFree);
    }
    @Test
    public void testGetUniqueTimesOnce() {
        List<CalendarEvent> userEvents = new ArrayList<CalendarEvent>();
        List<CalendarEvent> userEvents2 = new ArrayList<CalendarEvent>();
        List<CalendarEvent> userEvents3 = new ArrayList<CalendarEvent>();
        HashMap<Long, Integer> usersFree = new HashMap<Long, Integer>();
        HashMap<Long, Integer> tester = new HashMap<Long, Integer>();
        List<List<CalendarEvent>> userEventsList = new ArrayList<>();
        userEventsList.add(userEvents);
        MeetingTimeAlgorithm.getUniqueTime(0, usersFree, userEvents);
        assertEquals(tester, usersFree);
    }
    @Test
    public void testGetUniqueTimesTwice() {
        List<CalendarEvent> userEvents = new ArrayList<CalendarEvent>();
        List<CalendarEvent> userEvents2 = new ArrayList<CalendarEvent>();
        List<CalendarEvent> userEvents3 = new ArrayList<CalendarEvent>();
        HashMap<Long, Integer> usersFree = new HashMap<Long, Integer>();
        HashMap<Long, Integer> tester = new HashMap<Long, Integer>();
        List<List<CalendarEvent>> userEventsList = new ArrayList<>();
        userEventsList.add(userEvents);
        userEventsList.add(userEvents2);
        MeetingTimeAlgorithm.getUniqueTime(0, usersFree, userEvents);
        assertEquals(tester, usersFree);
    }
    @Test
    public void testGetUniqueTimesTypicalNumber() {
        List<CalendarEvent> userEvents = new ArrayList<CalendarEvent>();
        List<CalendarEvent> userEvents2 = new ArrayList<CalendarEvent>();
        List<CalendarEvent> userEvents3 = new ArrayList<CalendarEvent>();
        HashMap<Long, Integer> usersFree = new HashMap<Long, Integer>();
        HashMap<Long, Integer> tester = new HashMap<Long, Integer>();
        List<List<CalendarEvent>> userEventsList = new ArrayList<>();
        userEventsList.add(userEvents);
        userEventsList.add(userEvents2);
        userEventsList.add(userEvents3);
        MeetingTimeAlgorithm.getUniqueTime(0, usersFree, userEvents);
        assertEquals(tester, usersFree);
    }

}