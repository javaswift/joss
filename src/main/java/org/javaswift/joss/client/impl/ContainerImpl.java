package org.javaswift.joss.client.impl;

import org.javaswift.joss.instructions.ListInstructions;
import org.javaswift.joss.model.Account;
import org.javaswift.joss.model.Container;
import org.javaswift.joss.model.StoredObject;
import org.javaswift.joss.client.core.AbstractContainer;
import org.javaswift.joss.command.impl.container.*;
import org.javaswift.joss.command.impl.identity.access.AccessImpl;
import org.apache.http.client.HttpClient;

import java.util.Collection;

public class ContainerImpl extends AbstractContainer {

    public ContainerImpl(Account account, String name, boolean allowCaching) {
        super(account, name, allowCaching);
    }

    public void makePublic() {
        new ContainerRightsCommand(getAccount(), getClient(), getAccess(), this, true).call();
    }

    public void makePrivate() {
        new ContainerRightsCommand(getAccount(), getClient(), getAccess(), this, false).call();
    }

    public Collection<StoredObject> list(String prefix, String marker, int pageSize) {
        ListInstructions listInstructions = new ListInstructions()
                .setPrefix(prefix)
                .setMarker(marker)
                .setLimit(pageSize);
        return new ListObjectsCommand(getAccount(), getClient(), getAccess(), this, listInstructions).call();
    }

    public Container create() {
        new CreateContainerCommand(getAccount(), getClient(), getAccess(), this).call();
        return this;
    }

    public void delete() {
        new DeleteContainerCommand(getAccount(), getClient(), getAccess(), this).call();
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
        new ContainerMetadataCommand(getAccount(), getClient(), getAccess(), this, info.getMetadata()).call();
    }

    protected void getInfo() {
        this.info = new ContainerInformationCommand(getAccount(), getClient(), getAccess(), this).call();
        this.setInfoRetrieved();
    }

}
