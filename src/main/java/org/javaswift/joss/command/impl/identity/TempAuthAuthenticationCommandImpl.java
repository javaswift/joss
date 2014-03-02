package org.javaswift.joss.command.impl.identity;

import org.apache.http.client.HttpClient;
import org.javaswift.joss.headers.identity.XStoragePass;
import org.javaswift.joss.headers.identity.XStorageUser;

public class TempAuthAuthenticationCommandImpl extends AbstractSimpleAuthenticationCommandImpl {

    public TempAuthAuthenticationCommandImpl(HttpClient httpClient, String url, String username, String password) {
        super(httpClient, url);
        setHeader(new XStorageUser(username));
        setHeader(new XStoragePass(password));
    }

}
