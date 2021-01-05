package org.javaswift.joss.command.impl.factory;

import static org.javaswift.joss.client.factory.AuthenticationMethod.BASIC;
import static org.javaswift.joss.client.factory.AuthenticationMethod.EXTERNAL;
import static org.javaswift.joss.client.factory.AuthenticationMethod.KEYSTONE;
import static org.javaswift.joss.client.factory.AuthenticationMethod.KEYSTONE_V3;
import static org.javaswift.joss.client.factory.AuthenticationMethod.TEMPAUTH;
import static org.junit.Assert.assertEquals;

import org.javaswift.joss.client.factory.AccountConfig;
import org.javaswift.joss.client.factory.AuthenticationMethod;
import org.javaswift.joss.command.impl.identity.BasicAuthenticationCommandImpl;
import org.javaswift.joss.command.impl.identity.ExternalAuthenticationCommandImpl;
import org.javaswift.joss.command.impl.identity.KeystoneAuthenticationCommandImpl;
import org.javaswift.joss.command.impl.identity.KeystoneAuthenticationV3CommandImpl;
import org.javaswift.joss.command.impl.identity.TempAuthAuthenticationCommandImpl;
import org.javaswift.joss.model.Access;
import org.junit.Test;

public class AuthenticationCommandFactoryImplTest {

    @Test
    public void variousAuthenticationMechanisms() {
        AuthenticationCommandFactoryImpl factory = new AuthenticationCommandFactoryImpl();
        assertAuthenticationMechanism(factory, BASIC,    BasicAuthenticationCommandImpl.class);
        assertAuthenticationMechanism(factory, TEMPAUTH, TempAuthAuthenticationCommandImpl.class);
        assertAuthenticationMechanism(factory, KEYSTONE, KeystoneAuthenticationCommandImpl.class);
        assertAuthenticationMechanism(factory, KEYSTONE_V3, KeystoneAuthenticationV3CommandImpl.class);
        assertAuthenticationMechanism(factory, null, KeystoneAuthenticationCommandImpl.class);
    }

    private void assertAuthenticationMechanism(AuthenticationCommandFactoryImpl  factory,
                                                 AuthenticationMethod authenticationMethod, Class instance) {
        AccountConfig config = new AccountConfig();
        config.setAuthUrl("http://127.0.0.1");
        config.setAuthenticationMethod(authenticationMethod);

        assertEquals(instance, factory.createAuthenticationCommand(null, config).getClass());
    }

    @Test
    public void externalAuthenticationMechanisms() {
        AccountConfig config = new AccountConfig();
        config.setAuthUrl("http://127.0.0.1");
        config.setAuthenticationMethod(EXTERNAL);
        config.setAccessProvider(new AuthenticationMethod.AccessProvider() {
            @Override
            public Access authenticate() {
                return null ;
            }
        });

        AuthenticationCommandFactoryImpl factory = new AuthenticationCommandFactoryImpl();
        assertEquals(ExternalAuthenticationCommandImpl.class, factory.createAuthenticationCommand(null, config).getClass());
    }
}
