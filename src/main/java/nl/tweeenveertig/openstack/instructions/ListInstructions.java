package nl.tweeenveertig.openstack.instructions;

public class ListInstructions {

    public static final String MARKER_NAME = "marker";

    public static final String LIMIT_NAME = "limit";

    private String marker;

    private Integer limit;

    public ListInstructions setMarker(String marker) {
        this.marker = marker;
        return this;
    }

    public ListInstructions setLimit(Integer limit) {
        this.limit = limit;
        return this;
    }

    public QueryParameters getQueryParameters() {
        return new QueryParameters(new QueryParameter[] {
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
}
