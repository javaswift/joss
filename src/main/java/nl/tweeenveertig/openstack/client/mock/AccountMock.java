package nl.tweeenveertig.openstack.client.mock;

import nl.tweeenveertig.openstack.client.Container;
import nl.tweeenveertig.openstack.client.core.AbstractAccount;
import nl.tweeenveertig.openstack.client.mock.scheduled.ObjectDeleter;
import nl.tweeenveertig.openstack.command.identity.access.Access;
import nl.tweeenveertig.openstack.headers.account.AccountBytesUsed;
import nl.tweeenveertig.openstack.headers.account.AccountContainerCount;
import nl.tweeenveertig.openstack.headers.account.AccountObjectCount;

import java.util.*;

public class AccountMock extends AbstractAccount {

    private Map<String, Container> containers = new TreeMap<String, Container>();

    public Access authenticate() { return null; /* ignore */ }

    private ObjectDeleter objectDeleter;

    public AccountMock setObjectDeleter(ObjectDeleter objectDeleter) {
        this.objectDeleter = objectDeleter;
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
            objectCount += container.getObjectCount();
            bytesUsed += container.getBytesUsed();
        }
        this.info.setContainerCount(new AccountContainerCount(Integer.toString(containerCount)));
        this.info.setObjectCount(new AccountObjectCount(Integer.toString(objectCount)));
        this.info.setBytesUsed(new AccountBytesUsed(Long.toString(bytesUsed)));
    }

    public Collection<Container> listContainers() {
        return containers.values();
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
}
