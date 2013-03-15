package org.javaswift.joss.instructions;

public class ListInstructions {

    public static final String MARKER_NAME = "marker";

    public static final String LIMIT_NAME = "limit";

    public static final String PREFIX_NAME = "prefix";

    private String marker;

    private Integer limit;

    private String prefix;

    public ListInstructions setMarker(String marker) {
        this.marker = marker;
        return this;
    }

    public ListInstructions setLimit(Integer limit) {
        this.limit = limit;
        return this;
    }

    public ListInstructions setPrefix(String prefix) {
        this.prefix = prefix;
        return this;
    }

    public QueryParameters getQueryParameters() {
        return new ListQueryParameters(new QueryParameter[] {
            new QueryParameter(PREFIX_NAME, getPrefix()),
            new QueryParameter(MARKER_NAME, getMarker()),
            new QueryParameter(LIMIT_NAME, getLimit())
        });
    }

    public String getMarker() {
        return marker;
    }

    public Integer getLimit() {
        return limit;
    }

    public String getPrefix() {
        return prefix;
    }

}
