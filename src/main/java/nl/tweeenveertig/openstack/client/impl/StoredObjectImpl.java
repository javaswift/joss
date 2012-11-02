package nl.tweeenveertig.openstack.client.impl;

import nl.tweeenveertig.openstack.client.Container;
import nl.tweeenveertig.openstack.headers.object.DeleteAfter;
import nl.tweeenveertig.openstack.headers.object.DeleteAt;
import nl.tweeenveertig.openstack.headers.object.ObjectContentType;
import nl.tweeenveertig.openstack.headers.object.ObjectManifest;
import nl.tweeenveertig.openstack.model.DownloadInstructions;
import nl.tweeenveertig.openstack.model.UploadInstructions;
import nl.tweeenveertig.openstack.client.StoredObject;
import nl.tweeenveertig.openstack.client.core.AbstractStoredObject;
import nl.tweeenveertig.openstack.command.identity.access.Access;
import nl.tweeenveertig.openstack.command.object.*;
import org.apache.http.client.HttpClient;

import java.io.File;
import java.io.InputStream;
import java.util.Date;

public class StoredObjectImpl extends AbstractStoredObject {

    public StoredObjectImpl(Container container, String name) {
        super(container, name);
    }

    public InputStream downloadObjectAsInputStream() {
        return downloadObjectAsInputStream(new DownloadInstructions());
    }

    public InputStream downloadObjectAsInputStream(DownloadInstructions downloadInstructions) {
        return new DownloadObjectAsInputStreamCommand(getAccount(), getClient(), getAccess(), getContainer(), this, downloadInstructions).call();
    }

    public byte[] downloadObject() {
        return downloadObject(new DownloadInstructions());
    }

    public byte[] downloadObject(DownloadInstructions downloadInstructions) {
        return new DownloadObjectAsByteArrayCommand(getAccount(), getClient(), getAccess(), getContainer(), this, downloadInstructions).call();
    }

    public void downloadObject(File targetFile) {
        downloadObject(targetFile, new DownloadInstructions());
    }

    public void downloadObject(File targetFile, DownloadInstructions downloadInstructions) {
        new DownloadObjectToFileCommand(getAccount(), getClient(), getAccess(), getContainer(), this, downloadInstructions, targetFile).call();
    }

    public void uploadObject(UploadInstructions uploadInstructions) {
        if (uploadInstructions.requiresSegmentation()) {
            getContainer().uploadSegmentedObjects(uploadInstructions);
        } else {
            new UploadObjectCommand(getAccount(), getClient(), getAccess(), getContainer(), this, uploadInstructions).call();
        }
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
        new DeleteObjectCommand(getAccount(), getClient(), getAccess(), getContainer(), this).call();
    }

    public void copyObject(Container targetContainer, StoredObject targetObject) {
        new CopyObjectCommand(getAccount(), getClient(), getAccess(), getContainer(), this, targetContainer, targetObject).call();
    }

    public StoredObject setContentType(String contentType) {
        checkForInfo();
        info.setContentType(new ObjectContentType(contentType));
        new ObjectMetadataCommand(
                getAccount(), getClient(), getAccess(), getContainer(),
                this, info.getHeadersIncludingHeader(info.getContentTypeHeader())).call();
        return this;
    }

    public StoredObject setDeleteAfter(long seconds) {
        checkForInfo();
        info.setDeleteAt(null);
        info.setDeleteAfter(new DeleteAfter(seconds));
        new ObjectMetadataCommand(
                getAccount(), getClient(), getAccess(), getContainer(),
                this, info.getHeadersIncludingHeader(info.getDeleteAfter())).call();
        return this;
    }

    @Override
    public StoredObject setDeleteAt(Date date) {
        checkForInfo();
        info.setDeleteAt(new DeleteAt(date));
        new ObjectMetadataCommand(
                getAccount(), getClient(), getAccess(), getContainer(), this, info.getHeaders()).call();
        return this;
    }

    public String getPublicURL() {
        return getAccess().getPublicURL() + "/" + getPath();
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

    protected Access getAccess() {
        return getContainer().getAccess();
    }

    @Override
    protected void saveMetadata() {
        new ObjectMetadataCommand(getAccount(), getClient(), getAccess(), getContainer(), this, info.getHeaders()).call();
    }

    protected void getInfo() {
        this.info = new ObjectInformationCommand(getAccount(), getClient(), getAccess(), getContainer(), this).call();
        this.setInfoRetrieved();
    }

}
