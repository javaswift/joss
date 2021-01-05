package org.javaswift.joss.instructions;

import static junit.framework.Assert.assertEquals;

import org.junit.Test;

public class ListQueryParametersTest {

    @Test
    public void getQueryWithValidValues() {
        ListQueryParameters queryParameters = new ListQueryParameters(new QueryParameter[] {
                new QueryParameter("marker", "dog"),
                new QueryParameter("limit", 10)
        });
        assertEquals("?marker=dog&limit=10&format=json", queryParameters.getQuery());
    }

}
