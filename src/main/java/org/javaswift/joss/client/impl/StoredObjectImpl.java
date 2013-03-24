package org.javaswift.joss.client.impl;

import org.javaswift.joss.command.impl.factory.StoredObjectCommandFactoryImpl;
import org.javaswift.joss.command.shared.factory.StoredObjectCommandFactory;
import org.javaswift.joss.command.shared.identity.access.AccessImpl;
import org.javaswift.joss.instructions.DownloadInstructions;
import org.javaswift.joss.instructions.UploadInstructions;
import org.javaswift.joss.model.Container;
import org.javaswift.joss.headers.object.DeleteAfter;
import org.javaswift.joss.headers.object.DeleteAt;
import org.javaswift.joss.headers.object.ObjectContentType;
import org.javaswift.joss.model.StoredObject;
import org.javaswift.joss.client.core.AbstractStoredObject;
import org.apache.http.client.HttpClient;

import java.io.File;
import java.io.InputStream;
import java.util.Date;

public class StoredObjectImpl extends AbstractStoredObject {

    private StoredObjectCommandFactory commandFactory = new StoredObjectCommandFactoryImpl();

    public StoredObjectImpl(Container container, String name, boolean allowCaching) {
        super(container, name, allowCaching);
    }

    public InputStream downloadObjectAsInputStream() {
        return downloadObjectAsInputStream(new DownloadInstructions());
    }

    public InputStream downloadObjectAsInputStream(DownloadInstructions downloadInstructions) {
        return commandFactory.createDownloadObjectAsInputStreamCommand(getAccount(), getClient(), getAccess(), this, downloadInstructions).call();
    }

    public byte[] downloadObject() {
        return downloadObject(new DownloadInstructions());
    }

    public byte[] downloadObject(DownloadInstructions downloadInstructions) {
        return commandFactory.createDownloadObjectAsByteArrayCommand(getAccount(), getClient(), getAccess(), this, downloadInstructions).call();
    }

    public void downloadObject(File targetFile) {
        downloadObject(targetFile, new DownloadInstructions());
    }

    public void downloadObject(File targetFile, DownloadInstructions downloadInstructions) {
        commandFactory.createDownloadObjectToFileCommand(getAccount(), getClient(), getAccess(), this, downloadInstructions, targetFile).call();
    }

    public void directlyUploadObject(UploadInstructions uploadInstructions) {
        commandFactory.createUploadObjectCommand(getAccount(), getClient(), getAccess(), this, uploadInstructions).call();
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
        commandFactory.createDeleteObjectCommand(getAccount(), getClient(), getAccess(), this).call();
    }

    public void copyObject(Container targetContainer, StoredObject targetObject) {
        commandFactory.createCopyObjectCommand(getAccount(), getClient(), getAccess(), this, targetObject).call();
    }

    public StoredObject setContentType(String contentType) {
        checkForInfo();
        info.setContentType(new ObjectContentType(contentType));
        commandFactory.createObjectMetadataCommand(
                getAccount(), getClient(), getAccess(), this,
                info.getHeadersIncludingHeader(info.getContentTypeHeader())).call();
        return this;
    }

    public StoredObject setDeleteAfter(long seconds) {
        checkForInfo();
        info.setDeleteAt(null);
        info.setDeleteAfter(new DeleteAfter(seconds));
        commandFactory.createObjectMetadataCommand(
                getAccount(), getClient(), getAccess(), this,
                info.getHeadersIncludingHeader(info.getDeleteAfter())).call();
        return this;
    }

    @Override
    public StoredObject setDeleteAt(Date date) {
        checkForInfo();
        info.setDeleteAt(new DeleteAt(date));
        saveMetadata();
        return this;
    }

    protected AccountImpl getAccount() {
        return (AccountImpl)getContainer().getAccount();
    }

    @Override
    public ContainerImpl getContainer() {
        return (ContainerImpl)super.getContainer();
    }

    protected HttpClient getClient() {
        return getContainer().getClient();
    }

    protected AccessImpl getAccess() {
        return getContainer().getAccess();
    }

    @Override
    protected void saveMetadata() {
        commandFactory.createObjectMetadataCommand(getAccount(), getClient(), getAccess(), this, info.getHeaders()).call();
    }

    protected void getInfo() {
        this.info = commandFactory.createObjectInformationCommand(getAccount(), getClient(), getAccess(), this).call();
        this.setInfoRetrieved();
    }

}
