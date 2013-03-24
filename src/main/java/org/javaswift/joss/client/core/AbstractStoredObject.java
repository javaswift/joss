package org.javaswift.joss.client.core;

import org.javaswift.joss.exception.CommandException;
import org.javaswift.joss.headers.object.*;
import org.javaswift.joss.instructions.UploadInstructions;
import org.javaswift.joss.model.Container;
import org.javaswift.joss.model.StoredObject;
import org.javaswift.joss.headers.Metadata;
import org.javaswift.joss.information.ObjectInformation;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class AbstractStoredObject extends AbstractObjectStoreEntity<ObjectInformation> implements StoredObject {

    protected String name;

    private Container container;

    public AbstractStoredObject(Container container, String name, boolean allowCaching) {
        super(allowCaching);
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

    public void setLastModified(String date) {
        try {
            // The LastModified date in the JSON body differs from that in the response header
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            this.info.setLastModified(new ObjectLastModified(formatter.parse(date)));
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
    }

    public void uploadObjectAsSegments(UploadInstructions uploadInstructions) {
        ((AbstractContainer)getContainer()).uploadSegmentedObjects(getName(), uploadInstructions);
        // The manifest file is the handle which allows the ObjectStore to piece the segments together as one file
        UploadInstructions manifest = new UploadInstructions(new byte[] {})
                .setObjectManifest(new ObjectManifest(getPath()))
                .setContentType(uploadInstructions.getContentType());
        uploadObject(manifest);
    }

    protected abstract void directlyUploadObject(UploadInstructions uploadInstructions);

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
}
