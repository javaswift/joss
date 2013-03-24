package org.javaswift.joss.command.impl.container;

import org.javaswift.joss.command.impl.core.BaseCommandTest;
import org.javaswift.joss.exception.AlreadyExistsException;
import org.javaswift.joss.exception.CommandException;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;
import static org.mockito.Mockito.when;

public class CreateContainerCommandImplTest extends BaseCommandTest {

    @Before
    public void setup() throws IOException {
        super.setup();
    }

    @Test
    public void createContainerSuccess() throws IOException {
        when(statusLine.getStatusCode()).thenReturn(201);
        new CreateContainerCommandImpl(this.account, httpClient, defaultAccess, account.getContainer("containerName")).call();
    }

    @Test(expected = AlreadyExistsException.class)
    public void createContainerFail() throws IOException {
        checkForError(202, new CreateContainerCommandImpl(this.account, httpClient, defaultAccess, account.getContainer("containerName")));
    }

    @Test (expected = CommandException.class)
    public void unknownError() throws IOException {
        checkForError(500, new CreateContainerCommandImpl(this.account, httpClient, defaultAccess, account.getContainer("containerName")));
    }

    @Test
    public void isSecure() throws IOException {
        isSecure(new CreateContainerCommandImpl(this.account, httpClient, defaultAccess, account.getContainer("containerName")), 201);
    }
}
