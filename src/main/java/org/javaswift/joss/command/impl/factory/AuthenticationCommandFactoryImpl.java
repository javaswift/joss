package org.javaswift.joss.command.impl.factory;

import org.apache.http.client.HttpClient;
import org.javaswift.joss.command.impl.identity.AuthenticationCommandImpl;
import org.javaswift.joss.command.shared.factory.AuthenticationCommandFactory;
import org.javaswift.joss.command.shared.identity.AuthenticationCommand;

public class AuthenticationCommandFactoryImpl implements AuthenticationCommandFactory {

    @Override
    public AuthenticationCommand createAuthenticationCommand(HttpClient httpClient, String url, String tenantName,
                                                             String tenantId, String username, String password) {
        return new AuthenticationCommandImpl(httpClient, url, tenantName, tenantId, username, password);
    }

}
