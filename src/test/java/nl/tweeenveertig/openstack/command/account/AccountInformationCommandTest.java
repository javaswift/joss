package nl.tweeenveertig.openstack.command.account;

import nl.tweeenveertig.openstack.command.container.ContainerInformationCommand;
import nl.tweeenveertig.openstack.command.core.BaseCommandTest;
import nl.tweeenveertig.openstack.command.core.CommandExceptionError;
import nl.tweeenveertig.openstack.headers.Token;
import nl.tweeenveertig.openstack.model.AccountInformation;
import org.apache.http.Header;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static nl.tweeenveertig.openstack.headers.account.AccountMetadata.*;
import static nl.tweeenveertig.openstack.headers.account.AccountContainerCount.X_ACCOUNT_CONTAINER_COUNT;
import static nl.tweeenveertig.openstack.headers.account.AccountBytesUsed.X_ACCOUNT_BYTES_USED;
import static nl.tweeenveertig.openstack.headers.account.AccountObjectCount.X_ACCOUNT_OBJECT_COUNT;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AccountInformationCommandTest extends BaseCommandTest {

    @Before
    public void setup() throws IOException {
        super.setup();
        prepareMetadata();
    }

    private void prepareMetadata() {
        List<Header> headers = new ArrayList<Header>();
        prepareHeader(response, X_ACCOUNT_META_PREFIX + "Description", "Photo album", headers);
        prepareHeader(response, X_ACCOUNT_META_PREFIX + "Year", "1984", headers);
        prepareHeader(response, X_ACCOUNT_CONTAINER_COUNT, "7", headers);
        prepareHeader(response, X_ACCOUNT_OBJECT_COUNT, "123", headers);
        prepareHeader(response, X_ACCOUNT_BYTES_USED, "654321", headers);
        when(response.getAllHeaders()).thenReturn(headers.toArray(new Header[headers.size()]));
    }

    @Test
    public void getInfoSuccess() throws IOException {
        when(statusLine.getStatusCode()).thenReturn(204);
        AccountInformation info = new AccountInformationCommand(this.account, httpClient, defaultAccess).call();
        assertEquals(7, info.getContainerCount());
        assertEquals(123, info.getObjectCount());
        assertEquals(654321, info.getBytesUsed());
    }

    @Test
    public void unknownError() throws IOException {
        checkForError(500, new ContainerInformationCommand(this.account, httpClient, defaultAccess, account.getContainer("containerName")), CommandExceptionError.UNKNOWN);
    }

    @Test
    public void isSecure() throws IOException {
        isSecure(new AccountInformationCommand(this.account, httpClient, defaultAccess), 204);
    }

}
