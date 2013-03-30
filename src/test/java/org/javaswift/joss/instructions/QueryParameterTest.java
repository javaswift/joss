package org.javaswift.joss.instructions;

import static junit.framework.Assert.assertEquals;

import org.junit.Test;

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

}
