package org.javaswift.joss.headers;

import org.apache.http.HttpResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class HeaderTest {

    private String headerName = "some-header";

    @Mock
    private HttpResponse response;

    @Test
    public void noHeadersInResponse() {
        when(response.getHeaders(headerName)).thenReturn(null);
        assertNull(Header.convertResponseHeader(response, headerName));
    }

    @Test
    public void headersWithLengthZero() {
        when(response.getHeaders(headerName)).thenReturn(new org.apache.http.Header[] {});
        assertNull(Header.convertResponseHeader(response, headerName));
    }

    @Test
    public void headerWithValue() {
        org.apache.http.Header header = mock(org.apache.http.Header.class);
        when(header.getValue()).thenReturn("alpha");
        when(response.getHeaders(headerName)).thenReturn(new org.apache.http.Header[] { header });
        assertEquals("alpha", Header.convertResponseHeader(response, headerName));
    }

}
