package org.javaswift.joss.instructions;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class QueryParametersTest {

    @Test
    public void getQueryWithValidValues() {
        QueryParameters queryParameters = new QueryParameters(new QueryParameter[] {
                new QueryParameter("marker", "dog"),
                new QueryParameter("limit", 10)
        });
        assertEquals("?marker=dog&limit=10", queryParameters.getQuery());
    }

    @Test
    public void getQueryWithMultipleValues() {
        QueryParameters queryParameters = new QueryParameters(new QueryParameter[] {
                new QueryParameter("alpha", 1),
                new QueryParameter("beta", 2),
                new QueryParameter("gamma", 3),
                new QueryParameter("delta", 4),
                new QueryParameter("epsilon", 5)
        });
        assertEquals("?alpha=1&beta=2&gamma=3&delta=4&epsilon=5", queryParameters.getQuery());
    }

    @Test
    public void getUrl() {
        QueryParameters queryParameters = new QueryParameters(new QueryParameter[] {
                new QueryParameter("marker", "dog"),
                new QueryParameter("limit", 10)
        });
        String url = "http://www.nowhere.not";
        assertEquals(url+"?marker=dog&limit=10", queryParameters.createUrl(url));
    }

}
