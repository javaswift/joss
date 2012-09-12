package nl.tweeenveertig.openstack.client.impl;

import nl.tweeenveertig.openstack.client.Container;
import nl.tweeenveertig.openstack.client.core.AbstractAccount;
import nl.tweeenveertig.openstack.command.account.AccountInformation;
import nl.tweeenveertig.openstack.command.account.AccountInformationCommand;
import nl.tweeenveertig.openstack.command.account.ListContainersCommand;
import nl.tweeenveertig.openstack.command.identity.access.Access;
import org.apache.http.client.HttpClient;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class AccountImpl extends AbstractAccount {

    private HttpClient httpClient;
    private Access access;

    public AccountImpl(HttpClient httpClient, Access access) {
        this.httpClient = httpClient;
        this.access = access;
    }

    public Collection<Container> listContainers() {
        Collection<String> containerNames = new ListContainersCommand(httpClient, access).call();
        Collection<Container> containers = new ArrayList<Container>();
        for (String containerName : containerNames) {
            containers.add(this.getContainer(containerName));
        }
        return containers;
    }

    public Container getContainer(String containerName) {
        return new ContainerImpl(this, containerName);
    }

    protected void getInfo() {
        AccountInformation info = new AccountInformationCommand(httpClient, access).call();
        this.bytesUsed = info.getBytesUsed();
        this.containerCount = info.getContainerCount();
        this.objectCount = info.getObjectCount();
        this.setMetadata(info.getMetadata());
        this.setInfoRetrieved();
    }

    public HttpClient getClient() {
        return httpClient;
    }

    public Access getAccess() {
        return access;
    }
}
