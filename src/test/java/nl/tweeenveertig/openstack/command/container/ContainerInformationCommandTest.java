package nl.tweeenveertig.openstack.command.container;

import nl.tweeenveertig.openstack.command.account.AccountInformationCommand;
import nl.tweeenveertig.openstack.command.core.BaseCommandTest;
import nl.tweeenveertig.openstack.command.core.CommandExceptionError;
import nl.tweeenveertig.openstack.model.ContainerInformation;
import org.apache.http.Header;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.when;

import static nl.tweeenveertig.openstack.headers.container.ContainerMetadata.*;
import static nl.tweeenveertig.openstack.headers.container.ContainerBytesUsed.X_CONTAINER_BYTES_USED;
import static nl.tweeenveertig.openstack.headers.container.ContainerObjectCount.X_CONTAINER_OBJECT_COUNT;
import static nl.tweeenveertig.openstack.headers.container.ContainerRights.X_CONTAINER_READ;

public class ContainerInformationCommandTest extends BaseCommandTest {

    @Before
    public void setup() throws IOException {
        super.setup();
        prepareMetadata();
    }

    private void prepareMetadata() {
        List<Header> headers = new ArrayList<Header>();
        prepareHeader(response, X_CONTAINER_META_PREFIX + "Description", "Photo album", headers);
        prepareHeader(response, X_CONTAINER_META_PREFIX + "Year", "1984", headers);
        prepareHeader(response, X_CONTAINER_OBJECT_COUNT, "123", headers);
        prepareHeader(response, X_CONTAINER_BYTES_USED, "654321", headers);
        prepareHeader(response, X_CONTAINER_READ, ".r:*", headers);
        when(response.getAllHeaders()).thenReturn(headers.toArray(new Header[headers.size()]));
    }

    @Test
    public void getInfoSuccess() throws IOException {
        when(statusLine.getStatusCode()).thenReturn(204);
        ContainerInformation info = new ContainerInformationCommand(this.account, httpClient, defaultAccess, account.getContainer("containerName")).call();
        assertEquals("Photo album", info.getMetadata("Description"));
        assertEquals("1984", info.getMetadata("Year"));
        assertEquals(123, info.getObjectCount());
        assertEquals(654321, info.getBytesUsed());
        assertTrue(info.isPublicContainer());
    }

    @Test
    public void createContainerFail() throws IOException {
        checkForError(404, new ContainerInformationCommand(this.account, httpClient, defaultAccess, account.getContainer("containerName")), CommandExceptionError.CONTAINER_DOES_NOT_EXIST);
    }

    @Test
    public void unknownError() throws IOException {
        checkForError(500, new ContainerInformationCommand(this.account, httpClient, defaultAccess, account.getContainer("containerName")), CommandExceptionError.UNKNOWN);
    }

    @Test
    public void isSecure() throws IOException {
        isSecure(new ContainerInformationCommand(this.account, httpClient, defaultAccess, account.getContainer("containerName")), 204);
    }
}
