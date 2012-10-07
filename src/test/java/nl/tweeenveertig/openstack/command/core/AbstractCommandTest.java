package nl.tweeenveertig.openstack.command.core;

import nl.tweeenveertig.openstack.command.identity.AuthenticationCommand;
import nl.tweeenveertig.openstack.exception.CommandException;
import org.apache.http.client.methods.HttpRequestBase;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

public class AbstractCommandTest extends BaseCommandTest {

    @Before
    public void setup() throws IOException {
        super.setup();
    }

    @Test
    public void httpClientThrowsAnException() throws IOException {
        when(httpClient.execute(any(HttpRequestBase.class))).thenThrow(new IOException("Mocked HTTP client error"));
        try {
            new AuthenticationCommand(httpClient, "http://some.url", "some-tenant", "some-user", "some-pwd").call();
            fail("should have thrown an exception");
        } catch (CommandException err) {}
    }
}
