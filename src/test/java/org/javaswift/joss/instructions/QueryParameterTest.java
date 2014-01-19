package org.javaswift.joss.instructions;

import mockit.Expectations;
import mockit.Mocked;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import static junit.framework.Assert.assertEquals;

public class QueryParameterTest {

    @Test
    public void getQueryWithValueNotNull() {
        QueryParameter qp = new QueryParameter("marker", "dog");
        assertEquals("marker=dog", qp.getQuery());
    }

    @Test
    public void getQueryWithEncodedValue() {
        // 0x00E4

        QueryParameter qp = new QueryParameter("m\u00e4rker", "a \"Rose\" by any other Name");
        assertEquals("m%C3%A4rker=a%20%22Rose%22%20by%20any%20other%20Name", qp.getQuery());
    }

    @Test
    public void getQueryWithValueNull() {
        QueryParameter qp = new QueryParameter("marker", null);
        assertEquals(null, qp.getQuery());
    }

    @Test(expected = IllegalArgumentException.class)
    public void encodingThrowsException(@Mocked(stubOutClassInitialization = true) URLEncoder unused) throws UnsupportedEncodingException {
        new Expectations() {{
            URLEncoder.encode(anyString, anyString);
            result = new UnsupportedEncodingException();
        }};
        QueryParameter qp = new QueryParameter("abc", "def");
        qp.getQuery();
    }

}
