package nl.tweeenveertig.openstack.client.mock;

import nl.tweeenveertig.openstack.client.Account;
import nl.tweeenveertig.openstack.client.StoredObject;
import nl.tweeenveertig.openstack.client.core.AbstractContainer;
import nl.tweeenveertig.openstack.exception.CommandException;
import nl.tweeenveertig.openstack.command.core.CommandExceptionError;
import nl.tweeenveertig.openstack.exception.HttpStatusToExceptionMapper;
import nl.tweeenveertig.openstack.headers.container.ContainerBytesUsed;
import nl.tweeenveertig.openstack.headers.container.ContainerObjectCount;
import nl.tweeenveertig.openstack.headers.container.ContainerRights;
import nl.tweeenveertig.openstack.model.ContainerInformation;
import org.apache.http.HttpStatus;

import java.util.*;

public class ContainerMock extends AbstractContainer {

    private Map<String, StoredObject> objects = new TreeMap<String, StoredObject>();

    private boolean created = false;

    public ContainerMock(Account account, String name) {
        super(account, name);
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

    public void create() {
        if (this.created) {
            HttpStatusToExceptionMapper.throwException(HttpStatus.SC_ACCEPTED);
        }
        ((AccountMock)getAccount()).createContainer(this);
        this.created = true;
        invalidate();
    }

    public void delete() {

        if (!this.created) {
            HttpStatusToExceptionMapper.throwException(HttpStatus.SC_NOT_FOUND);
        }
        if (this.objects.size() > 0) {
            HttpStatusToExceptionMapper.throwException(HttpStatus.SC_CONFLICT);
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

