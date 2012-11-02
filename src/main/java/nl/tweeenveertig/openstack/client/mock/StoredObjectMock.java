package nl.tweeenveertig.openstack.client.mock;

import nl.tweeenveertig.openstack.client.Container;
import nl.tweeenveertig.openstack.client.mock.scheduled.ObjectDeleter;
import nl.tweeenveertig.openstack.exception.HttpStatusExceptionUtil;
import nl.tweeenveertig.openstack.headers.object.*;
import nl.tweeenveertig.openstack.model.DownloadInstructions;
import nl.tweeenveertig.openstack.model.UploadInstructions;
import nl.tweeenveertig.openstack.client.StoredObject;
import nl.tweeenveertig.openstack.client.core.AbstractStoredObject;
import nl.tweeenveertig.openstack.exception.CommandException;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpStatus;

import javax.activation.MimetypesFileTypeMap;
import java.io.*;
import java.util.Date;

public class StoredObjectMock extends AbstractStoredObject {

    private boolean created = false;

    private byte[] object;

    public StoredObjectMock(Container container, String name) {
        super(container, name);
    }

    @Override
    protected void getInfo() {
        this.info.setEtag(new Etag(object == null ? "" : DigestUtils.md5Hex(object)));
        this.info.setContentType(new ObjectContentType(new MimetypesFileTypeMap().getContentType(getName())));
        this.info.setContentLength(new ObjectContentLength(Long.toString(object == null ? 0 : object.length)));
    }

    public InputStream downloadObjectAsInputStream() {
        return downloadObjectAsInputStream(new DownloadInstructions());
    }

    public InputStream downloadObjectAsInputStream(DownloadInstructions downloadInstructions) {
        return new MockInputStreamWrapper(new ByteArrayInputStream(downloadObject(downloadInstructions)));
    }

    public byte[] downloadObject() {
        return downloadObject(new DownloadInstructions());
    }

    public byte[] downloadObject(DownloadInstructions downloadInstructions) {
        if (downloadInstructions.getRange() != null) {
            return downloadInstructions.getRange().copy(object);
        }
        if (downloadInstructions.getMatchConditional() != null) {
            downloadInstructions.getMatchConditional().matchAgainst(info.getEtag());
        }
        if (downloadInstructions.getSinceConditional() != null) {
            downloadInstructions.getSinceConditional().sinceAgainst(info.getLastModifiedAsDate());
        }
        return object;
    }

    public void downloadObject(File targetFile) {
        downloadObject(targetFile, new DownloadInstructions());
    }

    public void downloadObject(File targetFile, DownloadInstructions downloadInstructions) {
        InputStream is = null;
        OutputStream os = null;
        try {
            is = new ByteArrayInputStream(downloadObject(downloadInstructions));
            os = new FileOutputStream(targetFile);
            IOUtils.copy(is, os);
        } catch (IOException err) {
            throw new CommandException("IO Failure", err);
        } finally {
            if (os != null) try { os.close(); } catch (IOException logOrIgnore) {}
            if (is != null) try { is.close(); } catch (IOException logOrIgnore) {}
        }
    }

    public void directlyUploadObject(UploadInstructions uploadInstructions) {
        if (!this.created) {
            ((ContainerMock)getContainer()).createObject(this);
            this.created = true;
        }
        try {
            saveObject(IOUtils.toByteArray(uploadInstructions.getEntity().getContent()));
        } catch (IOException err) {
            throw new CommandException(err.getMessage());
        }
    }

    public void uploadObject(InputStream inputStream) {
        try {
            this.uploadObject(IOUtils.toByteArray(inputStream));
        } catch (IOException err) {
            throw new CommandException("Unable to convert inputstream to byte[]", err);
        }
    }

    public void uploadObject(byte[] fileToUpload) {
        uploadObject(new UploadInstructions(fileToUpload));
    }

    public void uploadObject(File fileToUpload) {
        InputStream is = null;
        OutputStream os = null;
        try {
            // When doing a mime type check, this would be the right place to do it
            is = new FileInputStream(fileToUpload);
            os = new ByteArrayOutputStream();
            IOUtils.copy(is, os);
            uploadObject(((ByteArrayOutputStream)os).toByteArray());
        } catch (IOException err) {
            throw new CommandException("IO Failure", err);
        } finally {
            if (os != null) try { os.close(); } catch (IOException logOrIgnore) {}
            if (is != null) try { is.close(); } catch (IOException logOrIgnore) {}
        }
    }

    public void delete() {

        if (!this.created) {
            HttpStatusExceptionUtil.throwException(HttpStatus.SC_NOT_FOUND);
        }
        ((ContainerMock)getContainer()).deleteObject(this);
        this.created = false;
        invalidate();
    }

    public void copyObject(Container targetContainer, StoredObject targetObject) {
        byte[] targetContent = object.clone();
        if (!targetContainer.exists()) {
            targetContainer.create();
        }
        targetObject.uploadObject(targetContent);
    }

    public StoredObject setContentType(String contentType) {
        info.setContentType(new ObjectContentType(contentType));
        return this;
    }

    public StoredObject setDeleteAfter(long seconds) {
        return setDeleteAt(new Date(new Date().getTime() + seconds * 1000));
    }

    public StoredObject setDeleteAt(Date date) {
        ObjectDeleter objectDeleter = ((AccountMock)getContainer().getAccount()).getObjectDeleter();
        this.info.setDeleteAt(new DeleteAt(date));
        if (objectDeleter != null) {
            objectDeleter.scheduleForDeletion(this, date);
        }
        return this;
    }

    public String getPublicURL() {
        return "";
    }

    protected void saveObject(byte[] object) {
        this.object = object;
        this.info.setLastModified(new ObjectLastModified(new Date()));
        invalidate();
    }

    public void invalidate() {
        ((ContainerMock)getContainer()).invalidate();
        super.invalidate();
    }

    @Override
    protected void saveMetadata() {} // no action necessary

    @Override
    public boolean exists() {
        return super.exists() && created;
    }
}
