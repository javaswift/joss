package org.javaswift.joss.command.impl.factory;

import static org.javaswift.joss.client.factory.AuthenticationMethod.BASIC;
import static org.javaswift.joss.client.factory.AuthenticationMethod.EXTERNAL;
import static org.javaswift.joss.client.factory.AuthenticationMethod.KEYSTONE_V3;
import static org.javaswift.joss.client.factory.AuthenticationMethod.TEMPAUTH;

import org.apache.http.client.HttpClient;
import org.javaswift.joss.client.factory.AccountConfig;
import org.javaswift.joss.command.impl.identity.BasicAuthenticationCommandImpl;
import org.javaswift.joss.command.impl.identity.ExternalAuthenticationCommandImpl;
import org.javaswift.joss.command.impl.identity.KeystoneAuthenticationCommandImpl;
import org.javaswift.joss.command.impl.identity.KeystoneAuthenticationV3CommandImpl;
import org.javaswift.joss.command.impl.identity.TempAuthAuthenticationCommandImpl;
import org.javaswift.joss.command.shared.factory.AuthenticationCommandFactory;
import org.javaswift.joss.command.shared.identity.AuthenticationCommand;

public class AuthenticationCommandFactoryImpl implements AuthenticationCommandFactory {

    @Override
    public AuthenticationCommand createAuthenticationCommand(HttpClient httpClient, AccountConfig config) {
        String url = config.getAuthUrl();
        String tenantName = config.getTenantName();
        String username = config.getUsername();
        String password = config.getPassword();

        if (config.getAuthenticationMethod() == BASIC) {
            return new BasicAuthenticationCommandImpl(httpClient, url, username, password, tenantName);
        } else if (config.getAuthenticationMethod() == TEMPAUTH) {
            return new TempAuthAuthenticationCommandImpl(httpClient, url, username, password, tenantName);
        } else if (config.getAuthenticationMethod() == EXTERNAL) {
        	return new ExternalAuthenticationCommandImpl (httpClient, url, config.getAccessProvider()) ;
        } else if (config.getAuthenticationMethod() == KEYSTONE_V3) {
            return new KeystoneAuthenticationV3CommandImpl(httpClient, config);
        } else { // KEYSTONE
            return new KeystoneAuthenticationCommandImpl(httpClient, url, tenantName, config.getTenantId(), username, password);
        }
    }

}
