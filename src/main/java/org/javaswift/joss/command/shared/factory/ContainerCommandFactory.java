package org.javaswift.joss.command.shared.factory;

import java.util.Collection;

import org.javaswift.joss.command.shared.container.ContainerInformationCommand;
import org.javaswift.joss.command.shared.container.ContainerMetadataCommand;
import org.javaswift.joss.command.shared.container.ContainerRightsCommand;
import org.javaswift.joss.command.shared.container.CreateContainerCommand;
import org.javaswift.joss.command.shared.container.DeleteContainerCommand;
import org.javaswift.joss.command.shared.container.ListDirectoryCommand;
import org.javaswift.joss.command.shared.container.ListObjectsCommand;
import org.javaswift.joss.headers.Header;
import org.javaswift.joss.instructions.ListInstructions;
import org.javaswift.joss.model.Account;
import org.javaswift.joss.model.Container;

public interface ContainerCommandFactory {

    ContainerInformationCommand createContainerInformationCommand(Account account, Container container, boolean allowErrorLog);

    ContainerMetadataCommand createContainerMetadataCommand(Account account, Container container,
                                                            Collection<? extends Header> headers);

    ContainerRightsCommand createContainerRightsCommand(Account account, Container container, boolean publicContainer);
    
    ContainerRightsCommand createContainerRightsCommand(Account account, Container container, String writePermission, String readPermission);

    CreateContainerCommand createCreateContainerCommand(Account account, Container container);

    DeleteContainerCommand createDeleteContainerCommand(Account account, Container container);

    ListObjectsCommand createListObjectsCommand(Account account, Container container, ListInstructions listInstructions);

    ListDirectoryCommand createListDirectoryCommand(Account account, Container container,
                                                    ListInstructions listInstructions, Character delimiter);

    StoredObjectCommandFactory getStoredObjectCommandFactory();

    public String getTempUrlPrefix();

    public Character getDelimiter();

}
