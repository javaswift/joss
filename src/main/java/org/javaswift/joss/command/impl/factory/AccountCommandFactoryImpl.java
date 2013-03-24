package org.javaswift.joss.command.impl.factory;

import org.apache.http.client.HttpClient;
import org.javaswift.joss.command.impl.account.AccountInformationCommandImpl;
import org.javaswift.joss.command.impl.account.AccountMetadataCommandImpl;
import org.javaswift.joss.command.impl.account.ListContainersCommandImpl;
import org.javaswift.joss.command.impl.identity.access.AccessImpl;
import org.javaswift.joss.command.shared.account.AccountInformationCommand;
import org.javaswift.joss.command.shared.account.AccountMetadataCommand;
import org.javaswift.joss.command.shared.account.ListContainersCommand;
import org.javaswift.joss.command.shared.factory.AccountCommandFactory;
import org.javaswift.joss.headers.Header;
import org.javaswift.joss.instructions.ListInstructions;
import org.javaswift.joss.model.Account;

import java.util.Collection;

public class AccountCommandFactoryImpl implements AccountCommandFactory {

    @Override
    public AccountInformationCommand createAccountInformationCommand(Account account, HttpClient httpClient, AccessImpl access) {
        return new AccountInformationCommandImpl(account, httpClient, access);
    }

    @Override
    public AccountMetadataCommand createAccountMetadataCommand(Account account, HttpClient httpClient, AccessImpl access, Collection<? extends Header> headers) {
        return new AccountMetadataCommandImpl(account, httpClient, access, headers);
    }

    @Override
    public ListContainersCommand createListContainersCommand(Account account, HttpClient httpClient, AccessImpl access, ListInstructions listInstructions) {
        return new ListContainersCommandImpl(account, httpClient, access, listInstructions);
    }
}
