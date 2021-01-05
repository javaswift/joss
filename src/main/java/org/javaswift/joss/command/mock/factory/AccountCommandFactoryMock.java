package org.javaswift.joss.command.mock.factory;

import java.util.Collection;

import org.javaswift.joss.command.mock.account.AccountInformationCommandMock;
import org.javaswift.joss.command.mock.account.AccountMetadataCommandMock;
import org.javaswift.joss.command.mock.account.HashPasswordCommandMock;
import org.javaswift.joss.command.mock.account.ListContainersCommandMock;
import org.javaswift.joss.command.mock.account.TenantCommandMock;
import org.javaswift.joss.command.shared.account.AccountInformationCommand;
import org.javaswift.joss.command.shared.account.AccountMetadataCommand;
import org.javaswift.joss.command.shared.account.HashPasswordCommand;
import org.javaswift.joss.command.shared.account.ListContainersCommand;
import org.javaswift.joss.command.shared.account.TenantCommand;
import org.javaswift.joss.command.shared.factory.AccountCommandFactory;
import org.javaswift.joss.command.shared.factory.ContainerCommandFactory;
import org.javaswift.joss.headers.Header;
import org.javaswift.joss.instructions.ListInstructions;
import org.javaswift.joss.model.Access;
import org.javaswift.joss.model.Account;
import org.javaswift.joss.swift.Swift;

public class AccountCommandFactoryMock implements AccountCommandFactory {

    private final ContainerCommandFactory containerCommandFactory;

    private final Swift swift;

    public AccountCommandFactoryMock(Swift swift) {
        this.swift = swift;
        this.containerCommandFactory = new ContainerCommandFactoryMock(this.swift);
    }

    @Override
    public void setPublicHost(String publicHost) {
        this.swift.setPublicHost(publicHost);
    }

    @Override
    public void setPrivateHost(String privateHost) {
        this.swift.setPrivateHost(privateHost);
    }

    @Override
    public String getPublicHost() {
        return swift.getPublicHost();
    }

    @Override
    public String getPrivateHost() {
        return swift.getPrivateHost();
    }

    @Override
    public String getOriginalHost() {
        return swift.getPublicHost();
    }

    @Override
    public Access authenticate() {
        return null;
    }

    @Override
    public Access getAccess() {
        return null;
    }

    @Override
    public AccountInformationCommand createAccountInformationCommand(Account account) {
        return new AccountInformationCommandMock(swift, account);
    }

    @Override
    public AccountMetadataCommand createAccountMetadataCommand(Account account, Collection<? extends Header> headers) {
        return new AccountMetadataCommandMock(swift, account, headers);
    }

    @Override
    public ListContainersCommand createListContainersCommand(Account account, ListInstructions listInstructions) {
        return new ListContainersCommandMock(swift, account, listInstructions);
    }

    @Override
    public TenantCommand createTenantCommand(Account account) {
        return new TenantCommandMock(swift, account);
    }

    @Override
    public HashPasswordCommand createHashPasswordCommand(Account account, String hashPassword) {
        return new HashPasswordCommandMock(swift, account, hashPassword);
    }

    @Override
    public ContainerCommandFactory getContainerCommandFactory() {
        return this.containerCommandFactory;
    }

    @Override
    public boolean isTenantSupplied() {
        return this.swift.isTenantSupplied();
    }

    public Swift getSwift() {
        return this.swift;
    }
}
