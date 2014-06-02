package org.javaswift.joss.command.impl.factory;

import org.javaswift.joss.client.factory.AuthenticationMethod;
import org.javaswift.joss.command.impl.identity.BasicAuthenticationCommandImpl;
import org.javaswift.joss.command.impl.identity.ExternalAuthenticationCommandImpl;
import org.javaswift.joss.command.impl.identity.KeystoneAuthenticationCommandImpl;
import org.javaswift.joss.command.impl.identity.TempAuthAuthenticationCommandImpl;
import org.javaswift.joss.model.Access;
import org.junit.Test;

import static org.javaswift.joss.client.factory.AuthenticationMethod.BASIC;
import static org.javaswift.joss.client.factory.AuthenticationMethod.KEYSTONE;
import static org.javaswift.joss.client.factory.AuthenticationMethod.TEMPAUTH;
import static org.javaswift.joss.client.factory.AuthenticationMethod.EXTERNAL;
import static org.junit.Assert.assertEquals;

public class AuthenticationCommandFactoryImplTest {

    @Test
    public void variousAuthenticationMechanisms() {
        AuthenticationCommandFactoryImpl factory = new AuthenticationCommandFactoryImpl();
        assertAuthenticationMechanism(factory, BASIC,    BasicAuthenticationCommandImpl.class);
        assertAuthenticationMechanism(factory, TEMPAUTH, TempAuthAuthenticationCommandImpl.class);
        assertAuthenticationMechanism(factory, KEYSTONE, KeystoneAuthenticationCommandImpl.class);
        assertAuthenticationMechanism(factory, null, KeystoneAuthenticationCommandImpl.class);
    }

    protected void assertAuthenticationMechanism(AuthenticationCommandFactoryImpl  factory,
                                                 AuthenticationMethod authenticationMethod, Class instance) {
        assertEquals(instance, factory.createAuthenticationCommand(null, authenticationMethod, "http://nowhere.com", null, null, null, null, null).getClass());
    }

    @Test
    public void externalAuthenticationMechanisms() {
        AuthenticationCommandFactoryImpl factory = new AuthenticationCommandFactoryImpl();
        assertEquals(ExternalAuthenticationCommandImpl.class, factory.createAuthenticationCommand(null, EXTERNAL, "http://nowhere.com", null, null, null, null, new AuthenticationMethod.AccessProvider() {
			@Override
			public Access authenticate() {
				return null ;
			}
		}).getClass());
    }
}
