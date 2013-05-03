package org.javaswift.joss.client.mock;

import org.javaswift.joss.client.core.AbstractAccount;
import org.javaswift.joss.command.mock.factory.AccountCommandFactoryMock;
import org.javaswift.joss.swift.Swift;

public class AccountMock extends AbstractAccount {

    public AccountMock(Swift swift) {
        super(
                new AccountCommandFactoryMock(swift),
                new ContainerFactoryMock(),
                new WebsiteFactoryMock(),
                ALLOW_CACHING
        );
    }

    public AccountMock() {
        this(new Swift());
    }

}
