package org.javaswift.joss.command.impl.factory;

import org.apache.http.client.HttpClient;
import org.javaswift.joss.command.impl.container.*;
import org.javaswift.joss.command.shared.container.*;
import org.javaswift.joss.command.shared.factory.ContainerCommandFactory;
import org.javaswift.joss.command.shared.factory.StoredObjectCommandFactory;
import org.javaswift.joss.command.shared.identity.access.AccessImpl;
import org.javaswift.joss.headers.Header;
import org.javaswift.joss.instructions.ListInstructions;
import org.javaswift.joss.model.Account;
import org.javaswift.joss.model.Container;

import java.util.Collection;

public class ContainerCommandFactoryImpl implements ContainerCommandFactory {

    private final StoredObjectCommandFactory storedObjectCommandFactory;

    private AccountCommandFactoryImpl accountCommandFactory;
    
    public ContainerCommandFactoryImpl(AccountCommandFactoryImpl accountCommandFactory) {
        this.accountCommandFactory = accountCommandFactory;
        this.storedObjectCommandFactory = new StoredObjectCommandFactoryImpl(this);
    }

    @Override
    public ContainerInformationCommand createContainerInformationCommand(Account account, Container container, boolean allowErrorLog) {
        return new ContainerInformationCommandImpl(account, getHttpClient(), getAccess(), container, allowErrorLog);
    }

    @Override
    public ContainerMetadataCommand createContainerMetadataCommand(Account account, Container container, Collection<? extends Header> headers) {
        return new ContainerMetadataCommandImpl(account, getHttpClient(), getAccess(), container, headers);
    }

    @Override
    public ContainerRightsCommand createContainerRightsCommand(Account account, Container container, boolean publicContainer) {
        return new ContainerRightsCommandImpl(account, getHttpClient(), getAccess(), container, publicContainer);
    }

    @Override
    public CreateContainerCommand createCreateContainerCommand(Account account, Container container) {
        return new CreateContainerCommandImpl(account, getHttpClient(), getAccess(), container);
    }

    @Override
    public DeleteContainerCommand createDeleteContainerCommand(Account account, Container container) {
        return new DeleteContainerCommandImpl(account, getHttpClient(), getAccess(), container);
    }

    @Override
    public ListObjectsCommand createListObjectsCommand(Account account, Container container, ListInstructions listInstructions) {
        return new ListObjectsCommandImpl(account, getHttpClient(), getAccess(), container, listInstructions);
    }

    public HttpClient getHttpClient() {
        return accountCommandFactory.getHttpClient();
    }

    public AccessImpl getAccess() {
        return accountCommandFactory.getAccess();
    }

    @Override
    public StoredObjectCommandFactory getStoredObjectCommandFactory() {
        return this.storedObjectCommandFactory;
    }
}
