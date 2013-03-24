package org.javaswift.joss.client.impl;

import org.javaswift.joss.command.impl.factory.ContainerCommandFactoryImpl;
import org.javaswift.joss.command.shared.factory.ContainerCommandFactory;
import org.javaswift.joss.instructions.ListInstructions;
import org.javaswift.joss.model.Account;
import org.javaswift.joss.model.Container;
import org.javaswift.joss.model.StoredObject;
import org.javaswift.joss.client.core.AbstractContainer;
import org.javaswift.joss.command.impl.identity.access.AccessImpl;
import org.apache.http.client.HttpClient;

import java.util.Collection;

public class ContainerImpl extends AbstractContainer {

    ContainerCommandFactory commandFactory = new ContainerCommandFactoryImpl();

    public ContainerImpl(Account account, String name, boolean allowCaching) {
        super(account, name, allowCaching);
    }

    public void makePublic() {
        commandFactory.createContainerRightsCommand(getAccount(), getClient(), getAccess(), this, true).call();
    }

    public void makePrivate() {
        commandFactory.createContainerRightsCommand(getAccount(), getClient(), getAccess(), this, false).call();
    }

    public Collection<StoredObject> list(String prefix, String marker, int pageSize) {
        ListInstructions listInstructions = new ListInstructions()
                .setPrefix(prefix)
                .setMarker(marker)
                .setLimit(pageSize);
        return commandFactory.createListObjectsCommand(getAccount(), getClient(), getAccess(), this, listInstructions).call();
    }

    public Container create() {
        commandFactory.createCreateContainerCommand(getAccount(), getClient(), getAccess(), this).call();
        return this;
    }

    public void delete() {
        commandFactory.createDeleteContainerCommand(getAccount(), getClient(), getAccess(), this).call();
    }

    public StoredObject getObject(String objectName) {
        return new StoredObjectImpl(this, objectName, isAllowCaching());
    }

    protected HttpClient getClient() {
        return ((AccountImpl)getAccount()).getClient();
    }

    protected AccessImpl getAccess() {
        return ((AccountImpl)getAccount()).getAccess();
    }

    @Override
    protected void saveMetadata() {
        commandFactory.createContainerMetadataCommand(getAccount(), getClient(), getAccess(), this, info.getMetadata()).call();
    }

    protected void getInfo() {
        this.info = commandFactory.createContainerInformationCommand(getAccount(), getClient(), getAccess(), this).call();
        this.setInfoRetrieved();
    }

}
