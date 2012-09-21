package nl.tweeenveertig.openstack.client.mock;

import nl.tweeenveertig.openstack.client.Container;
import nl.tweeenveertig.openstack.client.UploadInstructions;
import nl.tweeenveertig.openstack.client.StoredObject;
import nl.tweeenveertig.openstack.client.core.AbstractStoredObject;
import nl.tweeenveertig.openstack.command.core.CommandException;
import nl.tweeenveertig.openstack.command.core.CommandExceptionError;
import nl.tweeenveertig.openstack.util.MimeTypeMap;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpStatus;

import java.io.*;

public class StoredObjectMock extends AbstractStoredObject {

    private boolean created = false;

    private byte[] object;

    public StoredObjectMock(Container container, String name) {
        super(container, name);
    }

    @Override
    protected void getInfo() {
        this.etag = object == null ? "" : DigestUtils.md5Hex(object);
        this.contentType = MimeTypeMap.getContentType(getName());
        this.contentLength = object == null ? 0 : object.length;
    }

    public InputStream downloadObjectAsInputStream() {
        return new MockInputStreamWrapper(new ByteArrayInputStream(object));
    }

    public byte[] downloadObject() {
        return object;
    }

    public void downloadObject(File targetFile) {
        InputStream is = null;
        OutputStream os = null;
        try {
            is = new ByteArrayInputStream(object);
            os = new FileOutputStream(targetFile);
            IOUtils.copy(is, os);
        } catch (IOException err) {
            throw new CommandException("IO Failure", err);
        } finally {
            if (os != null) try { os.close(); } catch (IOException logOrIgnore) {}
            if (is != null) try { is.close(); } catch (IOException logOrIgnore) {}
        }
    }

    public void uploadObject(UploadInstructions uploadInstructions) throws IOException {
        if (!this.created) {
            ((ContainerMock)getContainer()).createObject(this);
            this.created = true;
        }
        saveObject(IOUtils.toByteArray(uploadInstructions.getEntity().getContent()));
    }

    public void uploadObject(InputStream inputStream) {
        try {
            this.uploadObject(IOUtils.toByteArray(inputStream));
        } catch (IOException err) {
            throw new CommandException("Unable to convert inputstream to byte[]", err);
        }
    }

    public void uploadObject(byte[] fileToUpload) throws IOException {
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
            throw new CommandException(HttpStatus.SC_NOT_FOUND, CommandExceptionError.CONTAINER_OR_OBJECT_DOES_NOT_EXIST);
        }
        ((ContainerMock)getContainer()).deleteObject(this);
        invalidate();
    }

    public void copyObject(Container targetContainer, StoredObject targetObject) throws IOException {
        byte[] targetContent = object.clone();
        if (!targetContainer.exists()) {
            targetContainer.create();
        }
        targetObject.uploadObject(targetContent);
    }

    public String getPublicURL() {
        return "";
    }

    protected void saveObject(byte[] object) {
        this.object = object;
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
