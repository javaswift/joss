package nl.tweeenveertig.openstack.client.impl;

import nl.tweeenveertig.openstack.client.Container;
import nl.tweeenveertig.openstack.client.StoredObject;
import nl.tweeenveertig.openstack.client.core.AbstractStoredObject;
import nl.tweeenveertig.openstack.command.container.ContainerMetadataCommand;
import nl.tweeenveertig.openstack.command.identity.access.Access;
import nl.tweeenveertig.openstack.command.object.*;
import org.apache.http.client.HttpClient;

import java.io.File;
import java.io.InputStream;

public class StoredObjectImpl extends AbstractStoredObject {

    public StoredObjectImpl(Container container, String name) {
        super(container, name);
    }

    public InputStream downloadObjectAsInputStream() {
        return new DownloadObjectAsInputStreamCommand(getClient(), getAccess(), getContainer(), this).call();
    }

    public byte[] downloadObject() {
        return new DownloadObjectAsByteArrayCommand(getClient(), getAccess(), getContainer(), this).call();
    }

    public void downloadObject(File targetFile) {
        new DownloadObjectToFileCommand(getClient(), getAccess(), getContainer(), this, targetFile).call();
    }

    public void uploadObject(InputStream inputStream) {
        new UploadObjectCommand(getClient(), getAccess(), getContainer(), this, inputStream).call();
    }

    public void uploadObject(byte[] fileToUpload) {
        new UploadObjectCommand(getClient(), getAccess(), getContainer(), this, fileToUpload).call();
    }

    public void uploadObject(File fileToUpload) {
        new UploadObjectCommand(getClient(), getAccess(), getContainer(), this, fileToUpload).call();
    }

    public void delete() {
        new DeleteObjectCommand(getClient(), getAccess(), getContainer(), this).call();
    }

    public void copyObject(Container targetContainer, StoredObject targetObject) {
        new CopyObjectCommand(getClient(), getAccess(), getContainer(), this, targetContainer, targetObject).call();
    }

    public String getPublicURL() {
        return getAccess().getPublicURL() + "/" + getContainer().getName() + "/" + getName();
    }

    protected HttpClient getClient() {
        return ((ContainerImpl)getContainer()).getClient();
    }

    protected Access getAccess() {
        return ((ContainerImpl)getContainer()).getAccess();
    }

    @Override
    protected void saveMetadata() {
        new ObjectMetadataCommand(getClient(), getAccess(), getContainer(), this, getMetadataWithoutTriggeringCheck()).call();
    }

    protected void getInfo() {
        ObjectInformation info = new ObjectInformationCommand(getClient(), getAccess(), getContainer(), this).call();
        this.lastModified = info.getLastModified();
        this.etag = info.getEtag();
        this.contentLength = info.getContentLength();
        this.contentType = info.getContentType();
        this.setMetadata(info.getMetadata());
        this.setInfoRetrieved();
    }

}
