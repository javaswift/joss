package org.javaswift.joss.command.shared.factory;

import org.apache.http.client.HttpClient;
import org.javaswift.joss.command.impl.identity.access.AccessImpl;
import org.javaswift.joss.command.shared.container.*;
import org.javaswift.joss.headers.Header;
import org.javaswift.joss.instructions.ListInstructions;
import org.javaswift.joss.model.Account;
import org.javaswift.joss.model.Container;

import java.util.Collection;

public interface ContainerCommandFactory {

    ContainerInformationCommand createContainerInformationCommand(Account account, HttpClient httpClient,
                                                                  AccessImpl access, Container container);

    ContainerMetadataCommand createContainerMetadataCommand(Account account, HttpClient httpClient,
                                                            AccessImpl access, Container container,
                                                            Collection<? extends Header> headers);

    ContainerRightsCommand createContainerRightsCommand(Account account, HttpClient httpClient, AccessImpl access,
                                                        Container container, boolean publicContainer);

    CreateContainerCommand createCreateContainerCommand(Account account, HttpClient httpClient,
                                                        AccessImpl access, Container container);

    DeleteContainerCommand createDeleteContainerCommand(Account account, HttpClient httpClient,
                                                        AccessImpl access, Container container);

    ListObjectsCommand createListObjectsCommand(Account account, HttpClient httpClient, AccessImpl access,
                                                Container container, ListInstructions listInstructions);

}
