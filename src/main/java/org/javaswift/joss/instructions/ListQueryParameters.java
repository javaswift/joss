package org.javaswift.joss.instructions;

public class ListQueryParameters extends QueryParameters {

    public ListQueryParameters(QueryParameter[] queryParameters) {
        super(queryParameters);
        this.queryParameters.add(new QueryParameter("format", "json"));
    }

}
