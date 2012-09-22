package nl.tweeenveertig.openstack.mock;

import nl.tweeenveertig.openstack.command.core.CommandException;
import nl.tweeenveertig.openstack.command.core.CommandExceptionError;
import nl.tweeenveertig.openstack.headers.account.AccountBytesUsed;
import nl.tweeenveertig.openstack.headers.account.AccountContainerCount;
import nl.tweeenveertig.openstack.headers.account.AccountObjectCount;
import nl.tweeenveertig.openstack.model.AccountInformation;
import nl.tweeenveertig.openstack.client.Container;
import nl.tweeenveertig.openstack.model.ContainerInformation;
import nl.tweeenveertig.openstack.client.StoredObject;
import org.apache.http.HttpStatus;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

public class MockAccount extends AbstractMock<AccountInformation> {

    private Map<Container, MockContainer> containers = new TreeMap<Container, MockContainer>();

    private Map<String, Object> metadata = new TreeMap<String, Object>();

    public MockContainer getContainer(Container container) {
        MockContainer foundContainer = containers.get(container);
        if (foundContainer == null) {
            throw new CommandException(HttpStatus.SC_NOT_FOUND, CommandExceptionError.CONTAINER_DOES_NOT_EXIST);
        }
        return foundContainer;
    }

    public MockContainer createContainer(Container container) {
        MockContainer foundContainer = containers.get(container);
        if (foundContainer != null) {
            throw new CommandException(HttpStatus.SC_ACCEPTED, CommandExceptionError.CONTAINER_ALREADY_EXISTS);
        }
        foundContainer = new MockContainer();
        containers.put(container, foundContainer);
        return foundContainer;
    }

    public void deleteContainer(Container container) {
        MockContainer foundContainer = getContainer(container);
        if (foundContainer.getNumberOfObjects() > 0) {
            throw new CommandException(HttpStatus.SC_CONFLICT, CommandExceptionError.CONTAINER_NOT_EMPTY);
        }
        containers.remove(container);
    }

    public Collection<Container> listContainers() {
        return containers.keySet();
    }

    public AccountInformation getInfo() {
        AccountInformation info = new AccountInformation();
        for (String metadataKey : metadata.keySet()) {
            info.addMetadata(metadataKey, metadata.get(metadataKey).toString());
        }
        return info;
    }

    public void copyObject(Container sourceContainer, StoredObject sourceObject, Container targetContainer, StoredObject targetObject) {
        MockObject source = getContainer(sourceContainer).getObject(sourceObject);
        MockObject target = getContainer(targetContainer).getOrCreateObject(targetObject);
        byte[] targetContent = source.getObject().clone();
        target.saveObject(targetObject, targetContent);
    }

    @Override
    protected void appendInformation(AccountInformation info) {
        info.setContainerCount(new AccountContainerCount(Integer.toString(containers.size())));
        int numberOfObjects = 0;
        long numberOfBytes = 0;
        for (MockContainer container : containers.values()) {
            ContainerInformation containerInformation = container.getInfo();
            numberOfObjects += containerInformation.getObjectCount();
            numberOfBytes += containerInformation.getBytesUsed();
        }
        info.setObjectCount(new AccountObjectCount(Integer.toString(numberOfObjects)));
        info.setBytesUsed(new AccountBytesUsed(Long.toString(numberOfBytes)));
    }

    @Override
    protected AccountInformation createInformationContainer() {
        return new AccountInformation();
    }

    public void setInfo(Map<String, Object> metadata) {
        this.metadata = metadata;
    }

}
