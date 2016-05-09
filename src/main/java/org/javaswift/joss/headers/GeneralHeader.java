package org.javaswift.joss.headers;

public class GeneralHeader extends Header {

    public String Name;
    public String Value;

    @Override
    public String getHeaderValue() {
        return Value;
    }

    @Override
    public String getHeaderName() {
        return Name;
    }

    public GeneralHeader(String Name, String Value) {
        this.Name = Name;
        this.Value = Value;
    }
}
