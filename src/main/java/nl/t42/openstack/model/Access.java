package nl.t42.openstack.model;

import org.codehaus.jackson.map.annotate.JsonRootName;

import java.util.ArrayList;
import java.util.List;

@JsonRootName(value="access")
public class Access {

    public Token token;

    public List<ServiceCatalog> serviceCatalog = new ArrayList<ServiceCatalog>();

    public User user;

}
