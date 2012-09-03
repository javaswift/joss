package nl.t42.openstack.model;

import java.util.Map;
import java.util.TreeMap;

public class ObjectInformation {

    private Map<String, String> metadata = new TreeMap<String, String>();

    private String lastModified;
    private String etag;
    private int contentLength;
    private String contentType;

    public void addMetadata(String name, String value) {
        metadata.put(name, value);
    }

    public Map<String, String> getMetadata() {
        return this.metadata;
    }

    public String getLastModified() {
        return lastModified;
    }

    public void setLastModified(String lastModified) {
        this.lastModified = lastModified;
    }

    public String getEtag() {
        return etag;
    }

    public void setEtag(String etag) {
        this.etag = etag;
    }

    public int getContentLength() {
        return contentLength;
    }

    public void setContentLength(int contentLength) {
        this.contentLength = contentLength;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
}
