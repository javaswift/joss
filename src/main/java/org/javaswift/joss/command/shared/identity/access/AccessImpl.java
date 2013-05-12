package org.javaswift.joss.command.shared.identity.access;

import org.apache.http.HttpStatus;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonRootName;
import org.javaswift.joss.exception.CommandExceptionError;
import org.javaswift.joss.exception.HttpStatusExceptionUtil;
import org.javaswift.joss.model.Access;

import java.util.ArrayList;
import java.util.List;

@JsonRootName(value="access")
@JsonIgnoreProperties(ignoreUnknown = true)
public class AccessImpl implements Access {

    public static final String SERVICE_CATALOG_OBJECT_STORE = "object-store";
    public Token token;

    public List<ServiceCatalog> serviceCatalog = new ArrayList<ServiceCatalog>();

    public User user;

    public Metadata metadata;

    @JsonIgnore
    private EndPoint currentEndPoint;

    private boolean tenantSupplied;

    private String preferredRegion;

    public void setPreferredRegion(String preferredRegion) {
        this.preferredRegion = preferredRegion;
    }

    public String getToken() {
        return token == null ? null : token.id;
    }

    public ServiceCatalog getObjectStoreCatalog() {
        for (ServiceCatalog catalog : serviceCatalog) {
            if (SERVICE_CATALOG_OBJECT_STORE.equals(catalog.type)) {
                return catalog;
            }
        }
        return null;
    }

    public boolean isTenantSupplied() {
        return this.tenantSupplied;
    }

    public AccessImpl setTenantSupplied(boolean tenantSupplied) {
        this.tenantSupplied = tenantSupplied;
        return this;
    }

    @SuppressWarnings("ConstantConditions")
    protected EndPoint getCurrentEndPoint() {
        if (!isTenantSupplied()) {
            HttpStatusExceptionUtil.throwException(HttpStatus.SC_NOT_FOUND, CommandExceptionError.NO_TENANT_SUPPLIED);
        }
        if (currentEndPoint != null) {
            return currentEndPoint;
        }
        ServiceCatalog objectStoreCatalog = getObjectStoreCatalog();
        if (objectStoreCatalog == null) {
            HttpStatusExceptionUtil.throwException(HttpStatus.SC_NOT_FOUND, CommandExceptionError.NO_SERVICE_CATALOG_FOUND);
        }
        this.currentEndPoint = objectStoreCatalog.getRegion(preferredRegion);
        if (this.currentEndPoint == null) {
            HttpStatusExceptionUtil.throwException(HttpStatus.SC_NOT_FOUND, CommandExceptionError.NO_END_POINT_FOUND);
        }
        return this.currentEndPoint;
    }

    public String getInternalURL() {
        return getCurrentEndPoint().internalURL;
    }

    public String getPublicURL() {
        return getCurrentEndPoint().publicURL;
    }

}
