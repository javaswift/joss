package org.javaswift.joss.information;

import org.javaswift.joss.headers.Header;
import org.javaswift.joss.headers.object.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

public class ObjectInformation extends AbstractInformation {

    private ObjectLastModified lastModified;
    private Etag etag;
    private ObjectContentLength contentLength;
    private ObjectContentType contentType;
    private DeleteAfter deleteAfter;
    private DeleteAt deleteAt;

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

    public void setDeleteAt(DeleteAt deleteAt) {
        this.deleteAt = deleteAt;
    }

    public DeleteAt getDeleteAt() {
        return this.deleteAt;
    }

    public void setDeleteAfter(DeleteAfter deleteAfter) {
        this.deleteAfter = deleteAfter;
    }

    public DeleteAfter getDeleteAfter() {
        return this.deleteAfter;
    }

    public void addHeader(Collection<Header> headers, Header header) {
        if (header == null) {
            return;
        }
        headers.add(header);
    }

    public Collection<Header> getHeaders() {
        Collection<Header> headers = new ArrayList<Header>();
        addHeader(headers, getDeleteAfter());
        addHeader(headers, getDeleteAt());
        headers.addAll(getMetadata()); // The original metadata must be passed as well, otherwise it's deleted
        return headers;
    }

    public Collection<Header> getHeadersIncludingHeader(Header header) {
        Collection<Header> headers = getHeaders();
        addHeader(headers, header);
        return headers;
    }
}
