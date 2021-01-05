package org.javaswift.joss.command.impl.container;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.javaswift.joss.headers.container.ContainerBytesUsed.X_CONTAINER_BYTES_USED;
import static org.javaswift.joss.headers.container.ContainerMetadata.X_CONTAINER_META_PREFIX;
import static org.javaswift.joss.headers.container.ContainerObjectCount.X_CONTAINER_OBJECT_COUNT;
import static org.javaswift.joss.headers.container.ContainerRights.X_CONTAINER_READ;
import static org.javaswift.joss.headers.container.ContainerWritePermissions.X_CONTAINER_WRITE;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.javaswift.joss.command.impl.core.BaseCommandTest;
import org.javaswift.joss.exception.CommandException;
import org.javaswift.joss.exception.NotFoundException;
import org.javaswift.joss.information.ContainerInformation;
import org.junit.Before;
import org.junit.Test;

public class ContainerInformationCommandImplTest extends BaseCommandTest {

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
        prepareHeader(response, X_CONTAINER_WRITE, "3,1,4", headers);
        prepareHeadersForRetrieval(response, headers);
    }

    @Test
    public void getInfoSuccess() throws IOException {
        expectStatusCode(204);
        ContainerInformation info = new ContainerInformationCommandImpl(this.account, httpClient, defaultAccess, account.getContainer("containerName"), true).call();
        assertEquals("Photo album", info.getMetadata("Description"));
        assertEquals("1984", info.getMetadata("Year"));
        assertEquals(123, info.getObjectCount());
        assertEquals(654321, info.getBytesUsed());
        assertEquals(".r:*", info.getReadPermissions());
        assertEquals("3,1,4", info.getWritePermissions());
        assertTrue(info.isPublicContainer());
    }

    @Test (expected = NotFoundException.class)
    public void createContainerFail() throws IOException {
        checkForError(404, new ContainerInformationCommandImpl(this.account, httpClient, defaultAccess, account.getContainer("containerName"), true));
    }

    @Test (expected = CommandException.class)
    public void unknownError() throws IOException {
        checkForError(500, new ContainerInformationCommandImpl(this.account, httpClient, defaultAccess, account.getContainer("containerName"), true));
    }

    @Test
    public void isSecure() throws IOException {
        isSecure(new ContainerInformationCommandImpl(this.account, httpClient, defaultAccess, account.getContainer("containerName"), true), 204);
    }
}
