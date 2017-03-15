package com.tempo;

import com.tempo.model.Group;
import com.tempo.model.User;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;
/**
 * Created by Alex on 2/25/17.
 */

public class TestGroupTwo {
    @Test
    public void TestgetMembersNonExistent() {
        User admin = new User("Jessie", null);
        User member1 = new User("Falessi", null);
        User randomUser = new User("Rebecca", null);
        ArrayList<User> members = new ArrayList<User>();
        Group testGroup = new Group("Test Group", admin, members, null);
        testGroup.addUserToGroup(admin);
        assertEquals(false, testGroup.getMembers().contains(member1));
    }

    @Test
    public void TestAddUserToGroup() {
        User admin = new User("Jessie", null);
        User member1 = new User("Falessi", null);
        User randomUser = new User("Rebecca", null);
        ArrayList<User> members = new ArrayList<User>();
        Group testGroup = new Group("Test Group", admin, members, null);
        testGroup.addUserToGroup(admin);
        testGroup.addUserToGroup(member1);
        assertEquals(true, testGroup.getMembers().contains(member1));
    }
}
