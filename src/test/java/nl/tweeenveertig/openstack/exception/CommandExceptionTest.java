package nl.tweeenveertig.openstack.exception;

import nl.tweeenveertig.openstack.command.core.CommandExceptionError;
import nl.tweeenveertig.openstack.exception.CommandException;
import org.junit.Test;

import static junit.framework.Assert.*;

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
        CommandException ex = new CommandException(404, CommandExceptionError.CONTAINER_DOES_NOT_EXIST);
        assertTrue(ex.toString().contains("404"));
        assertTrue(ex.toString().contains(CommandExceptionError.CONTAINER_DOES_NOT_EXIST.toString()));
    }
}
