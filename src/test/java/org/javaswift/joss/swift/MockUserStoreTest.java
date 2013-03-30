package org.javaswift.joss.swift;

import org.javaswift.joss.exception.CommandException;
import org.javaswift.joss.exception.CommandExceptionError;
import org.javaswift.joss.swift.MockUserStore;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;

public class MockUserStoreTest {

    private MockUserStore users = new MockUserStore();

    @Before
    public void prepare() {
        users.addUser("richard", "test123");
    }

    @Test
    public void authenticateSuccess() {
        try {
            users.authenticate("richard", "test123");
        } catch (Exception err) {
            fail("Should not have thrown an exception");
        }
    }

    @Test
    public void userDoesNotExist() {
        try {
            users.authenticate("charlie", "123test");
        } catch (CommandException err) {
            assertEquals(CommandExceptionError.UNAUTHORIZED, err.getError());
        }
    }

    @Test
    public void userHasWrongPassword() {
        try {
            users.authenticate("richard", "321tset");
        } catch (CommandException err) {
            assertEquals(CommandExceptionError.UNAUTHORIZED, err.getError());
        }
    }
}
