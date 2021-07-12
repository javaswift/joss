package org.javaswift.joss.headers;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;

import mockit.Expectations;
import mockit.Mocked;
import mockit.NonStrictExpectations;

import org.apache.http.HttpResponse;
import org.apache.http.message.BasicHeader;
import org.junit.Test;

import java.util.List;

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

    @Test
    public void getHeadersWithHeaderNameMatchingPrefixCase() {

        String headerValue = "34567as7dta8asd7";
        String headerName = "x-account-meta-temp-url-key";
        String searchString = "x-account-meta-";
        final org.apache.http.Header myHeader = new BasicHeader(headerName, headerValue);

        new Expectations() {{
            response.getAllHeaders();
            result = new org.apache.http.Header[]{myHeader};
        }};
        List<org.apache.http.Header> matchedHeaders = Header.getResponseHeadersStartingWith(
                response,
                searchString
        );
        assertEquals(1, matchedHeaders.size());
        assertEquals(headerName, matchedHeaders.get(0).getName());
        assertEquals(headerValue, matchedHeaders.get(0).getValue());

    }

    @Test
    public void getHeadersWithHeaderNameNotMatchingPrefixCase() {

        String headerValue = "34567as7dta8asd7";
        String headerName = "x-account-meta-temp-url-key";
        String searchString = "X-Account-Meta-";
        final org.apache.http.Header myHeader = new BasicHeader(headerName, headerValue);

        new Expectations() {{
            response.getAllHeaders();
            result = new org.apache.http.Header[]{myHeader};
        }};
        List<org.apache.http.Header> matchedHeaders = Header.getResponseHeadersStartingWith(
                response,
                searchString
        );
        assertEquals(1, matchedHeaders.size());
        assertEquals(headerName, matchedHeaders.get(0).getName());
        assertEquals(headerValue, matchedHeaders.get(0).getValue());
    }

    @Test
    public void getHeadersWithNullPrefix() {

        String searchString = null;
        List<org.apache.http.Header> matchedHeaders = Header.getResponseHeadersStartingWith(
                response,
                searchString
        );
        assertEquals(0, matchedHeaders.size());
    }
}
