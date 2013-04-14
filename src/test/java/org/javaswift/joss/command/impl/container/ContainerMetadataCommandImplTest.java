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

import static org.javaswift.joss.headers.account.AccountMetadata.X_ACCOUNT_META_PREFIX;

public class ContainerMetadataCommandImplTest extends BaseCommandTest {

    @Before
    public void setup() throws IOException {
        super.setup();
    }

    @Test
    public void getInfoSuccess() throws IOException {
        expectStatusCode(204);
        Collection<Header> headers = new ArrayList<Header>();
        headers.add(new AccountMetadata("Year", "123"));
        headers.add(new AccountMetadata("Title", "Roses are Red"));
        headers.add(new AccountMetadata("ISBN", "123456789"));
        new ContainerMetadataCommandImpl(this.account, httpClient, defaultAccess, account.getContainer("containerName"), headers).call();
        verifyHeaderValue("123", X_ACCOUNT_META_PREFIX + "Year", "POST");
        verifyHeaderValue("Roses are Red", X_ACCOUNT_META_PREFIX + "Title", "POST");
        verifyHeaderValue("123456789", X_ACCOUNT_META_PREFIX + "ISBN", "POST");
    }

    @Test (expected = NotFoundException.class)
    public void createContainerFail() throws IOException {
        checkForError(404, new ContainerMetadataCommandImpl(this.account, httpClient, defaultAccess, account.getContainer("containerName"), new ArrayList<Header>()));
    }

    @Test (expected = CommandException.class)
    public void unknownError() throws IOException {
        checkForError(500, new ContainerMetadataCommandImpl(this.account, httpClient, defaultAccess, account.getContainer("containerName"), new ArrayList<Header>()));
    }

    @Test
    public void isSecure() throws IOException {
        isSecure(new ContainerMetadataCommandImpl(this.account, httpClient, defaultAccess, account.getContainer("containerName"), new ArrayList<Header>()), 204);
    }
}
