package nl.t42.openstack;

import nl.t42.openstack.util.ClasspathTemplateResource;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringBufferInputStream;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OpenStackClientTest {

    @Mock
    private HttpClient httpClient;

    @Mock
    private HttpResponse response;

    @Mock
    private HttpEntity httpEntity;

    @Mock
    private StatusLine statusLine;

    @Before
    public void setup() throws IOException {
        String jsonString = new ClasspathTemplateResource("/sample-access.json").loadTemplate();
        InputStream inputStream = new StringBufferInputStream(jsonString);
        when(httpEntity.getContent()).thenReturn(inputStream);
        when(response.getEntity()).thenReturn(httpEntity);
        when(statusLine.getStatusCode()).thenReturn(200);
        when(response.getStatusLine()).thenReturn(statusLine);
        when(httpClient.execute(any(HttpPost.class))).thenReturn(response);
    }

    @Test
    public void authenticateSuccessful() throws IOException {
        OpenStackClient openStackClient = new OpenStackClient();
        openStackClient.setHttpClient(httpClient);
        openStackClient.authenticate("user", "pwd", "someurl");
        assertTrue("OpenStack client should have been authenticated", openStackClient.isAuthenticated());
    }

    public void authenticateFail() throws IOException {
        OpenStackClient openStackClient = new OpenStackClient();
        when(statusLine.getStatusCode()).thenReturn(401);
        openStackClient.setHttpClient(httpClient);
        openStackClient.authenticate("user", "pwd", "someurl");
        assertFalse("OpenStack client should have been authenticated", openStackClient.isAuthenticated());
    }
}
