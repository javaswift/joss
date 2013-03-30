package org.javaswift.joss.instructions;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@RunWith(PowerMockRunner.class)
@PrepareForTest( { QueryParameter.class, URLEncoder.class } )
public class QueryParameterTest {

    @Test
    public void getQueryWithValueNotNull() {
        QueryParameter qp = new QueryParameter("marker", "dog");
        assertEquals("marker=dog", qp.getQuery());
    }

    @Test
    public void getQueryWithEncodedValue() {
        QueryParameter qp = new QueryParameter("märker", "a \"Rose\" by any other Name");
        assertEquals("m%C3%A4rker=a+%22Rose%22+by+any+other+Name", qp.getQuery());
    }

    @Test
    public void getQueryWithValueNull() {
        QueryParameter qp = new QueryParameter("marker", null);
        assertEquals(null, qp.getQuery());
    }

    @Test(expected = IllegalArgumentException.class)
    public void encodingThrowsException() throws UnsupportedEncodingException {
        mockStatic(URLEncoder.class);
        when(URLEncoder.encode(any(String.class), any(String.class))).thenThrow(new UnsupportedEncodingException());
        QueryParameter qp = new QueryParameter("abc", "def");
        qp.getQuery();
    }

}
