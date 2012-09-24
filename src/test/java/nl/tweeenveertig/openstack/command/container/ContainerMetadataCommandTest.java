package nl.tweeenveertig.openstack.command.container;

import nl.tweeenveertig.openstack.command.account.AccountMetadataCommand;
import nl.tweeenveertig.openstack.command.core.BaseCommandTest;
import nl.tweeenveertig.openstack.command.core.CommandExceptionError;
import nl.tweeenveertig.openstack.headers.Header;
import nl.tweeenveertig.openstack.headers.account.AccountMetadata;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

import static junit.framework.Assert.assertEquals;
import static nl.tweeenveertig.openstack.headers.account.AccountMetadata.X_ACCOUNT_META_PREFIX;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ContainerMetadataCommandTest extends BaseCommandTest {

    @Before
    public void setup() throws IOException {
        super.setup();
    }

    @Test
    public void getInfoSuccess() throws IOException {
        when(statusLine.getStatusCode()).thenReturn(204);
        Collection<Header> headers = new ArrayList<Header>();
        headers.add(new AccountMetadata("Year", "123"));
        headers.add(new AccountMetadata("Title", "Roses are Red"));
        headers.add(new AccountMetadata("ISBN", "123456789"));
        new ContainerMetadataCommand(this.account, httpClient, defaultAccess, account.getContainer("containerName"), headers).call();
        verify(httpClient).execute(requestArgument.capture());
        assertEquals("123", requestArgument.getValue().getFirstHeader(X_ACCOUNT_META_PREFIX + "Year").getValue());
        assertEquals("Roses are Red", requestArgument.getValue().getFirstHeader(X_ACCOUNT_META_PREFIX + "Title").getValue());
        assertEquals("123456789", requestArgument.getValue().getFirstHeader(X_ACCOUNT_META_PREFIX + "ISBN").getValue());
    }

    @Test
    public void createContainerFail() throws IOException {
        checkForError(404, new ContainerMetadataCommand(this.account, httpClient, defaultAccess, account.getContainer("containerName"), new ArrayList<Header>()), CommandExceptionError.CONTAINER_DOES_NOT_EXIST);
    }

    @Test
    public void unknownError() throws IOException {
        checkForError(500, new ContainerMetadataCommand(this.account, httpClient, defaultAccess, account.getContainer("containerName"), new ArrayList<Header>()), CommandExceptionError.UNKNOWN);
    }

    @Test
    public void isSecure() throws IOException {
        isSecure(new ContainerMetadataCommand(this.account, httpClient, defaultAccess, account.getContainer("containerName"), new ArrayList<Header>()), 204);
    }
}
