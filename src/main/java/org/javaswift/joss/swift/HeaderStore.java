package org.javaswift.joss.swift;

import org.javaswift.joss.headers.Header;
import org.javaswift.joss.headers.Metadata;

import java.util.Collection;
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

    public void put(Header header) {
        this.headers.put(header.getHeaderName(), header);
    }

    public void remove(Header header) {
        this.headers.remove(header.getHeaderName());
    }

    public Header get(String headerName) {
        return this.headers.get(headerName);
    }

    public void saveMetadata(Collection<? extends Header> headers) {
        for (Header header : headers) {
            if (header.getHeaderValue() == null || header.getHeaderValue().equals("")) {
                remove(header);
            } else {
                put(header);
            }
        }
    }

}
