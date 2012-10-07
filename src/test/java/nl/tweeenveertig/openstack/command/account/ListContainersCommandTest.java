package nl.tweeenveertig.openstack.command.account;

import nl.tweeenveertig.openstack.command.core.BaseCommandTest;
import nl.tweeenveertig.openstack.exception.CommandException;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.mockito.Mockito.when;

public class ListContainersCommandTest extends BaseCommandTest {

    @Before
    public void setup() throws IOException {
        super.setup();
    }

    @Test
    public void listContainers() throws IOException {
        new ListContainersCommand(this.account, httpClient, defaultAccess).call();
    }

    @Test
    public void listContainersWithNoneThere() throws IOException {
        when(statusLine.getStatusCode()).thenReturn(204);
        new ListContainersCommand(this.account, httpClient, defaultAccess).call();
    }

    @Test (expected = CommandException.class)
    public void unknownError() throws IOException {
        checkForError(500, new ListContainersCommand(this.account, httpClient, defaultAccess));
    }

    @Test
    public void isSecure() throws IOException {
        isSecure(new ListContainersCommand(this.account, httpClient, defaultAccess));
    }
}
