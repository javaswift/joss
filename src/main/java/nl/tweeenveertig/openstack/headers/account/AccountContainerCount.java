package nl.tweeenveertig.openstack.headers.account;

import nl.tweeenveertig.openstack.headers.Header;
import nl.tweeenveertig.openstack.headers.SimpleHeader;

public class AccountContainerCount extends SimpleHeader {

    public static final String X_ACCOUNT_CONTAINER_COUNT  = "X-Account-Container-Count";

    public AccountContainerCount(String value) {
        super(value);
    }

    @Override
    public String getHeaderName() {
        return X_ACCOUNT_CONTAINER_COUNT;
    }
}
