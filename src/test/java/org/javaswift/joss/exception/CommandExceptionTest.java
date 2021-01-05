package org.javaswift.joss.exception;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;

import org.junit.Test;

public class CommandExceptionTest {

    @Test
    public void emptyException() {
        CommandException ex = new CommandException("some error");
        assertEquals(0, ex.getHttpStatusCode());
        assertTrue(ex.toString().contains("some error"));
    }

    @Test
    public void emptyErrorCode() {
        CommandException ex = new CommandException(404, null);
        assertEquals(404, ex.getHttpStatusCode());
        assertNull(ex.getError());
        assertFalse(ex.toString().contains("some error"));
    }

    @Test
    public void categorizedError() {
        CommandException ex = new CommandException(404, CommandExceptionError.ENTITY_DOES_NOT_EXIST);
        assertTrue(ex.toString().contains("404"));
        assertTrue(ex.toString().contains(CommandExceptionError.ENTITY_DOES_NOT_EXIST.toString()));
    }
}
