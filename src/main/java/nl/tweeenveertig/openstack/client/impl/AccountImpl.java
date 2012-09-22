package nl.tweeenveertig.openstack.client.impl;

import nl.tweeenveertig.openstack.client.Container;
import nl.tweeenveertig.openstack.client.core.AbstractAccount;
import nl.tweeenveertig.openstack.command.account.AccountInformationCommand;
import nl.tweeenveertig.openstack.command.account.AccountMetadataCommand;
import nl.tweeenveertig.openstack.command.account.ListContainersCommand;
import nl.tweeenveertig.openstack.command.identity.AuthenticationCommand;
import nl.tweeenveertig.openstack.command.identity.access.Access;
import org.apache.http.client.HttpClient;

import java.util.ArrayList;
import java.util.Collection;

public class AccountImpl extends AbstractAccount {

    private AuthenticationCommand command;
    private HttpClient httpClient;
    private Access access;

    public Access authenticate() {
        return access = command.call();
    }

    public AccountImpl(AuthenticationCommand command, HttpClient httpClient, Access access) {
        this.command = command;
        this.httpClient = httpClient;
        this.access = access;
    }

    public Collection<Container> listContainers() {
        Collection<String> containerNames = new ListContainersCommand(this, httpClient, access).call();
        Collection<Container> containers = new ArrayList<Container>();
        for (String containerName : containerNames) {
            containers.add(this.getContainer(containerName));
        }
        return containers;
    }

    public Container getContainer(String containerName) {
        return new ContainerImpl(this, containerName);
    }

    @Override
    protected void saveMetadata() {
        new AccountMetadataCommand(this, getClient(), getAccess(), getMetadataWithoutTriggeringCheck()).call();
    }

    protected void getInfo() {
        this.info = new AccountInformationCommand(this, httpClient, access).call();
        this.setInfoRetrieved();
    }

    public HttpClient getClient() {
        return httpClient;
    }

    public Access getAccess() {
        return access;
    }
}
