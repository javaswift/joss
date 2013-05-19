package org.javaswift.joss.headers;

import org.apache.commons.lang.WordUtils;

public abstract class Metadata extends SimpleHeader {

    private String name;

    public Metadata(String name, String value) {
        super(value);
        name = name.replaceAll("_", "-");
        this.name = WordUtils.capitalize(name, new char[] { '-' } );
    }

    public String getName() {
        return this.name;
    }

}
