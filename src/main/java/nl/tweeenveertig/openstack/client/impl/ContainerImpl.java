package nl.tweeenveertig.openstack.client.impl;

import nl.tweeenveertig.openstack.instructions.ListInstructions;
import nl.tweeenveertig.openstack.model.Account;
import nl.tweeenveertig.openstack.model.Container;
import nl.tweeenveertig.openstack.model.StoredObject;
import nl.tweeenveertig.openstack.client.core.AbstractContainer;
import nl.tweeenveertig.openstack.command.container.*;
import nl.tweeenveertig.openstack.command.identity.access.AccessImpl;
import org.apache.http.client.HttpClient;

import java.util.ArrayList;
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

    public Collection<StoredObject> list(String marker, int pageSize) {
        ListInstructions listInstructions = new ListInstructions()
                .setMarker(marker)
                .setLimit(pageSize);
        Collection<String> objectNames = new ListObjectsCommand(getAccount(), getClient(), getAccess(), this).call();
        Collection<StoredObject> objects = new ArrayList<StoredObject>();
        for (String objectName : objectNames) {
            objects.add(getObject(objectName));
        }
        return objects;
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
