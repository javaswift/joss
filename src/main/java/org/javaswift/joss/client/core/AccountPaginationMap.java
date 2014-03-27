package org.javaswift.joss.client.core;

import org.javaswift.joss.model.Account;
import org.javaswift.joss.model.Container;

public class AccountPaginationMap extends AbstractListHolderPaginationMap<Container> {

    public AccountPaginationMap(Account account, String prefix, Integer pageSize) {
        super(account, prefix, pageSize);
    }

}
