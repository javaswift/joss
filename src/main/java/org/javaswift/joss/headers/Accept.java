package org.javaswift.joss.headers;

public class Accept extends SimpleHeader {
    public static final String ACCEPT = "Accept";

    public Accept(String value) {
        super(value);
    }

    @Override
    public String getHeaderName() {
        return ACCEPT;
    }

}
