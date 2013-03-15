package org.javaswift.joss.headers.account;

import org.javaswift.joss.headers.Metadata;
import org.apache.http.HttpResponse;

import java.util.Map;
import java.util.TreeMap;

public class AccountMetadata extends Metadata {

    public static final String X_ACCOUNT_META_PREFIX = "X-Account-Meta-";

    public AccountMetadata(String name, String value) {
        super(name, value);
    }

    @Override
    public String getHeaderName() {
        return X_ACCOUNT_META_PREFIX + getName();
    }

    public static Map<String, Metadata> fromResponse(HttpResponse response) {
        Map<String, Metadata> metadata = new TreeMap<String, Metadata>();
        for (org.apache.http.Header header : getResponseHeadersStartingWith(response, X_ACCOUNT_META_PREFIX)) {
            AccountMetadata accountMetadata = new AccountMetadata(header.getName().substring(X_ACCOUNT_META_PREFIX.length()), header.getValue());
            metadata.put(accountMetadata.getName(), accountMetadata);
        }
        return metadata;
    }
}
