package org.javaswift.joss.command.shared.factory;

import org.javaswift.joss.command.shared.account.AccountInformationCommand;
import org.javaswift.joss.command.shared.account.AccountMetadataCommand;
import org.javaswift.joss.command.shared.account.ListContainersCommand;
import org.javaswift.joss.command.shared.account.TenantCommand;
import org.javaswift.joss.command.shared.identity.access.AccessImpl;
import org.javaswift.joss.headers.Header;
import org.javaswift.joss.instructions.ListInstructions;
import org.javaswift.joss.model.Account;

import java.util.Collection;

public interface AccountCommandFactory {

    public AccessImpl authenticate();

    public String getPublicHost();

    public String getPrivateHost();

    public String getOriginalHost();

    public void setPublicHost(String publicHost);

    public void setPrivateHost(String privateHost);

    AccountInformationCommand createAccountInformationCommand(Account account);

    AccountMetadataCommand createAccountMetadataCommand(Account account, Collection<? extends Header> headers);

    ListContainersCommand createListContainersCommand(Account account, ListInstructions listInstructions);

    TenantCommand createTenantCommand(Account account);

    ContainerCommandFactory getContainerCommandFactory();

    public boolean isTenantSupplied();

}
