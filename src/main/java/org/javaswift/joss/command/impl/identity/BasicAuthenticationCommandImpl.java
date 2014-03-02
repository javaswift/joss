package org.javaswift.joss.command.impl.identity;

import org.apache.http.client.HttpClient;
import org.javaswift.joss.headers.identity.XAuthKey;
import org.javaswift.joss.headers.identity.XAuthUser;

public class BasicAuthenticationCommandImpl extends AbstractSimpleAuthenticationCommandImpl {

    public BasicAuthenticationCommandImpl(HttpClient httpClient, String url, String username, String password) {
        super(httpClient, url);
        setHeader(new XAuthUser(username));
        setHeader(new XAuthKey(password));
    }

}
