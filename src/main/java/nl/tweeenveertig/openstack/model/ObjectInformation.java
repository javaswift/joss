package nl.tweeenveertig.openstack.model;

import nl.tweeenveertig.openstack.command.core.AbstractInformation;
import nl.tweeenveertig.openstack.headers.object.Etag;
import nl.tweeenveertig.openstack.headers.object.ObjectContentLength;
import nl.tweeenveertig.openstack.headers.object.ObjectContentType;
import nl.tweeenveertig.openstack.headers.object.ObjectLastModified;

public class ObjectInformation extends AbstractInformation {

    private ObjectLastModified lastModified;
    private Etag etag;
    private ObjectContentLength contentLength;
    private ObjectContentType contentType;

    public String getLastModified() {
        return lastModified.getHeaderValue();
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

    public String getContentType() {
        return contentType.getHeaderValue();
    }

    public void setContentType(ObjectContentType contentType) {
        this.contentType = contentType;
    }
}
