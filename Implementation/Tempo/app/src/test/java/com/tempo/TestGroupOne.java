package com.tempo;

import com.tempo.model.Group;
import com.tempo.model.User;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;
/**
 * Created by Brandon on 2/25/17.
 */

public class TestGroupOne {

    @Test
    public void TestDeleteUserFromGroupAdmin() {
        User admin = new User("Jessie", null);
        User member1 = new User("Falessi", null);
        ArrayList<User> members = new ArrayList<User>();
        Group testGroup = new Group("Test Group", admin, members, null);
        testGroup.addUserToGroup(admin);
        testGroup.addUserToGroup(member1);
        assertEquals(true, testGroup.deleteUserFromGroup(admin));
    }

    @Test
    public void TestDeleteUserFromGroupMember() {
        User admin = new User("Jessie", null);
        User member1 = new User("Falessi", null);
        ArrayList<User> members = new ArrayList<User>();
        Group testGroup = new Group("Test Group", admin, members, null);
        testGroup.addUserToGroup(admin);
        testGroup.addUserToGroup(member1);
        assertEquals(true, testGroup.deleteUserFromGroup(member1));
    }

    @Test
    public void TestDeleteUserFromGroupNonExistent() {
        User admin = new User("Jessie", null);
        User member1 = new User("Falessi", null);
        User randomUser = new User("Rebecca", null);
        ArrayList<User> members = new ArrayList<User>();
        Group testGroup = new Group("Test Group", admin, members, null);
        testGroup.addUserToGroup(admin);
        testGroup.addUserToGroup(member1);
        assertEquals(false, testGroup.deleteUserFromGroup(randomUser));
    }
}
