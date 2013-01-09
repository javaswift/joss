package nl.tweeenveertig.openstack.client.impl;

import nl.tweeenveertig.openstack.command.identity.access.AccessImpl;
import nl.tweeenveertig.openstack.model.Container;
import nl.tweeenveertig.openstack.client.core.AbstractAccount;
import nl.tweeenveertig.openstack.command.account.AccountInformationCommand;
import nl.tweeenveertig.openstack.command.account.AccountMetadataCommand;
import nl.tweeenveertig.openstack.command.account.ListContainersCommand;
import nl.tweeenveertig.openstack.command.identity.AuthenticationCommand;
import nl.tweeenveertig.openstack.model.PaginationMap;
import org.apache.http.client.HttpClient;

import java.util.ArrayList;
import java.util.Collection;

public class AccountImpl extends AbstractAccount {

    private AuthenticationCommand command;
    private HttpClient httpClient;
    private AccessImpl access;

    public AccessImpl authenticate() {
        return access = command.call();
    }

    public AccountImpl(AuthenticationCommand command, HttpClient httpClient, AccessImpl access, boolean allowCaching) {
        super(allowCaching);
        this.command = command;
        this.httpClient = httpClient;
        this.access = access;
    }

    public Collection<Container> listContainers() {
        return listContainers(null, ListContainersCommand.MAX_PAGE_SIZE);
    }

    @Override
    public PaginationMap getPaginationMap(int pageSize) {
        // TBD
        return null;
    }

    public Collection<Container> listContainers(String marker, int pageSize) {
        Collection<String> containerNames = new ListContainersCommand(this, httpClient, access, marker, pageSize).call();
        Collection<Container> containers = new ArrayList<Container>();
        for (String containerName : containerNames) {
            containers.add(this.getContainer(containerName));
        }
        return containers;
    }

    public Container getContainer(String containerName) {
        return new ContainerImpl(this, containerName, isAllowCaching());
    }

    @Override
    protected void saveMetadata() {
        new AccountMetadataCommand(this, getClient(), getAccess(), info.getMetadata()).call();
    }

    protected void getInfo() {
        this.info = new AccountInformationCommand(this, httpClient, access).call();
        this.setInfoRetrieved();
    }

    public HttpClient getClient() {
        return httpClient;
    }

    public AccessImpl getAccess() {
        return access;
    }

    public String getPublicURL() {
        return access.getPublicURL();
    }
}
