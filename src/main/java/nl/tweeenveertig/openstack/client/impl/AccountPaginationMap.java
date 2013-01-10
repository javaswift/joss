package nl.tweeenveertig.openstack.client.impl;

import nl.tweeenveertig.openstack.model.Account;
import nl.tweeenveertig.openstack.model.Container;

public class AccountPaginationMap extends AbstractPaginationMap<Container> {

    public AccountPaginationMap(Account account, String prefix, Integer pageSize) {
        super(account, prefix, pageSize);
    }

}
