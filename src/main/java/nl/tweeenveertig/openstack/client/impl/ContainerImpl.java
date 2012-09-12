package nl.tweeenveertig.openstack.client.impl;

import nl.tweeenveertig.openstack.client.Account;
import nl.tweeenveertig.openstack.client.Container;
import nl.tweeenveertig.openstack.client.StoredObject;
import nl.tweeenveertig.openstack.client.core.AbstractContainer;
import nl.tweeenveertig.openstack.command.container.*;
import nl.tweeenveertig.openstack.command.identity.access.Access;
import org.apache.http.client.HttpClient;

import java.util.ArrayList;
import java.util.Collection;

public class ContainerImpl extends AbstractContainer {

    public ContainerImpl(Account account, String name) {
        super(account, name);
    }

    public void makePublic() {
        new ContainerRightsCommand(getClient(), getAccess(), this, true).call();
    }

    public void makePrivate() {
        new ContainerRightsCommand(getClient(), getAccess(), this, false).call();
    }

    public Collection<StoredObject> listObjects() {
        Collection<String> objectNames = new ListObjectsCommand(getClient(), getAccess(), this).call();
        Collection<StoredObject> objects = new ArrayList<StoredObject>();
        for (String objectName : objectNames) {
            objects.add(getObject(objectName));
        }
        return objects;
    }

    public void create() {
        new CreateContainerCommand(getClient(), getAccess(), this).call();
    }

    public void delete() {
        new DeleteContainerCommand(getClient(), getAccess(), this).call();
    }

    public StoredObject getObject(String objectName) {
        return new StoredObjectImpl(this, objectName);
    }

    protected HttpClient getClient() {
        return ((AccountImpl)getAccount()).getClient();
    }

    protected Access getAccess() {
        return ((AccountImpl)getAccount()).getAccess();
    }

    protected void getInfo() {
        ContainerInformation info = new ContainerInformationCommand(getClient(), getAccess(), this).call();
        this.bytesUsed = info.getBytesUsed();
        this.objectCount = info.getObjectCount();
        this.publicContainer = info.isPublicContainer();
        this.setMetadata(info.getMetadata());
        this.setInfoRetrieved();
    }

}
