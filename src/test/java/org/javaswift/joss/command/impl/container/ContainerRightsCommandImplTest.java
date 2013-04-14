package org.javaswift.joss.command.impl.container;

import org.javaswift.joss.command.impl.core.BaseCommandTest;
import org.javaswift.joss.exception.CommandException;
import org.javaswift.joss.exception.NotFoundException;
import org.javaswift.joss.headers.container.ContainerRights;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class ContainerRightsCommandImplTest extends BaseCommandTest {

    @Before
    public void setup() throws IOException {
        super.setup();
    }

    @Test
    public void createContainerSuccess() throws IOException {
        expectStatusCode(202);
        new ContainerRightsCommandImpl(this.account, httpClient, defaultAccess, account.getContainer("containerName"), true).call();
        verifyHeaderValue(ContainerRights.PUBLIC_CONTAINER, ContainerRights.X_CONTAINER_READ);
    }

    @Test (expected = NotFoundException.class)
    public void createContainerFail() throws IOException {
        checkForError(404, new ContainerRightsCommandImpl(this.account, httpClient, defaultAccess, account.getContainer("containerName"), true));
    }

    @Test (expected = CommandException.class)
    public void unknownError() throws IOException {
        checkForError(500, new ContainerRightsCommandImpl(this.account, httpClient, defaultAccess, account.getContainer("containerName"), true));
    }

    @Test
    public void isSecure() throws IOException {
        isSecure(new ContainerRightsCommandImpl(this.account, httpClient, defaultAccess, account.getContainer("containerName"), true), 202);
    }

}
