package org.javaswift.joss.headers.account;

import org.javaswift.joss.headers.SimpleHeader;
import org.apache.http.HttpResponse;

public class AccountObjectCount extends SimpleHeader {

    public static final String X_ACCOUNT_OBJECT_COUNT     = "X-Account-Object-Count";

    public AccountObjectCount(String value) {
        super(value);
    }

    @Override
    public String getHeaderName() {
        return X_ACCOUNT_OBJECT_COUNT;
    }

    public static AccountObjectCount fromResponse(HttpResponse response) {
        return new AccountObjectCount(convertResponseHeader(response, X_ACCOUNT_OBJECT_COUNT));
    }
}
