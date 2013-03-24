package org.javaswift.joss.command.impl.container;

import org.javaswift.joss.command.impl.core.BaseCommandTest;
import org.javaswift.joss.exception.CommandException;
import org.javaswift.joss.exception.NotFoundException;
import org.javaswift.joss.headers.Header;
import org.javaswift.joss.headers.account.AccountMetadata;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import static junit.framework.Assert.assertEquals;
import static org.javaswift.joss.headers.account.AccountMetadata.X_ACCOUNT_META_PREFIX;
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

    @Test (expected = NotFoundException.class)
    public void createContainerFail() throws IOException {
        checkForError(404, new ContainerMetadataCommand(this.account, httpClient, defaultAccess, account.getContainer("containerName"), new ArrayList<Header>()));
    }

    @Test (expected = CommandException.class)
    public void unknownError() throws IOException {
        checkForError(500, new ContainerMetadataCommand(this.account, httpClient, defaultAccess, account.getContainer("containerName"), new ArrayList<Header>()));
    }

    @Test
    public void isSecure() throws IOException {
        isSecure(new ContainerMetadataCommand(this.account, httpClient, defaultAccess, account.getContainer("containerName"), new ArrayList<Header>()), 204);
    }
}
