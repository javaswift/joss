package org.javaswift.joss.instructions;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class QueryParameterTest {

    @Test
    public void getQueryWithValueNotNull() {
        QueryParameter qp = new QueryParameter("marker", "dog");
        assertEquals("marker=dog", qp.getQuery());
    }

    @Test
    public void getQueryWithValueNull() {
        QueryParameter qp = new QueryParameter("marker", null);
        assertEquals(null, qp.getQuery());
    }

}
