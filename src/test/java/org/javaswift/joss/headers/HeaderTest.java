package org.javaswift.joss.headers;

import mockit.Expectations;
import mockit.Mocked;
import mockit.NonStrictExpectations;
import org.apache.http.HttpResponse;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;

public class HeaderTest {

    private String headerName = "some-header";

    @Mocked
    private HttpResponse response;

    @Test
    public void noHeadersInResponse() {
        new Expectations() {{
            response.getHeaders(headerName);
            result = null;
        }};
        assertNull(Header.convertResponseHeader(response, headerName));
    }

    @Test
    public void headersWithLengthZero() {
        new NonStrictExpectations() {{
            response.getHeaders(headerName);
            result = new org.apache.http.Header[] {};
        }};
        assertNull(Header.convertResponseHeader(response, headerName));
    }

    @Test
    public void headerWithValue(@Mocked final org.apache.http.Header header) {
        new NonStrictExpectations() {{
            header.getValue(); result = "alpha";
            response.getHeaders(headerName); result = new org.apache.http.Header[] { header };
        }};
        assertEquals("alpha", Header.convertResponseHeader(response, headerName));
    }

}
