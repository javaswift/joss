package org.javaswift.joss.client.core;

import org.javaswift.joss.command.shared.factory.StoredObjectCommandFactory;
import org.javaswift.joss.exception.CommandException;
import org.javaswift.joss.headers.Metadata;
import org.javaswift.joss.headers.object.*;
import org.javaswift.joss.information.ObjectInformation;
import org.javaswift.joss.instructions.DownloadInstructions;
import org.javaswift.joss.instructions.UploadInstructions;
import org.javaswift.joss.model.Account;
import org.javaswift.joss.model.Container;
import org.javaswift.joss.model.StoredObject;

import java.io.File;
import java.io.InputStream;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class AbstractStoredObject extends AbstractObjectStoreEntity<ObjectInformation> implements StoredObject {

    protected String name;

    private Container container;

    private final StoredObjectCommandFactory commandFactory;

    public AbstractStoredObject(StoredObjectCommandFactory commandFactory, Container container, String name, boolean allowCaching) {
        super(allowCaching);
        this.commandFactory = commandFactory;
        this.container = container;
        this.name = name;
        this.info = new ObjectInformation();
    }

    public void metadataSetFromHeaders() {
        this.staleHeaders = false;
    }

    public Date getLastModifiedAsDate() {
        checkForInfoAndAllowHeaderSet();
        return info.getLastModifiedAsDate();
    }

    public String getLastModified() {
        checkForInfoAndAllowHeaderSet();
        return info.getLastModified();
    }

    public String getEtag() {
        checkForInfoAndAllowHeaderSet();
        return info.getEtag();
    }

    public long getContentLength() {
        checkForInfoAndAllowHeaderSet();
        return info.getContentLength();
    }

    public String getContentType() {
        checkForInfoAndAllowHeaderSet();
        return info.getContentType();
    }

    public Date getDeleteAtAsDate() {
        checkForInfo();
        return info.getDeleteAt() == null ? null : info.getDeleteAt().getDate();
    }

    public String getDeleteAt() {
        checkForInfo();
        return info.getDeleteAt() == null ? null : info.getDeleteAt().getHeaderValue();
    }

    public String getName() {
        return name;
    }

    public Container getContainer() {
        return container;
    }

    @Override
    public String getPublicURL() {
        return getContainer().getAccount().getPublicURL() + "/" + getPath();
    }

    public String getPath() {
        try {
            return URLEncoder.encode(getContainer().getName(), "UTF-8") + "/" + URLEncoder.encode(getName(), "UTF-8");
        } catch (Exception e) {
            throw new CommandException("Unable to encode the object path: "+getContainer().getName()+"/"+getName());
        }
    }

    public void setLastModified(Date date) {
        this.info.setLastModified(new ObjectLastModified(date));
    }

    public void setLastModified(String date) {
        try {
            // The LastModified date in the JSON body differs from that in the response header
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            setLastModified(formatter.parse(date));
        } catch (ParseException e) {
            throw new CommandException("Unable to convert date string: "+date, e);
        }
    }

    public void setEtag(String etag) {
        this.info.setEtag(new Etag(etag));
    }

    public void setContentLength(long contentLength) {
        this.info.setContentLength(new ObjectContentLength(Long.toString(contentLength)));
    }

    public void setContentTypeWithoutSaving(String contentType) {
        this.info.setContentType(new ObjectContentType(contentType));
    }

    public int hashCode() {
        return getName().hashCode();
    }

    public void uploadObject(UploadInstructions uploadInstructions) {
        if (uploadInstructions.requiresSegmentation()) {
            uploadObjectAsSegments(uploadInstructions);
        } else {
            directlyUploadObject(uploadInstructions);
        }
        invalidate();
    }

    public void uploadObjectAsSegments(UploadInstructions uploadInstructions) {
        ((AbstractContainer)getContainer()).uploadSegmentedObjects(getName(), uploadInstructions);
        // The manifest file is the handle which allows the ObjectStore to piece the segments together as one file
        UploadInstructions manifest = new UploadInstructions(new byte[] {})
                .setObjectManifest(new ObjectManifest(getPath()))
                .setContentType(uploadInstructions.getContentType());
        uploadObject(manifest);
    }

    @SuppressWarnings("ConstantConditions")
    public boolean equals(Object o) {
        return o instanceof StoredObject && compareTo((StoredObject) o) == 0;
    }

    @SuppressWarnings("ConstantConditions")
    public int compareTo(StoredObject o) {
        int compareValue = getName().compareTo(o.getName());
        if (compareValue == 0) {
            compareValue = getContainer().compareTo(((AbstractStoredObject) o).getContainer());
        }
        return compareValue;
    }

    protected Metadata createMetadataEntry(String name, String value) {
        return new ObjectMetadata(name, value);
    }

    public InputStream downloadObjectAsInputStream() {
        return downloadObjectAsInputStream(new DownloadInstructions());
    }

    public InputStream downloadObjectAsInputStream(DownloadInstructions downloadInstructions) {
        return commandFactory.createDownloadObjectAsInputStreamCommand(getAccount(), getContainer(), this, downloadInstructions).call();
    }

    public byte[] downloadObject() {
        return downloadObject(new DownloadInstructions());
    }

    public byte[] downloadObject(DownloadInstructions downloadInstructions) {
        return commandFactory.createDownloadObjectAsByteArrayCommand(getAccount(), getContainer(),this, downloadInstructions).call();
    }

    public void downloadObject(File targetFile) {
        downloadObject(targetFile, new DownloadInstructions());
    }

    public void downloadObject(File targetFile, DownloadInstructions downloadInstructions) {
        commandFactory.createDownloadObjectToFileCommand(getAccount(), getContainer(),this, downloadInstructions, targetFile).call();
    }

    public void directlyUploadObject(UploadInstructions uploadInstructions) {
        commandFactory.createUploadObjectCommand(getAccount(), getContainer(), this, uploadInstructions).call();
    }

    public void uploadObject(InputStream inputStream) {
        uploadObject(new UploadInstructions(inputStream));
    }

    public void uploadObject(byte[] fileToUpload) {
        uploadObject(new UploadInstructions(fileToUpload));
    }

    public void uploadObject(File fileToUpload) {
        uploadObject(new UploadInstructions(fileToUpload));
    }

    public void delete() {
        commandFactory.createDeleteObjectCommand(getAccount(), getContainer(), this).call();
    }

    public void copyObject(Container targetContainer, StoredObject targetObject) {
        commandFactory.createCopyObjectCommand(
                getAccount(),
                getContainer(), this,
                ((AbstractStoredObject)targetObject).getContainer(), targetObject).call();
    }

    public StoredObject setContentType(String contentType) {
        checkForInfo();
        info.setContentType(new ObjectContentType(contentType));
        commandFactory.createObjectMetadataCommand(
                getAccount(), getContainer(), this, info.getHeadersIncludingHeader(info.getContentTypeHeader())).call();
        return this;
    }

    public StoredObject setDeleteAfter(long seconds) {
        checkForInfo();
        info.setDeleteAt(null);
        info.setDeleteAfter(new DeleteAfter(seconds));
        commandFactory.createObjectMetadataCommand(
                getAccount(), getContainer(), this, info.getHeadersIncludingHeader(info.getDeleteAfter())).call();
        return this;
    }

    @Override
    public StoredObject setDeleteAt(Date date) {
        checkForInfo();
        info.setDeleteAt(new DeleteAt(date));
        saveMetadata();
        return this;
    }

    protected Account getAccount() {
        return getContainer().getAccount();
    }

    @Override
    protected void saveMetadata() {
        commandFactory.createObjectMetadataCommand(getAccount(), getContainer(), this, info.getHeaders()).call();
    }

    protected void getInfo(boolean allowErrorLog) {
        this.info = commandFactory.createObjectInformationCommand(getAccount(), getContainer(), this, allowErrorLog).call();
        this.setInfoRetrieved();
    }

}
