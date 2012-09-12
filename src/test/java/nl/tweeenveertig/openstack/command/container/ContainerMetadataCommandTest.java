package nl.tweeenveertig.openstack.command.container;

import nl.tweeenveertig.openstack.command.core.BaseCommandTest;
import nl.tweeenveertig.openstack.command.core.CommandExceptionError;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

import static org.mockito.Mockito.when;

public class ContainerMetadataCommandTest extends BaseCommandTest {

    @Before
    public void setup() throws IOException {
        super.setup();
    }

    @Test
    public void getInfoSuccess() throws IOException {
        when(statusLine.getStatusCode()).thenReturn(204);
        Map<String, Object> metadata = new TreeMap<String, Object>();
        metadata.put("Year", 123);
        metadata.put("Title", "Roses are Red");
        metadata.put("ISBN", 123456789);
        new ContainerMetadataCommand(httpClient, defaultAccess, account.getContainer("containerName"), metadata).call();
    }

    @Test
    public void createContainerFail() throws IOException {
        checkForError(404, new ContainerMetadataCommand(httpClient, defaultAccess, account.getContainer("containerName"), new TreeMap<String, Object>()), CommandExceptionError.CONTAINER_DOES_NOT_EXIST);
    }

    @Test
    public void unknownError() throws IOException {
        checkForError(500, new ContainerMetadataCommand(httpClient, defaultAccess, account.getContainer("containerName"), new TreeMap<String, Object>()), CommandExceptionError.UNKNOWN);
    }
}
