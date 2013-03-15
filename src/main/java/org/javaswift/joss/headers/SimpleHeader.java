package org.javaswift.joss.headers;

public abstract class SimpleHeader extends Header {

    private String value;

    public SimpleHeader(String value) {
        this.value = value;
    }

    @Override
    public String getHeaderValue() {
        return this.value;
    }

}
