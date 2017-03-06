package com.tempo;

import org.junit.Test;
import com.tempo.Model.Group;
import com.tempo.Model.User;

import java.util.ArrayList;

import static org.junit.Assert.*;
/**
 * Created by Someone on 2/25/17.
 */

public class TestUser {

    @Test
    public void TestCreateNewGroup() {
        User admin = new User("Falessi", null);
        Group testGroup = new Group("This Group", admin, null, null);
        System.out.println("Testgroup: " + testGroup);
        System.out.println("Create new group: " + admin.createNewGroup("This Group"));
        assertEquals(true, testGroup.getName().equals(admin.createNewGroup("This Group").getName()));
    }

    @Test
    public void TestGetUserName() {
        User newUser = new User("Falessi", null);
        assertEquals("Falessi", newUser.getUserName());
    }

    @Test
    public void deleteGroupSuccess() {
        User admin = new User("Costin", null);
        User member1 = new User("Falessi", null);
        ArrayList<User> members = new ArrayList<User>();
        Group testGroup = new Group("Test Group", admin, members, null);
        Group testGroup2 = new Group("Test Group 2", admin, members, null);
        admin.leaveGroup("Test Group 2");
        assertEquals(false, admin.getGroups().contains("Test Group 2"));
    }

}
