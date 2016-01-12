package org.javaswift.joss.client.factory;

import org.javaswift.joss.model.Access;

/**
 * See <a href="http://docs.openstack.org/api/openstack-object-storage/1.0/content/authentication-object-dev-guide.html#d6e234">BASIC</a>:
 *         basic authentication is described as the first option in the Swift API docs.
 *
 * See <a href="http://docs.openstack.org/api/openstack-identity-service/2.0/content/POST_authenticate_v2.0_tokens_.html">KEYSTONE</a>:
 *         when you make use of Keystone as an identity service, you will use this option.
 *
 * See <a href="https://github.com/openstack/swift/blob/master/swift/common/middleware/tempauth.py">TEMPAUTH</a>: if you have TempAuth enabled, you will use this option. Looks very similar to Basic authentication
 *
 * EXTERNAL: an implementation of the interface AccessProvider must be provided
 */
public enum AuthenticationMethod {
    BASIC,
    KEYSTONE,
    TEMPAUTH,
    EXTERNAL;
    
    public static interface AccessProvider {
    	public Access authenticate () ;
    }
}
