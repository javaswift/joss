package org.javaswift.joss.command.mock.factory;

import org.javaswift.joss.command.mock.container.*;
import org.javaswift.joss.command.shared.container.*;
import org.javaswift.joss.command.shared.factory.ContainerCommandFactory;
import org.javaswift.joss.command.shared.factory.StoredObjectCommandFactory;
import org.javaswift.joss.headers.Header;
import org.javaswift.joss.instructions.ListInstructions;
import org.javaswift.joss.model.Account;
import org.javaswift.joss.model.Container;
import org.javaswift.joss.swift.Swift;

import java.util.Collection;

public class ContainerCommandFactoryMock implements ContainerCommandFactory {

    private final StoredObjectCommandFactory storedObjectCommandFactory;

    private final Swift swift;

    public ContainerCommandFactoryMock(Swift swift) {
        this.swift = swift;
        this.storedObjectCommandFactory = new StoredObjectCommandFactoryMock(swift);
    }

    @Override
    public ContainerInformationCommand createContainerInformationCommand(Account account, Container container) {
        return new ContainerInformationCommandMock(swift, account, container);
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
    public StoredObjectCommandFactory getStoredObjectCommandFactory() {
        return this.storedObjectCommandFactory;
    }
}
