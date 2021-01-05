package org.javaswift.joss.swift;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class MockUserStoreTest {

    private MockUserStore users = new MockUserStore();

    @Before
    public void prepare() {
        users.addUser("richard", "test123");
    }

    @Test
    public void authenticateSuccess() {
        assertTrue(users.authenticate("", "", "richard", "test123"));
    }

    @Test
    public void userDoesNotExist() {
        assertFalse(users.authenticate("", "", "charlie", "123test"));
    }

    @Test
    public void userHasWrongPassword() {
        assertFalse(users.authenticate("", "", "richard", "321tset"));
    }
}
