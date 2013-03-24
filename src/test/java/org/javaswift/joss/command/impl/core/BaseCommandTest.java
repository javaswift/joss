package org.javaswift.joss.command.impl.core;

import org.javaswift.joss.model.StoredObject;
import org.javaswift.joss.client.impl.AccountImpl;
import org.javaswift.joss.command.impl.identity.access.AccessImpl;
import org.javaswift.joss.headers.Token;
import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpRequestBase;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public abstract class BaseCommandTest {

    protected AccountImpl account;

    @Mock
    protected AccessImpl defaultAccess;

    @Mock
    protected HttpClient httpClient;

    @Mock
    protected HttpResponse response;

    @Mock
    protected HttpEntity httpEntity;

    @Mock
    protected StatusLine statusLine;

    protected ArgumentCaptor<HttpRequestBase> requestArgument;

    public void setup() throws IOException {
        InputStream inputStream = IOUtils.toInputStream("[]");
        when(defaultAccess.getInternalURL()).thenReturn("http://someurl.nowhere");
        when(defaultAccess.getPublicURL()).thenReturn("http://someurl.public");
        when(defaultAccess.getToken()).thenReturn("cafebabe");
        when(httpEntity.getContent()).thenReturn(inputStream);
        when(response.getEntity()).thenReturn(httpEntity);
        when(statusLine.getStatusCode()).thenReturn(200);
        when(response.getStatusLine()).thenReturn(statusLine);
        when(httpClient.execute(any(HttpRequestBase.class))).thenReturn(response);
        account = new AccountImpl(null, httpClient, defaultAccess, true);
        requestArgument = ArgumentCaptor.forClass(HttpRequestBase.class);
    }

    protected StoredObject getObject(String name) {
        return account.getContainer("container").getObject(name);
    }

    protected void checkForError(int httpStatusCode, AbstractCommand command) throws IOException {
        when(statusLine.getStatusCode()).thenReturn(httpStatusCode);
        command.call();
        fail("Should have thrown an exception");
    }

    public static void prepareHeader(HttpResponse response, String name, String value, List<Header> headers) {
        Header header = Mockito.mock(Header.class);
        when(header.getName()).thenReturn(name);
        when(header.getValue()).thenReturn(value);
        when(response.getHeaders(name)).thenReturn(new Header[] { header } );
        if (headers != null) {
            headers.add(header);
        }
    }

    protected void prepareHeader(HttpResponse response, String name, String value) {
        prepareHeader(response, name, value, null);
    }

    protected void isSecure(AbstractCommand command, int expectedOk) throws IOException {
        when(statusLine.getStatusCode()).thenReturn(expectedOk);
        command.call();
        verify(httpClient).execute(requestArgument.capture());
        assertEquals("cafebabe", requestArgument.getValue().getFirstHeader(Token.X_AUTH_TOKEN).getValue());
    }

    protected void isSecure(AbstractCommand command) throws IOException {
        isSecure(command, 200);
    }

}
