package org.javaswift.joss.client.mock;

import org.javaswift.joss.model.Account;
import org.javaswift.joss.model.Container;
import org.javaswift.joss.model.StoredObject;
import org.javaswift.joss.client.core.AbstractContainer;
import org.javaswift.joss.exception.HttpStatusExceptionUtil;
import org.javaswift.joss.headers.container.ContainerBytesUsed;
import org.javaswift.joss.headers.container.ContainerObjectCount;
import org.javaswift.joss.headers.container.ContainerRights;
import org.javaswift.joss.information.ContainerInformation;
import org.apache.http.HttpStatus;

import java.util.*;

public class ContainerMock extends AbstractContainer {

    private Map<String, StoredObject> objects = new TreeMap<String, StoredObject>();

    private boolean created = false;

    public ContainerMock(Account account, String name) {
        super(account, name, ALLOW_CACHING);
        this.info = new ContainerInformation();
    }

    @Override
    protected void getInfo() {
        int objectCount = 0;
        long bytesUsed = 0;
        for (StoredObject object : objects.values()) {
            objectCount++;
            bytesUsed += object.getContentLength();
        }
        this.info.setBytesUsed(new ContainerBytesUsed(Long.toString(bytesUsed)));
        this.info.setObjectCount(new ContainerObjectCount(Integer.toString(objectCount)));
    }

    public void makePublic() {
        this.info.setPublicContainer(new ContainerRights(true));
    }

    public void makePrivate() {
        this.info.setPublicContainer(new ContainerRights(false));
    }

    public Collection<StoredObject> listObjects() {
        return objects.values();
    }

    public Container create() {
        if (this.created) {
            HttpStatusExceptionUtil.throwException(HttpStatus.SC_ACCEPTED);
        }
        ((AccountMock)getAccount()).createContainer(this);
        this.created = true;
        invalidate();
        return this;
    }

    public void delete() {

        if (!this.created) {
            HttpStatusExceptionUtil.throwException(HttpStatus.SC_NOT_FOUND);
        }
        if (this.objects.size() > 0) {
            HttpStatusExceptionUtil.throwException(HttpStatus.SC_CONFLICT);
        }
        this.created = false;
        ((AccountMock)getAccount()).deleteContainer(this);
        invalidate();
    }

    public StoredObject getObject(String name) {
        StoredObject foundObject = objects.get(name);
        return foundObject == null ? new StoredObjectMock(this, name) : foundObject;
    }

    public void deleteObject(StoredObject object) {
        this.objects.remove(object.getName());
        getInfo();
    }

    public void createObject(StoredObject object) {
        if (!this.created) {
            HttpStatusExceptionUtil.throwException(HttpStatus.SC_NOT_FOUND);
        }
        this.objects.put(object.getName(), object);
        getInfo();
    }

    public void invalidate() {
        ((AccountMock)getAccount()).invalidate();
        super.invalidate();
    }

    @Override
    protected void saveMetadata() {} // no action necessary

    @Override
    public boolean exists() {
        return super.exists() && created;
    }

    @Override
    public Collection<StoredObject> list(String prefix, String marker, int pageSize) {
        return new PageServer<StoredObject>().createPage(objects.values(), prefix, marker, pageSize);
    }

}

