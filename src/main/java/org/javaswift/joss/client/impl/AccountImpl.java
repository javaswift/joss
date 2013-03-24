package org.javaswift.joss.client.impl;

import org.javaswift.joss.command.impl.factory.AccountCommandFactoryImpl;
import org.javaswift.joss.command.impl.identity.access.AccessImpl;
import org.javaswift.joss.command.shared.factory.AccountCommandFactory;
import org.javaswift.joss.instructions.ListInstructions;
import org.javaswift.joss.model.Container;
import org.javaswift.joss.client.core.AbstractAccount;
import org.javaswift.joss.command.impl.identity.AuthenticationCommand;
import org.apache.http.client.HttpClient;

import java.util.Collection;

public class AccountImpl extends AbstractAccount {

    private AccountCommandFactory commandFactory = new AccountCommandFactoryImpl();
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
        return commandFactory.createListContainersCommand(this, httpClient, access, listInstructions).call();
    }

    public Container getContainer(String containerName) {
        return new ContainerImpl(this, containerName, isAllowCaching());
    }

    @Override
    protected void saveMetadata() {
        commandFactory.createAccountMetadataCommand(this, getClient(), getAccess(), info.getMetadata()).call();
    }

    protected void getInfo() {
        this.info = commandFactory.createAccountInformationCommand(this, httpClient, access).call();
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
