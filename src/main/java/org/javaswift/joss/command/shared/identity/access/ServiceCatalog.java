package org.javaswift.joss.command.shared.identity.access;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ServiceCatalog {

    public List<EndPoint> endpoints = new ArrayList<EndPoint>();

    public static final Logger LOG = LoggerFactory.getLogger(ServiceCatalog.class);

//    @JsonProperty(value = "endpoints_links")
//    public List<EndPointLink> endpointsLinks = new ArrayList<EndPointLink>();

    public String type;

    public String name;

    public EndPoint getRegion(String regionName) {
        EndPoint firstEndPoint = null;
        LOG.info("getRegion: regionName:" + regionName);
        for (EndPoint endPoint : endpoints) {
            LOG.info("getRegion: iter endpoint ", endPoint.region);
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
