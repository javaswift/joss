package org.javaswift.joss.client.mock;

import org.javaswift.joss.instructions.DownloadInstructions;
import org.javaswift.joss.instructions.UploadInstructions;
import org.javaswift.joss.model.Container;
import org.javaswift.joss.client.mock.scheduled.ObjectDeleter;
import org.javaswift.joss.exception.HttpStatusExceptionUtil;
import org.javaswift.joss.headers.object.*;
import org.javaswift.joss.model.StoredObject;
import org.javaswift.joss.client.core.AbstractStoredObject;
import org.javaswift.joss.exception.CommandException;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.activation.MimetypesFileTypeMap;
import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

public class StoredObjectMock extends AbstractStoredObject {

    public static final Logger LOG = LoggerFactory.getLogger(StoredObjectMock.class);

    private boolean created = false;

    private byte[] object;

    private ObjectManifest objectManifest;

    public StoredObjectMock(Container container, String name) {
        super(container, name, ALLOW_CACHING);
    }

    @Override
    protected void getInfo() {
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

    protected byte[] mergeSegmentedObjects() {
        Container segmentFolder = getContainer().getAccount().getContainer(this.objectManifest.getContainerName());
        String objectPrefix = this.objectManifest.getObjectPrefix();
        Collection<StoredObject> segments = new ArrayList<StoredObject>();
        int byteCount = 0;
        for (StoredObject storedObject : segmentFolder.list()) {
            if (storedObject.getName().startsWith(objectPrefix) && ! storedObject.getName().equals(objectPrefix)) {
                segments.add(storedObject);
                byteCount += storedObject.getContentLength();
            }
        }
        byte[] mergedObject = new byte[byteCount];
        int offset = 0;
        for (StoredObject segment : segments) {
            System.arraycopy(segment.downloadObject(), 0, mergedObject, offset, (int)segment.getContentLength());
            offset += segment.getContentLength();
        }
        return mergedObject;
    }

    public byte[] downloadObject(DownloadInstructions downloadInstructions) {
        byte[] object = this.object;
        if (this.objectManifest != null) {
            object = mergeSegmentedObjects();
        }
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
            closeStreams(is, os);
        }
    }

    public void directlyUploadObject(UploadInstructions uploadInstructions) {
        if (!this.created) {
            ((ContainerMock)getContainer()).createObject(this);
            this.created = true;
        }
        try {
            saveObject(IOUtils.toByteArray(uploadInstructions.getEntity().getContent()));
            this.objectManifest = uploadInstructions.getObjectManifest();
            // Set Etag
            this.info.setEtag(
                    new Etag(uploadInstructions.getMd5() != null ?
                        uploadInstructions.getMd5() :
                        DigestUtils.md5Hex(object))
            );
            // Set Content-Type
            this.info.setContentType(
                    uploadInstructions.getContentType() != null ?
                        uploadInstructions.getContentType() :
                        new ObjectContentType(new MimetypesFileTypeMap().getContentType(getName()))
            );
        } catch (IOException err) {
            LOG.error("JOSS / Failed to upload content: "+err.getMessage());
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
            closeStreams(is, os);
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

    @SuppressWarnings("EmptyCatchBlock")
    protected void closeStreams(InputStream is, OutputStream os) {
        if (os != null) try { os.close(); } catch (IOException logOrIgnore) {}
        if (is != null) try { is.close(); } catch (IOException logOrIgnore) {}
    }
}
