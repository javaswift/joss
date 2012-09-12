package nl.tweeenveertig.openstack.client.mock;

import nl.tweeenveertig.openstack.client.Account;
import nl.tweeenveertig.openstack.client.StoredObject;
import nl.tweeenveertig.openstack.client.core.AbstractContainer;
import nl.tweeenveertig.openstack.command.core.CommandException;
import nl.tweeenveertig.openstack.command.core.CommandExceptionError;
import org.apache.http.HttpStatus;

import java.util.*;

public class ContainerMock extends AbstractContainer {

    private Map<String, StoredObject> objects = new TreeMap<String, StoredObject>();

    private boolean created = false;

    public ContainerMock(Account account, String name) {
        super(account, name);
    }

    @Override
    protected void getInfo() {
        this.objectCount = 0;
        this.bytesUsed = 0;
        for (StoredObject object : objects.values()) {
            this.objectCount++;
            this.bytesUsed += object.getContentLength();
        }
    }

    public void makePublic() {
        this.publicContainer = true;
    }

    public void makePrivate() {
        this.publicContainer = false;
    }

    public Collection<StoredObject> listObjects() {
        return objects.values();
    }

    public void create() {
        if (this.created) {
            throw new CommandException(HttpStatus.SC_ACCEPTED, CommandExceptionError.CONTAINER_ALREADY_EXISTS);
        }
        ((AccountMock)getAccount()).createContainer(this);
        this.created = true;
        invalidate();
    }

    public void delete() {

        if (!this.created) {
            throw new CommandException(HttpStatus.SC_NOT_FOUND, CommandExceptionError.CONTAINER_DOES_NOT_EXIST);
        }
        if (this.objects.size() > 0) {
            throw new CommandException(HttpStatus.SC_CONFLICT, CommandExceptionError.CONTAINER_NOT_EMPTY);
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

