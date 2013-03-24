package org.javaswift.joss.command.impl.factory;

import org.apache.http.client.HttpClient;
import org.javaswift.joss.command.impl.container.*;
import org.javaswift.joss.command.impl.identity.access.AccessImpl;
import org.javaswift.joss.command.shared.container.*;
import org.javaswift.joss.command.shared.factory.ContainerCommandFactory;
import org.javaswift.joss.headers.Header;
import org.javaswift.joss.instructions.ListInstructions;
import org.javaswift.joss.model.Account;
import org.javaswift.joss.model.Container;

import java.util.Collection;

public class ContainerCommandFactoryImpl implements ContainerCommandFactory {

    @Override
    public ContainerInformationCommand createContainerInformationCommand(Account account, HttpClient httpClient, AccessImpl access, Container container) {
        return new ContainerInformationCommandImpl(account, httpClient, access, container);
    }

    @Override
    public ContainerMetadataCommand createContainerMetadataCommand(Account account, HttpClient httpClient, AccessImpl access, Container container, Collection<? extends Header> headers) {
        return new ContainerMetadataCommandImpl(account, httpClient, access, container, headers);
    }

    @Override
    public ContainerRightsCommand createContainerRightsCommand(Account account, HttpClient httpClient, AccessImpl access, Container container, boolean publicContainer) {
        return new ContainerRightsCommandImpl(account, httpClient, access, container, publicContainer);
    }

    @Override
    public CreateContainerCommand createCreateContainerCommand(Account account, HttpClient httpClient, AccessImpl access, Container container) {
        return new CreateContainerCommandImpl(account, httpClient, access, container);
    }

    @Override
    public DeleteContainerCommand createDeleteContainerCommand(Account account, HttpClient httpClient, AccessImpl access, Container container) {
        return new DeleteContainerCommandImpl(account, httpClient, access, container);
    }

    @Override
    public ListObjectsCommand createListObjectsCommand(Account account, HttpClient httpClient, AccessImpl access, Container container, ListInstructions listInstructions) {
        return new ListObjectsCommandImpl(account, httpClient, access, container, listInstructions);
    }
}
