package org.javaswift.joss.command.shared.factory;

import org.javaswift.joss.command.shared.account.*;
import org.javaswift.joss.headers.Header;
import org.javaswift.joss.instructions.ListInstructions;
import org.javaswift.joss.model.Access;
import org.javaswift.joss.model.Account;
import org.javaswift.joss.model.ObjectIdentifier;

import java.util.Collection;

public interface AccountCommandFactory {

    public Access authenticate();

    public Access getAccess();

    public String getPublicHost();

    public String getPrivateHost();

    public String getOriginalHost();

    public void setPublicHost(String publicHost);

    public void setPrivateHost(String privateHost);

    AccountInformationCommand createAccountInformationCommand(Account account);

    AccountMetadataCommand createAccountMetadataCommand(Account account, Collection<? extends Header> headers);

    ListContainersCommand createListContainersCommand(Account account, ListInstructions listInstructions);

    TenantCommand createTenantCommand(Account account);

    HashPasswordCommand createHashPasswordCommand(Account account, String hashPassword);
    
    BulkDeleteCommand createBulkDeleteCommand(Account account, Collection<ObjectIdentifier> objectsToDelete);
    
    ContainerCommandFactory getContainerCommandFactory();

    public boolean isTenantSupplied();

}
