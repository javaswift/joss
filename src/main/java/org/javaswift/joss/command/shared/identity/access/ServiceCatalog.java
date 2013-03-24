package org.javaswift.joss.command.shared.identity.access;

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

    public EndPoint getRegion(String regionName) {
        EndPoint firstEndPoint = null;
        for (EndPoint endPoint : endpoints) {
            if (regionName == null) { // If no region is passed, return the first region
                return endPoint;
            }
            if (firstEndPoint == null) { // If the requested region was not found, return the first region -- show must go on
                firstEndPoint = endPoint;
            }
            if (regionName.equals(endPoint.region)) {
                return endPoint;
            }
        }
        return firstEndPoint;
    }
}
