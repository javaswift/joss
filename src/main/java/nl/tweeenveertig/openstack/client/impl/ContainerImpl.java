package nl.tweeenveertig.openstack.client.impl;

import nl.tweeenveertig.openstack.client.Account;
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
        new ContainerRightsCommand(getAccount(), getClient(), getAccess(), this, true).call();
    }

    public void makePrivate() {
        new ContainerRightsCommand(getAccount(), getClient(), getAccess(), this, false).call();
    }

    public Collection<StoredObject> listObjects() {
        Collection<String> objectNames = new ListObjectsCommand(getAccount(), getClient(), getAccess(), this).call();
        Collection<StoredObject> objects = new ArrayList<StoredObject>();
        for (String objectName : objectNames) {
            objects.add(getObject(objectName));
        }
        return objects;
    }

    public void create() {
        new CreateContainerCommand(getAccount(), getClient(), getAccess(), this).call();
    }

    public void delete() {
        new DeleteContainerCommand(getAccount(), getClient(), getAccess(), this).call();
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

    @Override
    protected void saveMetadata() {
        new ContainerMetadataCommand(getAccount(), getClient(), getAccess(), this, getMetadataWithoutTriggeringCheck()).call();
    }

    protected void getInfo() {
        this.info = new ContainerInformationCommand(getAccount(), getClient(), getAccess(), this).call();
        this.setInfoRetrieved();
    }

}
