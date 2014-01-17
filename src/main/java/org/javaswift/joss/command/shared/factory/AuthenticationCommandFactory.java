package org.javaswift.joss.command.shared.factory;

import org.apache.http.client.HttpClient;
import org.javaswift.joss.client.factory.AuthenticationMethod;
import org.javaswift.joss.command.shared.identity.AuthenticationCommand;

public interface AuthenticationCommandFactory {

    AuthenticationCommand createAuthenticationCommand(HttpClient httpClient, AuthenticationMethod authenticationMethod,
                                                      String url, String tenantName, String tenantId,
                                                      String username, String password);

}
