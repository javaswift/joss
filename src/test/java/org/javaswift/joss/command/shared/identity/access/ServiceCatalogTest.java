package org.javaswift.joss.command.shared.identity.access;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static org.javaswift.joss.command.shared.identity.access.EndPointBuilder.createEndPoint;

public class ServiceCatalogTest {

    @Test
    public void searchRegionThatExists() {
        ServiceCatalog catalog = createCatalogWithEndPoints();
        EndPoint endPoint = catalog.getRegion("AMS-01");
        assertEquals("https://og.cloudvps.com:443/v1/AUTH_bfo000024", endPoint.internalURL);
    }

    @Test
    public void searchRegionThatDoesNotExis() {
        ServiceCatalog catalog = createCatalogWithEndPoints();
        EndPoint endPoint = catalog.getRegion("AMS-02");
        assertEquals("https://og.cloudvps.com:443/v2/AUTH_bfo000024", endPoint.internalURL);
    }

    @Test
    public void searchRegionNoPreference() {
        ServiceCatalog catalog = createCatalogWithEndPoints();
        EndPoint endPoint = catalog.getRegion("AMS-01");
        assertEquals("https://og.cloudvps.com:443/v1/AUTH_bfo000024", endPoint.internalURL);
    }

    protected ServiceCatalog createCatalogWithEndPoints() {
        ServiceCatalog catalog = new ServiceCatalog();
        catalog.endpoints.add(createEndPoint(
                "https://og.cloudvps.com/", "http://bfo000024.og.cloudvps.com:80",
                "https://og.cloudvps.com:443/v1/AUTH_bfo000024", "AMS-01"));
        catalog.endpoints.add(createEndPoint(
                "https://og.cloudvps.com/", "http://bfo000024.og.cloudvps.com:80",
                "https://og.cloudvps.com:443/v2/AUTH_bfo000024", "AMS-02"));
        catalog.endpoints.add(createEndPoint(
                "https://og.cloudvps.com/", "http://bfo000024.og.cloudvps.com:80",
                "https://og.cloudvps.com:443/v3/AUTH_bfo000024", "AMS-03"));
        return catalog;
    }

}
