package org.javaswift.joss.command.shared.identity.access;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonRootName;
import org.apache.http.HttpStatus;
import org.javaswift.joss.client.factory.TempUrlHashPrefixSource;
import org.javaswift.joss.exception.CommandExceptionError;
import org.javaswift.joss.exception.HttpStatusExceptionUtil;

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

    @Override
    public String getTempUrlPrefix(TempUrlHashPrefixSource tempUrlHashPrefixSource) {
        final String tempUrlPrefix;
        if (tempUrlHashPrefixSource == null) {
            tempUrlPrefix = "";
        } else if (tempUrlHashPrefixSource == TempUrlHashPrefixSource.PUBLIC_URL_PATH) {
            tempUrlPrefix = TempUrlHashPrefixSource.getPath(getCurrentEndPoint().publicURL);
        } else if (tempUrlHashPrefixSource == TempUrlHashPrefixSource.INTERNAL_URL_PATH) {
            tempUrlPrefix = TempUrlHashPrefixSource.getPath(getCurrentEndPoint().internalURL);
        } else {
            tempUrlPrefix = TempUrlHashPrefixSource.getPath(getCurrentEndPoint().adminURL);
        }
        return tempUrlPrefix.endsWith("/") ? tempUrlPrefix.substring(0, tempUrlPrefix.length()-1) : tempUrlPrefix;
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
