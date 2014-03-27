package org.javaswift.joss.client.impl;

import org.apache.http.client.HttpClient;
import org.javaswift.joss.client.core.AbstractAccount;
import org.javaswift.joss.client.factory.TempUrlHashPrefixSource;
import org.javaswift.joss.command.impl.factory.AccountCommandFactoryImpl;
import org.javaswift.joss.command.shared.identity.AuthenticationCommand;
import org.javaswift.joss.model.Access;

public class AccountImpl extends AbstractAccount {

    public AccountImpl(AuthenticationCommand command, HttpClient httpClient, Access access,
                       boolean allowCaching, TempUrlHashPrefixSource tempUrlHashPrefixSource,
                       Character delimiter) {
        super(
                new AccountCommandFactoryImpl(httpClient, access, command, tempUrlHashPrefixSource, delimiter),
                new ContainerFactoryImpl(),
                new WebsiteFactoryImpl(),
                allowCaching
        );
    }

}
