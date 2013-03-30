package org.javaswift.joss.swift;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpStatus;
import org.javaswift.joss.headers.Header;
import org.javaswift.joss.headers.object.*;
import org.javaswift.joss.information.ObjectInformation;
import org.javaswift.joss.instructions.DownloadInstructions;
import org.javaswift.joss.instructions.UploadInstructions;
import org.javaswift.joss.model.ListSubject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.activation.MimetypesFileTypeMap;
import java.io.*;
import java.util.Collection;
import java.util.Date;

public class SwiftStoredObject implements Comparable<SwiftStoredObject>, ListSubject {

    public static final Logger LOG = LoggerFactory.getLogger(SwiftStoredObject.class);

    private String name;

    private byte[] content;

    private Etag etag;

    private ObjectContentType contentType;

    private Date lastModified;

    private ObjectManifest objectManifest;

    private HeaderStore headers = new HeaderStore();

    public SwiftStoredObject(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof SwiftStoredObject)) {
            return false;
        }
        return this.name.equals(((SwiftStoredObject) obj).name);
    }

    @Override
    public int hashCode() {
        return this.name.hashCode();
    }

    @Override
    public int compareTo(SwiftStoredObject column) {
        return this.name.compareTo(column.name);
    }

    public long getBytesUsed() {
        return content.length;
    }

    public Etag getEtag() {
        return this.etag;
    }

    public ObjectContentType getContentType() {
        return this.contentType;
    }

    public Date getLastModified() {
        return this.lastModified;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void metadataSetFromHeaders() { /* not used */ }

    public ObjectInformation getInfo() {
        ObjectInformation objectInformation = new ObjectInformation();
        objectInformation.setMetadata(headers.getMetadata());
        objectInformation.setContentLength(new ObjectContentLength(Long.toString(content.length)));
        objectInformation.setContentType(contentType);
        objectInformation.setEtag(etag);
        objectInformation.setLastModified(new ObjectLastModified(lastModified));
        return objectInformation;
    }

    public SwiftResult<Object> uploadObject(UploadInstructions uploadInstructions) {
        try {
            this.lastModified = new Date();
            this.content = IOUtils.toByteArray(uploadInstructions.getEntity().getContent());
            this.objectManifest = uploadInstructions.getObjectManifest();
            this.etag = new Etag(uploadInstructions.getMd5() != null ?
                        uploadInstructions.getMd5() :
                        DigestUtils.md5Hex(content));
            this.contentType =
                    uploadInstructions.getContentType() != null ?
                        uploadInstructions.getContentType() :
                        new ObjectContentType(new MimetypesFileTypeMap().getContentType(getName()));
            return new SwiftResult<Object>(HttpStatus.SC_CREATED);
        } catch (IOException err) {
            return new SwiftResult<Object>(HttpStatus.SC_UNPROCESSABLE_ENTITY);
        }
    }

    public SwiftResult<Object> copyFrom(SwiftStoredObject sourceObject) {
        UploadInstructions uploadInstructions = new UploadInstructions(sourceObject.getContent().clone());
        uploadInstructions.setContentType(sourceObject.getContentType().getHeaderValue());
        uploadObject(uploadInstructions);
        return new SwiftResult<Object>(HttpStatus.SC_CREATED);
    }

    public SwiftResult<byte[]> downloadObject(DownloadInstructions downloadInstructions) {
        byte[] object = this.content;
        if (downloadInstructions.getRange() != null) {
            return new SwiftResult<byte[]>(downloadInstructions.getRange().copy(object), HttpStatus.SC_PARTIAL_CONTENT);
        }
        if (downloadInstructions.getMatchConditional() != null) {
            downloadInstructions.getMatchConditional().matchAgainst(etag.getHeaderValue());
        }
        if (downloadInstructions.getSinceConditional() != null) {
            downloadInstructions.getSinceConditional().sinceAgainst(lastModified);
        }
        return new SwiftResult<byte[]>(object, HttpStatus.SC_OK);
    }

    public SwiftResult<Object> saveMetadata(Collection<? extends Header> headers) {
        this.headers.clear();
        for (Header header : headers) {
            this.headers.put(header);
        }
        return new SwiftResult<Object>(HttpStatus.SC_ACCEPTED);
    }

    public byte[] getContent() {
        return this.content;
    }

    public ObjectManifest getObjectManifest() {
        return this.objectManifest;
    }

    public void addHeader(Header header) {
        this.headers.addHeader(header);
    }

}
