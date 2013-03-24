package org.javaswift.joss.command.impl.account;

import org.javaswift.joss.command.impl.container.ContainerInformationCommandImpl;
import org.javaswift.joss.command.impl.core.BaseCommandTest;
import org.javaswift.joss.exception.CommandException;
import org.javaswift.joss.information.AccountInformation;
import org.apache.http.Header;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static org.javaswift.joss.headers.account.AccountMetadata.*;
import static org.javaswift.joss.headers.account.AccountContainerCount.X_ACCOUNT_CONTAINER_COUNT;
import static org.javaswift.joss.headers.account.AccountBytesUsed.X_ACCOUNT_BYTES_USED;
import static org.javaswift.joss.headers.account.AccountObjectCount.X_ACCOUNT_OBJECT_COUNT;
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
        AccountInformation info = new AccountInformationCommandImpl(this.account, httpClient, defaultAccess).call();
        assertEquals(7, info.getContainerCount());
        assertEquals(123, info.getObjectCount());
        assertEquals(654321, info.getBytesUsed());
    }

    @Test (expected = CommandException.class)
    public void unknownError() throws IOException {
        checkForError(500, new ContainerInformationCommandImpl(this.account, httpClient, defaultAccess, account.getContainer("containerName")));
    }

    @Test
    public void isSecure() throws IOException {
        isSecure(new AccountInformationCommandImpl(this.account, httpClient, defaultAccess), 204);
    }

}
