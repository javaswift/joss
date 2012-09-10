package nl.tweeenveertig.openstack.command.account;

import nl.tweeenveertig.openstack.command.core.BaseCommandTest;
import nl.tweeenveertig.openstack.command.core.CommandExceptionError;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
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
        Map<String, Object> metadata = new TreeMap<String, Object>();
        metadata.put("Year", 1989);
        metadata.put("Company", "42 BV");
        new AccountMetadataCommand(httpClient, defaultAccess, metadata).execute();
    }

    @Test
    public void unknownError() throws IOException {
        checkForError(500, new AccountMetadataCommand(httpClient, defaultAccess, new TreeMap<String, Object>()), CommandExceptionError.UNKNOWN);
    }
}
