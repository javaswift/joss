package org.javaswift.joss.command.impl.core;

import org.javaswift.joss.command.impl.identity.AuthenticationCommand;
import org.javaswift.joss.exception.CommandException;
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

    @Test(expected = CommandException.class)
    public void httpClientThrowsAnException() throws IOException {
        when(httpClient.execute(any(HttpRequestBase.class))).thenThrow(new IOException("Mocked HTTP client error"));
        new AuthenticationCommand(httpClient, "http://some.url", "some-tenant", "some-user", "some-pwd").call();
    }

    @Test(expected = CommandException.class)
    public void httpClientThrowsAnExceptionWithRootCause() throws IOException {
        IOException exc = new IOException("Mocked HTTP client error");

        when(httpClient.execute(any(HttpRequestBase.class))).thenThrow(new CommandException("Something went wrong", exc));
        new AuthenticationCommand(httpClient, "http://some.url", "some-tenant", "some-user", "some-pwd").call();
    }

}
