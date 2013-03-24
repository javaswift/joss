package org.javaswift.joss.client.mock;

import org.javaswift.joss.model.Container;
import org.javaswift.joss.client.core.AbstractAccount;
import org.javaswift.joss.client.mock.scheduled.ObjectDeleter;
import org.javaswift.joss.command.impl.identity.access.AccessImpl;
import org.javaswift.joss.exception.CommandException;
import org.javaswift.joss.headers.account.AccountBytesUsed;
import org.javaswift.joss.headers.account.AccountContainerCount;
import org.javaswift.joss.headers.account.AccountObjectCount;

import java.util.*;

public class AccountMock extends AbstractAccount {

    private Map<String, Container> containers = new TreeMap<String, Container>();

    public AccessImpl authenticate() { return null; /* ignore */ }

    private ObjectDeleter objectDeleter;

    private String publicUrl;

    public AccountMock() {
        super(ALLOW_CACHING);
    }

    public AccountMock setOnFileObjectStore(String onFileObjectStore) {
        if (onFileObjectStore == null) {
            return this;
        }
        OnFileObjectStoreLoader loader = new OnFileObjectStoreLoader();
        try {
            loader.createContainers(this, onFileObjectStore);
        } catch (Exception err) {
            throw new CommandException("Unable to load the object store from file: "+err.getMessage(), err);
        }
        return this;
    }

    public AccountMock setObjectDeleter(ObjectDeleter objectDeleter) {
        this.objectDeleter = objectDeleter;
        return this;
    }

    public AccountMock setPublicUrl(String publicUrl) {
        this.publicUrl = publicUrl;
        return this;
    }

    public ObjectDeleter getObjectDeleter() {
        return this.objectDeleter;
    }

    @Override
    protected void saveMetadata() {} // no action necessary

    @Override
    protected void getInfo() {

        int containerCount= 0;
        int objectCount = 0;
        long bytesUsed = 0;
        for (Container container : containers.values()) {
            containerCount++;
            objectCount += container.getCount();
            bytesUsed += container.getBytesUsed();
        }
        this.info.setContainerCount(new AccountContainerCount(Integer.toString(containerCount)));
        this.info.setObjectCount(new AccountObjectCount(Integer.toString(objectCount)));
        this.info.setBytesUsed(new AccountBytesUsed(Long.toString(bytesUsed)));
    }

    @Override
    public Collection<Container> list(String prefix, String marker, int pageSize) {
        return new PageServer<Container>().createPage(containers.values(), prefix, marker, pageSize);
    }

    public Container getContainer(String name) {
        Container foundContainer = containers.get(name);
        return foundContainer == null ? new ContainerMock(this, name) : foundContainer;
    }

    public void createContainer(Container container) {
        this.containers.put(container.getName(), container);
    }

    public void deleteContainer(Container container) {
        this.containers.remove(container.getName());
    }

    public String getPublicURL() {
        return this.publicUrl == null ? "" : this.publicUrl;
    }

}
