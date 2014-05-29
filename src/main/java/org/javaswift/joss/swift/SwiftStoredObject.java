package org.javaswift.joss.swift;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpStatus;
import org.javaswift.joss.headers.Header;
import org.javaswift.joss.headers.object.*;
import org.javaswift.joss.information.ObjectInformation;
import org.javaswift.joss.instructions.DownloadInstructions;
import org.javaswift.joss.instructions.UploadInstructions;
import org.javaswift.joss.model.*;
import org.javaswift.joss.util.LocalTime;

import javax.activation.MimetypesFileTypeMap;
import java.io.IOException;
import java.util.*;

public class SwiftStoredObject implements ListSubject, DirectoryOrObject {

    private String name;

    private byte[] content;

    private Etag etag;

    private ObjectContentType contentType;

    private Date lastModified;

    private ObjectManifest objectManifest;

    private HeaderStore headers = new HeaderStore();

    private DeleteAt deleteAt;

    public SwiftStoredObject(String name) {
        this.name = name;
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
        objectInformation.setDeleteAt(deleteAt);
        objectInformation.setManifest(objectManifest);
        return objectInformation;
    }

    public SwiftResult<Object> uploadObject(UploadInstructions uploadInstructions) {
        try {
            this.lastModified = LocalTime.currentDate();
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
        this.headers.saveMetadata(headers);
        return new SwiftResult<Object>(HttpStatus.SC_ACCEPTED);
    }

    public byte[] getContent() {
        return this.content;
    }

    public ObjectManifest getObjectManifest() {
        return this.objectManifest;
    }

    public void setDeleteAt(DeleteAt deleteAt) {
        this.deleteAt = deleteAt;
    }

    public static Collection<DirectoryOrObject> convertToDirectories(Container container, Collection<SwiftStoredObject> objects,
                                                                     String prefix, Character delimiter) {
        Set<DirectoryOrObject> files = new TreeSet<DirectoryOrObject>();
        for (SwiftStoredObject sourceObject : objects) {
            if (prefix != null && !sourceObject.getName().startsWith(prefix)) {
                continue;
            }
            int searchDirectoryDepth = prefix == null ? 0 : prefix.length() - prefix.replace(delimiter.toString(), "").length();
            StringTokenizer tokenizer = new StringTokenizer(sourceObject.getName(), delimiter.toString());
            StringBuilder path = new StringBuilder();
            int currentDepth = 0;
            while (tokenizer.hasMoreTokens()) {
                if (currentDepth > 0) {
                    path.append(delimiter);
                    if (currentDepth == searchDirectoryDepth + 1) {
                        files.add(new Directory(path.toString(), delimiter));
                    }
                }
                String token = tokenizer.nextToken();
                path.append(token);
                currentDepth++;
            }
            if (currentDepth == searchDirectoryDepth + 1) {
                files.add(sourceObject.copyToStoredObject(container.getObject(sourceObject.getName())));
            }
        }
        return files;
    }

    protected StoredObject copyToStoredObject(StoredObject targetObject) {
        targetObject.setContentLength(getBytesUsed());
        targetObject.setContentTypeWithoutSaving(getContentType().getHeaderValue());
        targetObject.setEtag(getEtag().getHeaderValue());
        targetObject.setLastModified(getLastModified());
        targetObject.metadataSetFromHeaders();
        return targetObject;
    }

    @Override
    public boolean isObject() {
        return true;
    }

    @Override
    public boolean isDirectory() {
        return false;
    }

    @Override
    public Directory getAsDirectory() {
        return null;
    }

    @Override
    public StoredObject getAsObject() {
        return null;
    }

    @Override
    public String getBareName() {
        return null;
    }
}
