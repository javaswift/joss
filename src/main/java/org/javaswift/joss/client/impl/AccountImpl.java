package org.javaswift.joss.client.impl;

import org.apache.http.client.HttpClient;
import org.javaswift.joss.client.core.AbstractAccount;
import org.javaswift.joss.command.impl.factory.AccountCommandFactoryImpl;
import org.javaswift.joss.command.shared.factory.AccountCommandFactory;
import org.javaswift.joss.command.shared.identity.AuthenticationCommand;
import org.javaswift.joss.command.shared.identity.access.AccessImpl;
import org.javaswift.joss.instructions.ListInstructions;
import org.javaswift.joss.model.Container;

import java.util.Collection;

public class AccountImpl extends AbstractAccount {

    public AccountImpl(AuthenticationCommand command, HttpClient httpClient, AccessImpl access,
                       String host, boolean allowCaching) {
        super(new AccountCommandFactoryImpl(httpClient, access, command, host), allowCaching);
    }

    public Container getContainer(String containerName) {
        return new ContainerImpl(this, containerName, isAllowCaching());
    }

}
