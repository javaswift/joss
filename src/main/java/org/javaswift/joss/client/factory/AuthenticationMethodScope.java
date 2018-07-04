package org.javaswift.joss.client.factory;

/**
 * The Keystone V3 API allows scoped authentication. By utilizing this option you can request a special scope during
 * authentication.
 *
 * Depending on the chosen AuthenticationMethodScope different options must be provided in the {@link AccountConfig}
 * object:
 *
 * <p>PROJECT_NAME: TenantName and Domain must be provided.</p>
 * <p>DOMAIN_NAME: Domain must be provided.</p>
 *
 * <b>Note:</b> Currently only project/tenant name and domain name are supported. Scoping to the project/tenant ID or
 * domain ID is not implemented.
 */
public enum AuthenticationMethodScope {
    DEFAULT,
    PROJECT_NAME,
    DOMAIN_NAME
}
