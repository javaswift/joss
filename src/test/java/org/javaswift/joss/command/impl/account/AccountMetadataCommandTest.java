package org.javaswift.joss.command.impl.account;

import org.javaswift.joss.command.impl.core.BaseCommandTest;
import org.javaswift.joss.exception.CommandException;
import org.javaswift.joss.headers.Header;
import org.javaswift.joss.headers.account.AccountMetadata;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import static org.javaswift.joss.headers.account.AccountMetadata.X_ACCOUNT_META_PREFIX;

public class AccountMetadataCommandTest extends BaseCommandTest {

    @Before
    public void setup() throws IOException {
        super.setup();
    }

    @Test
    public void getInfoSuccess() throws IOException {
        expectStatusCode(204);
        Collection<Header> headers = new ArrayList<Header>();
        headers.add(new AccountMetadata("Year", "1989"));
        headers.add(new AccountMetadata("Company", "42 BV"));
        new AccountMetadataCommandImpl(this.account, httpClient, defaultAccess, headers).call();
        verifyHeaderValue("1989", X_ACCOUNT_META_PREFIX + "Year", "POST");
        verifyHeaderValue("42 BV", X_ACCOUNT_META_PREFIX + "Company", "POST");
    }

    @Test (expected = CommandException.class)
    public void unknownError() throws IOException {
        checkForError(500, new AccountMetadataCommandImpl(this.account, httpClient, defaultAccess, new ArrayList<Header>()));
    }

    @Test
    public void isSecure() throws IOException {
        isSecure(new AccountMetadataCommandImpl(this.account, httpClient, defaultAccess, new ArrayList<Header>()), 204);
    }
}
