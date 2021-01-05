package org.javaswift.joss.command.mock.factory;

import java.util.Collection;

import org.javaswift.joss.command.mock.container.ContainerInformationCommandMock;
import org.javaswift.joss.command.mock.container.ContainerMetadataCommandMock;
import org.javaswift.joss.command.mock.container.ContainerRightsCommandMock;
import org.javaswift.joss.command.mock.container.CreateContainerCommandMock;
import org.javaswift.joss.command.mock.container.DeleteContainerCommandMock;
import org.javaswift.joss.command.mock.container.ListDirectoryCommandMock;
import org.javaswift.joss.command.mock.container.ListObjectsCommandMock;
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
import org.javaswift.joss.model.Account;
import org.javaswift.joss.model.Container;
import org.javaswift.joss.swift.Swift;

public class ContainerCommandFactoryMock implements ContainerCommandFactory {

    private final StoredObjectCommandFactory storedObjectCommandFactory;

    private final Swift swift;

    public ContainerCommandFactoryMock(Swift swift) {
        this.swift = swift;
        this.storedObjectCommandFactory = new StoredObjectCommandFactoryMock(swift);
    }

    @Override
    public ContainerInformationCommand createContainerInformationCommand(Account account, Container container, boolean allowErrorLog) {
        return new ContainerInformationCommandMock(swift, account, container, allowErrorLog);
    }

    @Override
    public ContainerMetadataCommand createContainerMetadataCommand(Account account, Container container, Collection<? extends Header> headers) {
        return new ContainerMetadataCommandMock(swift, account, container, headers);
    }

    @Override
    public ContainerRightsCommand createContainerRightsCommand(Account account, Container container, boolean publicContainer) {
        return new ContainerRightsCommandMock(swift, account, container, publicContainer);
    }

    @Override
    public CreateContainerCommand createCreateContainerCommand(Account account, Container container) {
        return new CreateContainerCommandMock(swift, account, container);
    }

    @Override
    public DeleteContainerCommand createDeleteContainerCommand(Account account, Container container) {
        return new DeleteContainerCommandMock(swift, account, container);
    }

    @Override
    public ListObjectsCommand createListObjectsCommand(Account account, Container container, ListInstructions listInstructions) {
        return new ListObjectsCommandMock(swift, account, container, listInstructions);
    }

    @Override
    public ListDirectoryCommand createListDirectoryCommand(Account account, Container container,
                                                           ListInstructions listInstructions, Character delimiter) {
        return new ListDirectoryCommandMock(swift, account, container, listInstructions);
    }

    @Override
    public StoredObjectCommandFactory getStoredObjectCommandFactory() {
        return this.storedObjectCommandFactory;
    }

    @Override
    public String getTempUrlPrefix() {
        return "";
    }

    @Override
    public Character getDelimiter() {
        return swift.getDelimiter();
    }

    @Override
    public ContainerRightsCommand createContainerRightsCommand(Account account,
            Container container, String writePermission, String readPermission) {
        // TODO Auto-generated method stub
        return null;
    }

}
