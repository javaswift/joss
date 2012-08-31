package nl.t42.openstack;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpRequestBase;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringBufferInputStream;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public abstract class BaseCommandTest {

    @Mock
    protected HttpClient httpClient;

    @Mock
    protected HttpResponse response;

    @Mock
    protected HttpEntity httpEntity;

    @Mock
    protected StatusLine statusLine;

    public void setup() throws IOException {
        InputStream inputStream = new StringBufferInputStream("");
        when(httpEntity.getContent()).thenReturn(inputStream);
        when(response.getEntity()).thenReturn(httpEntity);
        when(statusLine.getStatusCode()).thenReturn(200);
        when(response.getStatusLine()).thenReturn(statusLine);
        when(httpClient.execute(any(HttpRequestBase.class))).thenReturn(response);
    }

}
