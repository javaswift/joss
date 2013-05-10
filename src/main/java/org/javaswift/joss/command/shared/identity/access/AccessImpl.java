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

    public void setPreferredRegion(String preferredRegion) {
        this.currentEndPoint = getObjectStoreCatalog().getRegion(preferredRegion);
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

    @SuppressWarnings("ConstantConditions")
    public AccessImpl initCurrentEndPoint() {
        ServiceCatalog objectStoreCatalog = getObjectStoreCatalog();
        if (objectStoreCatalog == null) {
            HttpStatusExceptionUtil.throwException(HttpStatus.SC_NOT_FOUND, CommandExceptionError.NO_SERVICE_CATALOG_FOUND);
        }
        this.currentEndPoint = objectStoreCatalog.getRegion(null);
        if (this.currentEndPoint == null) {
            HttpStatusExceptionUtil.throwException(HttpStatus.SC_NOT_FOUND, CommandExceptionError.NO_END_POINT_FOUND);
        }
        return this;
    }

    public String getInternalURL() {
        return currentEndPoint.internalURL;
    }

    public String getPublicURL() {
        return currentEndPoint.publicURL;
    }

}
