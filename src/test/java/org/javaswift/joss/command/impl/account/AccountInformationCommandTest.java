package org.javaswift.joss.command.impl.account;

import mockit.Expectations;
import mockit.NonStrictExpectations;
import org.apache.http.Header;
import org.javaswift.joss.command.impl.container.ContainerInformationCommandImpl;
import org.javaswift.joss.command.impl.core.BaseCommandTest;
import org.javaswift.joss.exception.CommandException;
import org.javaswift.joss.information.AccountInformation;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static org.javaswift.joss.headers.account.AccountBytesUsed.X_ACCOUNT_BYTES_USED;
import static org.javaswift.joss.headers.account.AccountContainerCount.X_ACCOUNT_CONTAINER_COUNT;
import static org.javaswift.joss.headers.account.AccountMetadata.X_ACCOUNT_META_PREFIX;
import static org.javaswift.joss.headers.account.AccountObjectCount.X_ACCOUNT_OBJECT_COUNT;
import static org.javaswift.joss.headers.account.ServerDate.DATE;

public class AccountInformationCommandTest extends BaseCommandTest {

    @Before
    public void setup() throws IOException {
        super.setup();
        prepareMetadata();
    }

    private void prepareMetadata() {
        final List<Header> headers = new ArrayList<Header>();
        prepareHeader(response, X_ACCOUNT_META_PREFIX + "Description", "Photo album", headers);
        prepareHeader(response, X_ACCOUNT_META_PREFIX + "Year", "1984", headers);
        prepareHeader(response, X_ACCOUNT_CONTAINER_COUNT, "7", headers);
        prepareHeader(response, X_ACCOUNT_OBJECT_COUNT, "123", headers);
        prepareHeader(response, X_ACCOUNT_BYTES_USED, "654321", headers);
        prepareHeader(response, DATE, "Tue, 28 May 2013 12:17:28 GMT", headers);
        new NonStrictExpectations() {{
            response.getAllHeaders();
            result = headers.toArray(new Header[headers.size()]);
        }};
    }

    @Test
    public void getInfoSuccess() throws IOException {
        new Expectations() {{
            statusLine.getStatusCode(); result = 204;
        }};
        AccountInformation info = new AccountInformationCommandImpl(this.account, httpClient, defaultAccess).call();
        assertEquals(7, info.getContainerCount());
        assertEquals(123, info.getObjectCount());
        assertEquals(654321, info.getBytesUsed());
        assertEquals(1369743448000L, info.getServerDate());
    }

    @Test (expected = CommandException.class)
    public void unknownError() throws IOException {
        checkForError(500, new ContainerInformationCommandImpl(this.account, httpClient, defaultAccess, account.getContainer("containerName"), true));
    }

    @Test
    public void isSecure() throws IOException {
        isSecure(new AccountInformationCommandImpl(this.account, httpClient, defaultAccess), 204);
    }

}
