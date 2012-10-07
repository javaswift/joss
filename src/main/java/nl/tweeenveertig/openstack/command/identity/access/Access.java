package nl.tweeenveertig.openstack.command.identity.access;

import nl.tweeenveertig.openstack.exception.CommandException;
import nl.tweeenveertig.openstack.command.core.CommandExceptionError;
import org.apache.http.HttpStatus;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonRootName;

import java.util.ArrayList;
import java.util.List;

@JsonRootName(value="access")
public class Access {

    public static final String SERVICE_CATALOG_OBJECT_STORE = "object-store";
    public Token token;

    public List<ServiceCatalog> serviceCatalog = new ArrayList<ServiceCatalog>();

    public User user;

    @JsonIgnore
    private EndPoint currentEndPoint;

    @JsonIgnore
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

    public void initCurrentEndPoint() {
        ServiceCatalog objectStoreCatalog = getObjectStoreCatalog();
        if (objectStoreCatalog == null) {
            throw new CommandException(HttpStatus.SC_NOT_FOUND, CommandExceptionError.NO_SERVICE_CATALOG_FOUND);
        }
        this.currentEndPoint = objectStoreCatalog.getRegion(this.preferredRegion);
        if (this.currentEndPoint == null) {
            throw new CommandException(HttpStatus.SC_NOT_FOUND, CommandExceptionError.NO_END_POINT_FOUND);
        }
    }

    public String getInternalURL() {
        if (this.currentEndPoint == null) {
            initCurrentEndPoint();
        }
        return currentEndPoint.internalURL;
    }

    public String getPublicURL() {
        if (this.currentEndPoint == null) {
            initCurrentEndPoint();
        }
        return currentEndPoint.publicURL;
    }
}
