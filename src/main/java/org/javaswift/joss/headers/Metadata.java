package org.javaswift.joss.headers;

public abstract class Metadata extends SimpleHeader {

    private String name;

    public Metadata(String name, String value) {
        super(value);
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

}
