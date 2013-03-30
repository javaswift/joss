package org.javaswift.joss.swift;

import org.javaswift.joss.headers.Header;
import org.javaswift.joss.headers.Metadata;

import java.util.Map;
import java.util.TreeMap;

public class HeaderStore {

    private Map<String, Header> headers = new TreeMap<String, Header>();

    public void addHeader(Header header) {
        this.headers.put(header.getHeaderName(), header);
    }

    public Map<String, Metadata> getMetadata() {
        Map<String, Metadata> metadata = new TreeMap<String, Metadata>();
        for (Header header : headers.values()) {
            if (header instanceof Metadata) {
                metadata.put(header.getHeaderName(), (Metadata)header);
            }
        }
        return metadata;
    }

    public void clear() {
        this.headers.clear();
    }

    public void put(Header header) {
        this.headers.put(header.getHeaderName(), header);
    }

    public Header get(String headerName) {
        return this.headers.get(headerName);
    }

}
