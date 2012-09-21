package nl.tweeenveertig.openstack.headers.metadata;

import nl.tweeenveertig.openstack.headers.Header;

public abstract class Metadata extends Header {

    private String name;

    private String value;

    public Metadata(String name, String value) {
        this.name = name;
        this.value = value;
    }

    protected String getName() {
        return this.name;
    }

    @Override
    public String getHeaderValue() {
        return this.value;
    }

}
