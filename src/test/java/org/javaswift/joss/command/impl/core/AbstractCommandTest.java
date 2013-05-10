package org.javaswift.joss.command.impl.core;

import mockit.Expectations;
import org.apache.http.client.methods.HttpRequestBase;
import org.javaswift.joss.command.impl.identity.AuthenticationCommandImpl;
import org.javaswift.joss.exception.CommandException;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class AbstractCommandTest extends BaseCommandTest {

    @Before
    public void setup() throws IOException {
        super.setup();
    }

    @Test(expected = CommandException.class)
    public void httpClientThrowsAnException() throws IOException {
        new Expectations() {{
            httpClient.execute((HttpRequestBase)any);
            result = new IOException("Mocked HTTP client error");
        }};
        new AuthenticationCommandImpl(httpClient, "http://some.url", "some-tenant", "tenant-id", "some-user", "some-pwd").call();
    }

    @Test(expected = CommandException.class)
    public void httpClientThrowsAnExceptionWithRootCause() throws IOException {
        final IOException exc = new IOException("Mocked HTTP client error");
        new Expectations() {{
            httpClient.execute((HttpRequestBase)any);
            result = new CommandException("Something went wrong", exc);
        }};
        new AuthenticationCommandImpl(httpClient, "http://some.url", "some-tenant", "tenant-id", "some-user", "some-pwd").call();
    }

}
