package org.javaswift.joss.command.shared.identity.access;

import org.apache.http.HttpStatus;
import org.javaswift.joss.exception.CommandExceptionError;
import org.javaswift.joss.exception.HttpStatusExceptionUtil;
import org.javaswift.joss.model.Access;

public abstract class AbstractAccess implements Access {

    public static final String SERVICE_CATALOG_OBJECT_STORE = "object-store";

    public Token token;

    public User user;

    public Metadata metadata;

    private String preferredRegion;

    protected abstract EndPoint determineCurrentEndPoint();

    public abstract boolean isTenantSupplied();

    public String getToken() {
        return token == null ? null : token.id;
    }

    public String getPreferredRegion() {
        return this.preferredRegion;
    }

    public void setPreferredRegion(String preferredRegion) {
        this.preferredRegion = preferredRegion;
    }

    protected EndPoint getCurrentEndPoint() {
        if (!isTenantSupplied()) {
            HttpStatusExceptionUtil.throwException(HttpStatus.SC_NOT_FOUND, CommandExceptionError.NO_TENANT_SUPPLIED);
        }
        return determineCurrentEndPoint();
    }

    public String getInternalURL() {
        return getCurrentEndPoint().internalURL;
    }

    public String getPublicURL() {
        return getCurrentEndPoint().publicURL;
    }

}
