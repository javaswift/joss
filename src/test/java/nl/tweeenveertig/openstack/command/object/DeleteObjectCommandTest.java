package nl.tweeenveertig.openstack.command.object;

import nl.tweeenveertig.openstack.command.core.BaseCommandTest;
import nl.tweeenveertig.openstack.command.core.CommandExceptionError;
import nl.tweeenveertig.openstack.exception.CommandException;
import nl.tweeenveertig.openstack.exception.NotFoundException;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.mockito.Mockito.when;

public class DeleteObjectCommandTest extends BaseCommandTest {

    @Before
    public void setup() throws IOException {
        super.setup();
    }

    @Test
    public void deleteContainerSuccess() throws IOException {
        when(statusLine.getStatusCode()).thenReturn(204);
        new DeleteObjectCommand(this.account, httpClient, defaultAccess, account.getContainer("containerName"), getObject("objectName")).call();
    }

    @Test (expected = NotFoundException.class)
    public void deleteContainerDoesNotExist() throws IOException {
        checkForError(404, new DeleteObjectCommand(this.account, httpClient, defaultAccess, account.getContainer("containerName"), getObject("objectName")));
    }

    @Test (expected = CommandException.class)
    public void unknownError() throws IOException {
        checkForError(500, new DeleteObjectCommand(this.account, httpClient, defaultAccess, account.getContainer("containerName"), getObject("objectName")));
    }

    @Test
    public void isSecure() throws IOException {
        isSecure(new DeleteObjectCommand(this.account, httpClient, defaultAccess,
                account.getContainer("containerName"), getObject("objectName")), 204);
    }
}
