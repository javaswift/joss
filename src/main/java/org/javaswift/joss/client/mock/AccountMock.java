package org.javaswift.joss.client.mock;

import org.javaswift.joss.client.core.AbstractAccount;
import org.javaswift.joss.command.mock.factory.AccountCommandFactoryMock;
import org.javaswift.joss.model.Container;
import org.javaswift.joss.swift.Swift;

public class AccountMock extends AbstractAccount {

    public AccountMock(Swift swift) {
        super(new AccountCommandFactoryMock(swift), ALLOW_CACHING);
    }

    public AccountMock() {
        this(new Swift());
    }

    @Override
    public Container getContainer(String name) {
        return new ContainerMock(this, name);
    }

}
