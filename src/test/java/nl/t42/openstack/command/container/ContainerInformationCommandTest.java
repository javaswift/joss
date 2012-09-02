package nl.t42.openstack.command.container;

import nl.t42.openstack.command.core.BaseCommandTest;
import nl.t42.openstack.command.core.CommandExceptionError;
import nl.t42.openstack.model.Container;
import nl.t42.openstack.model.ContainerInformation;
import org.apache.http.Header;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.when;

import static nl.t42.openstack.command.container.ContainerInformationCommand.*;

public class ContainerInformationCommandTest extends BaseCommandTest {

    @Before
    public void setup() throws IOException {
        super.setup();
    }

    @Test
    public void getInfoSuccess() throws IOException {
        when(statusLine.getStatusCode()).thenReturn(204);
        List<Header> headers = new ArrayList<Header>();
        prepareHeader(response, X_CONTAINER_META_PREFIX + "Description", "Photo album", headers);
        prepareHeader(response, X_CONTAINER_META_PREFIX + "Year", "1984", headers);
        prepareHeader(response, X_CONTAINER_OBJECT_COUNT, "123", headers);
        prepareHeader(response, X_CONTAINER_BYTES_USED, "654321", headers);
        when(response.getAllHeaders()).thenReturn(headers.toArray(new Header[headers.size()]));
        ContainerInformation info = new ContainerInformationCommand(httpClient, defaultAccess, new Container("containerName")).execute();
        assertEquals("Photo album", info.getMetadata().get("Description"));
        assertEquals("1984", info.getMetadata().get("Year"));
        assertEquals(123, info.getObjectCount());
        assertEquals(654321, info.getBytesUsed());
    }

    @Test
    public void createContainerFail() throws IOException {
        checkForError(404, new ContainerInformationCommand(httpClient, defaultAccess, new Container("containerName")), CommandExceptionError.CONTAINER_DOES_NOT_EXIST);
    }

    @Test
    public void unknownError() throws IOException {
        checkForError(500, new ContainerInformationCommand(httpClient, defaultAccess, new Container("containerName")), CommandExceptionError.UNKNOWN);
    }
}
