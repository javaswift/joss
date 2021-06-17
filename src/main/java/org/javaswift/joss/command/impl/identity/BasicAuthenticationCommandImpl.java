package org.javaswift.joss.command.impl.identity;

import org.apache.http.client.HttpClient;
import org.javaswift.joss.headers.identity.XAuthKey;
import org.javaswift.joss.headers.identity.XAuthUser;

public class BasicAuthenticationCommandImpl extends AbstractSimpleAuthenticationCommandImpl {

    public BasicAuthenticationCommandImpl(HttpClient httpClient, String url, String username,
                                          String password, String tenantName) {
        super(httpClient, url);
        setHeader(new XAuthUser(determineCompoundUsername(username, tenantName)));
        setHeader(new XAuthKey(password));
    }

    @Override
    protected String determineCompoundUsername(String username, String tenantName) {
        return tenantName == null ? username : tenantName + ":" + username;
    }
}
