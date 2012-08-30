package nl.t42.openstack.model;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ServiceCatalog {

    public List<EndPoint> endpoints = new ArrayList<EndPoint>();

//    @JsonProperty(value = "endpoints_links")
//    public List<EndPointLink> endpointsLinks = new ArrayList<EndPointLink>();

    public String type;

    public String name;
}
