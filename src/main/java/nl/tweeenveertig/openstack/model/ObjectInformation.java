package nl.tweeenveertig.openstack.model;

import nl.tweeenveertig.openstack.headers.Header;
import nl.tweeenveertig.openstack.headers.object.Etag;
import nl.tweeenveertig.openstack.headers.object.ObjectContentLength;
import nl.tweeenveertig.openstack.headers.object.ObjectContentType;
import nl.tweeenveertig.openstack.headers.object.ObjectLastModified;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

public class ObjectInformation extends AbstractInformation {

    private ObjectLastModified lastModified;
    private Etag etag;
    private ObjectContentLength contentLength;
    private ObjectContentType contentType;

    public Date getLastModifiedAsDate() {
        return lastModified == null ? null : lastModified.getDate();
    }

    public String getLastModified() {
        return lastModified == null ? null : lastModified.getHeaderValue();
    }

    public void setLastModified(ObjectLastModified lastModified) {
        this.lastModified = lastModified;
    }

    public String getEtag() {
        return etag.getHeaderValue();
    }

    public void setEtag(Etag etag) {
        this.etag = etag;
    }

    public long getContentLength() {
        return Long.parseLong(contentLength.getHeaderValue());
    }

    public void setContentLength(ObjectContentLength contentLength) {
        this.contentLength = contentLength;
    }

    public Header getContentTypeHeader() {
        return this.contentType;
    }

    public String getContentType() {
        return contentType.getHeaderValue();
    }

    public void setContentType(ObjectContentType contentType) {
        this.contentType = contentType;
    }

    public Collection<Header> getHeadersIncludingContentType(String contentType) {
        setContentType(new ObjectContentType(contentType));
        Collection<Header> headers = new ArrayList<Header>();
        headers.add(getContentTypeHeader());
        headers.addAll(getMetadata()); // The original metadata must be passed as well, otherwise it's deleted
        return headers;
    }
}
