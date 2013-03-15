package org.javaswift.joss.command.container;

import org.javaswift.joss.command.core.BaseCommandTest;
import org.javaswift.joss.exception.CommandException;
import org.javaswift.joss.exception.NotFoundException;
import org.javaswift.joss.information.ContainerInformation;
import org.apache.http.Header;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.when;

import static org.javaswift.joss.headers.container.ContainerMetadata.*;
import static org.javaswift.joss.headers.container.ContainerBytesUsed.X_CONTAINER_BYTES_USED;
import static org.javaswift.joss.headers.container.ContainerObjectCount.X_CONTAINER_OBJECT_COUNT;
import static org.javaswift.joss.headers.container.ContainerRights.X_CONTAINER_READ;

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

    @Test (expected = NotFoundException.class)
    public void createContainerFail() throws IOException {
        checkForError(404, new ContainerInformationCommand(this.account, httpClient, defaultAccess, account.getContainer("containerName")));
    }

    @Test (expected = CommandException.class)
    public void unknownError() throws IOException {
        checkForError(500, new ContainerInformationCommand(this.account, httpClient, defaultAccess, account.getContainer("containerName")));
    }

    @Test
    public void isSecure() throws IOException {
        isSecure(new ContainerInformationCommand(this.account, httpClient, defaultAccess, account.getContainer("containerName")), 204);
    }
}
