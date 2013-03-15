package org.javaswift.joss.command.container;

import org.javaswift.joss.command.core.BaseCommandTest;
import org.javaswift.joss.exception.CommandException;
import org.javaswift.joss.exception.NotEmptyException;
import org.javaswift.joss.exception.NotFoundException;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;
import static org.mockito.Mockito.when;

public class DeleteContainerCommandTest extends BaseCommandTest {

    @Before
    public void setup() throws IOException {
        super.setup();
    }

    @Test
    public void deleteContainerSuccess() throws IOException {
        when(statusLine.getStatusCode()).thenReturn(204);
        new DeleteContainerCommand(this.account, httpClient, defaultAccess, account.getContainer("containerName")).call();
    }

    @Test(expected = NotFoundException.class)
    public void deleteContainerDoesNotExist() throws IOException {
        checkForError(404, new DeleteContainerCommand(this.account, httpClient, defaultAccess, account.getContainer("containerName")));
    }

    @Test(expected = NotEmptyException.class)
    public void deleteContainerNotEmpty() throws IOException {
        checkForError(409, new DeleteContainerCommand(this.account, httpClient, defaultAccess, account.getContainer("containerName")));
    }

    @Test(expected = CommandException.class)
    public void unknownError() throws IOException {
        checkForError(500, new DeleteContainerCommand(this.account, httpClient, defaultAccess, account.getContainer("containerName")));
    }

    @Test
    public void isSecure() throws IOException {
        isSecure(new DeleteContainerCommand(this.account, httpClient, defaultAccess, account.getContainer("containerName")), 204);
    }
}
