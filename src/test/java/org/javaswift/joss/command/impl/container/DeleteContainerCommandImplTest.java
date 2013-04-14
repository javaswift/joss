package org.javaswift.joss.command.impl.container;

import org.javaswift.joss.command.impl.core.BaseCommandTest;
import org.javaswift.joss.exception.CommandException;
import org.javaswift.joss.exception.NotEmptyException;
import org.javaswift.joss.exception.NotFoundException;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class DeleteContainerCommandImplTest extends BaseCommandTest {

    @Before
    public void setup() throws IOException {
        super.setup();
    }

    @Test
    public void deleteContainerSuccess() throws IOException {
        expectStatusCode(204);
        new DeleteContainerCommandImpl(this.account, httpClient, defaultAccess, account.getContainer("containerName")).call();
    }

    @Test(expected = NotFoundException.class)
    public void deleteContainerDoesNotExist() throws IOException {
        checkForError(404, new DeleteContainerCommandImpl(this.account, httpClient, defaultAccess, account.getContainer("containerName")));
    }

    @Test(expected = NotEmptyException.class)
    public void deleteContainerNotEmpty() throws IOException {
        checkForError(409, new DeleteContainerCommandImpl(this.account, httpClient, defaultAccess, account.getContainer("containerName")));
    }

    @Test(expected = CommandException.class)
    public void unknownError() throws IOException {
        checkForError(500, new DeleteContainerCommandImpl(this.account, httpClient, defaultAccess, account.getContainer("containerName")));
    }

    @Test
    public void isSecure() throws IOException {
        isSecure(new DeleteContainerCommandImpl(this.account, httpClient, defaultAccess, account.getContainer("containerName")), 204);
    }
}
