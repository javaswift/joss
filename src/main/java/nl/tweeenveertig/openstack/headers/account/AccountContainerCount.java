package nl.tweeenveertig.openstack.headers.account;

import nl.tweeenveertig.openstack.headers.SimpleHeader;
import org.apache.http.HttpResponse;

public class AccountContainerCount extends SimpleHeader {

    public static final String X_ACCOUNT_CONTAINER_COUNT  = "X-Account-Container-Count";

    public AccountContainerCount(String value) {
        super(value);
    }

    @Override
    public String getHeaderName() {
        return X_ACCOUNT_CONTAINER_COUNT;
    }

    public static AccountContainerCount fromResponse(HttpResponse response) {
        return new AccountContainerCount(convertResponseHeader(response, X_ACCOUNT_CONTAINER_COUNT));
    }
}
