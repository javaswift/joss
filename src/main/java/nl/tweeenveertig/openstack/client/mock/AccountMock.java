package nl.tweeenveertig.openstack.client.mock;

import nl.tweeenveertig.openstack.client.Container;
import nl.tweeenveertig.openstack.client.core.AbstractAccount;

import java.util.*;

public class AccountMock extends AbstractAccount {

    private Map<String, Container> containers = new TreeMap<String, Container>();

    @Override
    protected void getInfo() {

        this.containerCount= 0;
        this.objectCount = 0;
        this.bytesUsed = 0;
        for (Container container : containers.values()) {
            this.containerCount= 0;
            this.objectCount += container.getObjectCount();
            this.bytesUsed += container.getBytesUsed();
        }
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
