package org.javaswift.joss.command.shared.identity.access;

import org.apache.http.HttpStatus;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonRootName;
import org.javaswift.joss.exception.CommandExceptionError;
import org.javaswift.joss.exception.HttpStatusExceptionUtil;

import java.util.ArrayList;
import java.util.List;

@JsonRootName(value="access")
@JsonIgnoreProperties(ignoreUnknown = true)
public class AccessTenant extends AbstractAccess {

    public List<ServiceCatalog> serviceCatalog = new ArrayList<ServiceCatalog>();

    @JsonIgnore
    private EndPoint currentEndPoint;

    private ServiceCatalog getObjectStoreCatalog() {
        for (ServiceCatalog catalog : serviceCatalog) {
            if (SERVICE_CATALOG_OBJECT_STORE.equals(catalog.type)) {
                return catalog;
            }
        }
        return null;
    }

    @Override
    public boolean isTenantSupplied() {
        return true;
    }

    @SuppressWarnings("ConstantConditions")
    protected EndPoint determineCurrentEndPoint() {
        if (currentEndPoint != null) {
            return currentEndPoint;
        }
        ServiceCatalog objectStoreCatalog = getObjectStoreCatalog();
        if (objectStoreCatalog == null) {
            HttpStatusExceptionUtil.throwException(HttpStatus.SC_NOT_FOUND, CommandExceptionError.NO_SERVICE_CATALOG_FOUND);
        }
        this.currentEndPoint = objectStoreCatalog.getRegion(getPreferredRegion());
        if (this.currentEndPoint == null) {
            HttpStatusExceptionUtil.throwException(HttpStatus.SC_NOT_FOUND, CommandExceptionError.NO_END_POINT_FOUND);
        }
        return this.currentEndPoint;
    }

}
