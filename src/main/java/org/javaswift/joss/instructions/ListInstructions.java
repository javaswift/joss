package org.javaswift.joss.instructions;

public class ListInstructions {

    public static final String MARKER_NAME = "marker";

    public static final String LIMIT_NAME = "limit";

    public static final String PREFIX_NAME = "prefix";
    
    public static final String  DELIMITER_NAME = "delimiter";

    private String marker;

    private Integer limit;

    private String prefix;
    
    private String delimiter;

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
    
    public ListInstructions setDelimiter(String delimiter) {
    	this.delimiter = delimiter;
    	return this;
    }

    public QueryParameters getQueryParameters() {
        return new ListQueryParameters(new QueryParameter[] {
            new QueryParameter(PREFIX_NAME, getPrefix()),
            new QueryParameter(MARKER_NAME, getMarker()),
            new QueryParameter(LIMIT_NAME, getLimit()),
            new QueryParameter(DELIMITER_NAME, getDelimiter())
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
    
    public String getDelimiter() {
    	return delimiter;
    }

}
