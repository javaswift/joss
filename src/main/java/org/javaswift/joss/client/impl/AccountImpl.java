package org.javaswift.joss.client.impl;

import org.javaswift.joss.command.identity.access.AccessImpl;
import org.javaswift.joss.instructions.ListInstructions;
import org.javaswift.joss.model.Container;
import org.javaswift.joss.client.core.AbstractAccount;
import org.javaswift.joss.command.account.AccountInformationCommand;
import org.javaswift.joss.command.account.AccountMetadataCommand;
import org.javaswift.joss.command.account.ListContainersCommand;
import org.javaswift.joss.command.identity.AuthenticationCommand;
import org.apache.http.client.HttpClient;

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

    public Collection<Container> list(String prefix, String marker, int pageSize) {
        ListInstructions listInstructions = new ListInstructions()
                .setPrefix(prefix)
                .setMarker(marker)
                .setLimit(pageSize);
        return new ListContainersCommand(this, httpClient, access, listInstructions).call();
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
