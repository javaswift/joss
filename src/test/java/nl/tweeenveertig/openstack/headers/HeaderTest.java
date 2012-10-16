package nl.tweeenveertig.openstack.headers;

import org.apache.http.client.methods.HttpRequestBase;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public abstract class HeaderTest {

    @Mock
    private HttpRequestBase request;

    protected void testHeader(Header header) {
        header.setHeader(request);
        verify(request).setHeader(header.getHeaderName(), header.getHeaderValue());
    }
}