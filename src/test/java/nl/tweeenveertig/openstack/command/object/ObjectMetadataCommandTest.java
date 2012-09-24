package nl.tweeenveertig.openstack.command.object;

import nl.tweeenveertig.openstack.command.container.ContainerMetadataCommand;
import nl.tweeenveertig.openstack.command.core.BaseCommandTest;
import nl.tweeenveertig.openstack.command.core.CommandExceptionError;
import nl.tweeenveertig.openstack.headers.Header;
import nl.tweeenveertig.openstack.headers.account.AccountMetadata;
import nl.tweeenveertig.openstack.headers.object.ObjectMetadata;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import static junit.framework.Assert.assertEquals;
import static nl.tweeenveertig.openstack.headers.object.ObjectMetadata.X_OBJECT_META_PREFIX;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ObjectMetadataCommandTest extends BaseCommandTest {

    @Before
    public void setup() throws IOException {
        super.setup();
    }

    @Test
    public void getInfoSuccess() throws IOException {
        when(statusLine.getStatusCode()).thenReturn(202);
        Collection<Header> headers = new ArrayList<Header>();
        headers.add(new ObjectMetadata("Year", "1989"));
        headers.add(new ObjectMetadata("Company", "42 BV"));
        new ObjectMetadataCommand(this.account, httpClient, defaultAccess, account.getContainer("containerName"), getObject("objectName"), headers).call();
        verify(httpClient).execute(requestArgument.capture());
        assertEquals("1989", requestArgument.getValue().getFirstHeader(X_OBJECT_META_PREFIX + "Year").getValue());
        assertEquals("42 BV", requestArgument.getValue().getFirstHeader(X_OBJECT_META_PREFIX + "Company").getValue());
    }

    @Test
    public void objectDoesNotExist() throws IOException {
        checkForError(404, new ObjectMetadataCommand(this.account, httpClient, defaultAccess, account.getContainer("containerName"), getObject("objectName"), new ArrayList<Header>()), CommandExceptionError.CONTAINER_OR_OBJECT_DOES_NOT_EXIST);
    }

    @Test
    public void unknownError() throws IOException {
        checkForError(500, new ObjectMetadataCommand(this.account, httpClient, defaultAccess, account.getContainer("containerName"), getObject("objectName"), new ArrayList<Header>()), CommandExceptionError.UNKNOWN);
    }

    @Test
    public void isSecure() throws IOException {
        isSecure(new ObjectMetadataCommand(this.account, httpClient, defaultAccess, account.getContainer("containerName"), getObject("objectName"), new ArrayList<Header>()), 202);
    }
}
