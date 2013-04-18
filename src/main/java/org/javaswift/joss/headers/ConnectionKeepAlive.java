package org.javaswift.joss.headers;

public class ConnectionKeepAlive extends Header {

    public static String CONNECTION = "CONNECTION";

    @Override
    public String getHeaderValue() {
        return "Keep-Alive";
    }

    @Override
    public String getHeaderName() {
        return CONNECTION;
    }
}
