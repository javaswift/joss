package nl.tweeenveertig.openstack.command.account;

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
    }

    @Test
    public void unknownError() throws IOException {
        checkForError(500, new AccountMetadataCommand(this.account, httpClient, defaultAccess, new ArrayList<Header>()), CommandExceptionError.UNKNOWN);
    }
}
