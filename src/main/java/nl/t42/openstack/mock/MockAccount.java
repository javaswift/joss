package nl.t42.openstack.mock;

import nl.t42.openstack.command.core.CommandException;
import nl.t42.openstack.command.core.CommandExceptionError;
import nl.t42.openstack.model.AccountInformation;
import nl.t42.openstack.model.Container;
import nl.t42.openstack.model.StoreObject;
import org.apache.http.HttpStatus;

import java.util.Map;
import java.util.TreeMap;

public class MockAccount {

    private Map<Container, MockContainer> containers = new TreeMap<Container, MockContainer>();

    private Map<String, Object> metadata = new TreeMap<String, Object>();

    public MockContainer getContainer(Container container) {
        MockContainer foundContainer = containers.get(container);
        if (foundContainer == null) {
            throw new CommandException(HttpStatus.SC_NOT_FOUND, CommandExceptionError.CONTAINER_DOES_NOT_EXIST);
        }
        return foundContainer;
    }

    public MockObject getObject(Container container, StoreObject object) {
        MockContainer foundContainer = containers.get(container);
        if (foundContainer == null) {
            throw new CommandException(HttpStatus.SC_NOT_FOUND, CommandExceptionError.CONTAINER_OR_OBJECT_DOES_NOT_EXIST);
        }
        MockObject foundObject = foundContainer.getObject(object);
        if (foundObject == null) {
            throw new CommandException(HttpStatus.SC_NOT_FOUND, CommandExceptionError.CONTAINER_OR_OBJECT_DOES_NOT_EXIST);
        }
        return foundObject;
    }

    public void createContainer(Container container) {
        MockContainer foundContainer = containers.get(container);
        if (foundContainer != null) {
            throw new CommandException(HttpStatus.SC_ACCEPTED, CommandExceptionError.CONTAINER_ALREADY_EXISTS);
        }
        containers.put(container, new MockContainer());
    }

    public void deleteContainer(Container container) {
        MockContainer foundContainer = getContainer(container);
        if (foundContainer.getNumberOfObjects() > 0) {
            throw new CommandException(HttpStatus.SC_CONFLICT, CommandExceptionError.CONTAINER_NOT_EMPTY);
        }
        containers.remove(container);
    }

    public Container[] listContainers() {
        return containers.keySet().toArray(new Container[containers.size()]);
    }

    public AccountInformation getInfo() {
        AccountInformation info = new AccountInformation();
        for (String metadataKey : metadata.keySet()) {
            info.addMetadata(metadataKey, metadata.get(metadataKey).toString());
        }
        return info;
    }

    public void setInfo(Map<String, Object> metadata) {
        this.metadata = metadata;
    }

}
