package nl.tweeenveertig.openstack.command.object;

import nl.tweeenveertig.openstack.command.core.BaseCommandTest;
import nl.tweeenveertig.openstack.command.core.CommandExceptionError;
import nl.tweeenveertig.openstack.model.Container;
import nl.tweeenveertig.openstack.model.StoreObject;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

import static org.mockito.Mockito.when;

public class ObjectMetadataCommandTest extends BaseCommandTest {

    @Before
    public void setup() throws IOException {
        super.setup();
    }

    @Test
    public void getInfoSuccess() throws IOException {
        when(statusLine.getStatusCode()).thenReturn(202);
        Map<String, Object> metadata = new TreeMap<String, Object>();
        metadata.put("Year", 1989);
        metadata.put("Company", "42 BV");
        new ObjectMetadataCommand(httpClient, defaultAccess, new Container("containerName"), new StoreObject("objectName"), metadata).call();
    }

    @Test
    public void objectDoesNotExist() throws IOException {
        checkForError(404, new ObjectMetadataCommand(httpClient, defaultAccess, new Container("containerName"), new StoreObject("objectName"), new TreeMap<String, Object>()), CommandExceptionError.CONTAINER_OR_OBJECT_DOES_NOT_EXIST);
    }

    @Test
    public void unknownError() throws IOException {
        checkForError(500, new ObjectMetadataCommand(httpClient, defaultAccess, new Container("containerName"), new StoreObject("objectName"), new TreeMap<String, Object>()), CommandExceptionError.UNKNOWN);
    }
}
