package org.javaswift.joss.command.impl.factory;

import java.util.Collection;

import org.apache.http.client.HttpClient;
import org.javaswift.joss.client.factory.TempUrlHashPrefixSource;
import org.javaswift.joss.command.impl.container.ContainerInformationCommandImpl;
import org.javaswift.joss.command.impl.container.ContainerMetadataCommandImpl;
import org.javaswift.joss.command.impl.container.ContainerRightsCommandImpl;
import org.javaswift.joss.command.impl.container.CreateContainerCommandImpl;
import org.javaswift.joss.command.impl.container.DeleteContainerCommandImpl;
import org.javaswift.joss.command.impl.container.ListDirectoryCommandImpl;
import org.javaswift.joss.command.impl.container.ListObjectsCommandImpl;
import org.javaswift.joss.command.shared.container.ContainerInformationCommand;
import org.javaswift.joss.command.shared.container.ContainerMetadataCommand;
import org.javaswift.joss.command.shared.container.ContainerRightsCommand;
import org.javaswift.joss.command.shared.container.CreateContainerCommand;
import org.javaswift.joss.command.shared.container.DeleteContainerCommand;
import org.javaswift.joss.command.shared.container.ListDirectoryCommand;
import org.javaswift.joss.command.shared.container.ListObjectsCommand;
import org.javaswift.joss.command.shared.factory.ContainerCommandFactory;
import org.javaswift.joss.command.shared.factory.StoredObjectCommandFactory;
import org.javaswift.joss.headers.Header;
import org.javaswift.joss.instructions.ListInstructions;
import org.javaswift.joss.model.Access;
import org.javaswift.joss.model.Account;
import org.javaswift.joss.model.Container;

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
    public ContainerRightsCommand createContainerRightsCommand(Account account, Container container, String writePermissions, String readPermissions) {
        return new ContainerRightsCommandImpl(account, getHttpClient(), getAccess(), container, writePermissions, readPermissions);
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

    @Override
    public ListDirectoryCommand createListDirectoryCommand(Account account, Container container,
                                                           ListInstructions listInstructions, Character delimiter) {
        return new ListDirectoryCommandImpl(account, getHttpClient(), getAccess(), container, listInstructions, delimiter);
    }

    public HttpClient getHttpClient() {
        return accountCommandFactory.getHttpClient();
    }

    public Access getAccess() {
        return accountCommandFactory.getAccess();
    }

    @Override
    public StoredObjectCommandFactory getStoredObjectCommandFactory() {
        return this.storedObjectCommandFactory;
    }

    @Override
    public String getTempUrlPrefix() {
        TempUrlHashPrefixSource source = accountCommandFactory.getTempUrlHashPrefixSource();
        return getAccess().getTempUrlPrefix(source);
    }

    @Override
    public Character getDelimiter() {
        return accountCommandFactory.getDelimiter();
    }

}
