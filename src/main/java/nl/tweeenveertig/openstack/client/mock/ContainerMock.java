package nl.tweeenveertig.openstack.client.mock;

import nl.tweeenveertig.openstack.model.Account;
import nl.tweeenveertig.openstack.model.Container;
import nl.tweeenveertig.openstack.model.StoredObject;
import nl.tweeenveertig.openstack.client.core.AbstractContainer;
import nl.tweeenveertig.openstack.exception.HttpStatusExceptionUtil;
import nl.tweeenveertig.openstack.headers.container.ContainerBytesUsed;
import nl.tweeenveertig.openstack.headers.container.ContainerObjectCount;
import nl.tweeenveertig.openstack.headers.container.ContainerRights;
import nl.tweeenveertig.openstack.information.ContainerInformation;
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
}

