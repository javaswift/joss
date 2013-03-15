package org.javaswift.joss.command.account;

import org.javaswift.joss.command.core.BaseCommandTest;
import org.javaswift.joss.exception.CommandException;
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

public class AccountMetadataCommandTest extends BaseCommandTest {

    @Before
    public void setup() throws IOException {
        super.setup();
    }

    @Test
    public void getInfoSuccess() throws IOException {
        when(statusLine.getStatusCode()).thenReturn(204);
        Collection<Header> headers = new ArrayList<Header>();
        headers.add(new AccountMetadata("Year", "1989"));
        headers.add(new AccountMetadata("Company", "42 BV"));
        new AccountMetadataCommand(this.account, httpClient, defaultAccess, headers).call();
        verify(httpClient).execute(requestArgument.capture());
        assertEquals("1989", requestArgument.getValue().getFirstHeader(X_ACCOUNT_META_PREFIX + "Year").getValue());
        assertEquals("42 BV", requestArgument.getValue().getFirstHeader(X_ACCOUNT_META_PREFIX + "Company").getValue());
    }

    @Test (expected = CommandException.class)
    public void unknownError() throws IOException {
        checkForError(500, new AccountMetadataCommand(this.account, httpClient, defaultAccess, new ArrayList<Header>()));
    }

    @Test
    public void isSecure() throws IOException {
        isSecure(new AccountMetadataCommand(this.account, httpClient, defaultAccess, new ArrayList<Header>()), 204);
    }
}
