package org.javaswift.joss.headers;

import org.apache.commons.lang.WordUtils;

public abstract class Metadata extends SimpleHeader {

    private String name;

    public Metadata(String name, String value) {
        super(value);
        this.name = capitalize(name);
    }

    public String getName() {
        return this.name;
    }

    public static String capitalize(String key) {
        key = key.replaceAll("_", "-");
        return WordUtils.capitalize(key, new char[]{'-'});
    }

}
