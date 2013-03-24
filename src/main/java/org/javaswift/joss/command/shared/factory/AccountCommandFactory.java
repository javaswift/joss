package org.javaswift.joss.command.shared.factory;

import org.apache.http.client.HttpClient;
import org.javaswift.joss.command.shared.identity.access.AccessImpl;
import org.javaswift.joss.command.shared.account.AccountInformationCommand;
import org.javaswift.joss.command.shared.account.AccountMetadataCommand;
import org.javaswift.joss.command.shared.account.ListContainersCommand;
import org.javaswift.joss.headers.Header;
import org.javaswift.joss.instructions.ListInstructions;
import org.javaswift.joss.model.Account;

import java.util.Collection;

public interface AccountCommandFactory {

    AccountInformationCommand createAccountInformationCommand(Account account, HttpClient httpClient, AccessImpl access);

    AccountMetadataCommand createAccountMetadataCommand(Account account, HttpClient httpClient, AccessImpl access,
                                                        Collection<? extends Header> headers);

    ListContainersCommand createListContainersCommand(Account account, HttpClient httpClient, AccessImpl access,
                                                      ListInstructions listInstructions);

}
