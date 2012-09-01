package nl.t42.openstack.command.identity.access;

import org.codehaus.jackson.map.annotate.JsonRootName;

import java.util.ArrayList;
import java.util.List;

@JsonRootName(value="access")
public class Access {

    public Token token;

    public List<ServiceCatalog> serviceCatalog = new ArrayList<ServiceCatalog>();

    public User user;

    public String getToken() {
        return token == null ? null : token.id;
    }

    public String getInternalURL() {
        return serviceCatalog.size() > 0 ? (serviceCatalog.get(0).endpoints.size() > 0 ? serviceCatalog.get(0).endpoints.get(0).internalURL : null ) : null;
    }
}
