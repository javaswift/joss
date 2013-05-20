package org.javaswift.joss.command.shared.identity.access;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.javaswift.joss.exception.CommandExceptionError;
import org.javaswift.joss.exception.NotFoundException;
import org.javaswift.joss.model.Access;
import org.javaswift.joss.util.ClasspathTemplateResource;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;
import static org.javaswift.joss.command.shared.identity.access.ServiceCatalogBuilder.createServiceCatalog;

public class AccessTest {
    
    @Test
    public void testUnmarshalling() throws IOException {
        String jsonString = new ClasspathTemplateResource("/sample-access.json").loadTemplate();
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationConfig.Feature.UNWRAP_ROOT_VALUE, true);
        Access access = mapper.readValue(jsonString, AccessTenant.class);
        assertEquals("a376b74fbdb64a4986cd3234647ff6f8", access.getToken());
        assertEquals("https://og.cloudvps.com:443/v1/AUTH_bfo000024", access.getInternalURL());
        assertEquals("http://bfo000024.og.cloudvps.com:80", access.getPublicURL());
    }

    @Test
    public void searchCatalogTypePreferredRegionWithUrl() {
        AccessTenant access = setUpAccess();
        access.setPreferredRegion("AMS-03");
        assertEquals("http://www.somewhere.com:80", access.getInternalURL());
    }

    @Test
    public void searchCatalogTypePreferredRegionNullUrl() {
        Access access = setUpAccess();
        access.setPreferredRegion("AMS-01");
        assertEquals(null, access.getInternalURL());
    }

    @Test
    public void searchCatalogTypeNoTenantSupplied() {
        Access access = new AccessNoTenant();
        try {
            access.getInternalURL();
            fail("Should have thrown an exception");
        } catch (NotFoundException err) {
            assertEquals(CommandExceptionError.NO_TENANT_SUPPLIED, err.getError());
        }
    }

    protected AccessTenant setUpAccess() {
        AccessTenant access = new AccessTenant();
        access.serviceCatalog.add(createServiceCatalog("keystone", "identity", new ArrayList<EndPoint>()));
        access.serviceCatalog.add(createServiceCatalog("somethis", "clustering", new ArrayList<EndPoint>()));
        List<EndPoint> endPoints = new ArrayList<EndPoint>();
        endPoints.add(new EndPointBuilder().setRegion("AMS-01").getEndPoint());
        endPoints.add(new EndPointBuilder().setRegion("AMS-02").getEndPoint());
        endPoints.add(new EndPointBuilder().setInternalURL("http://www.somewhere.com:80").setRegion("AMS-03").getEndPoint());
        access.serviceCatalog.add(createServiceCatalog("swift", "object-store", endPoints));
        access.serviceCatalog.add(createServiceCatalog("somethat", "sharding", new ArrayList<EndPoint>()));
        return access;
    }

    @Test
    public void noEndPoints() {
        AccessTenant access = new AccessTenant();
        access.serviceCatalog.add(createServiceCatalog("swift", "object-store", new ArrayList<EndPoint>()));
        try {
            access.getInternalURL();
            fail("Should have thrown an exception");
        } catch (NotFoundException err) {
            assertEquals(CommandExceptionError.NO_END_POINT_FOUND, err.getError());
        }
    }

    @Test
    public void noCatalogs() {
        Access access = new AccessTenant();
        try {
            access.getInternalURL();
            fail("Should have thrown an exception");
        } catch (NotFoundException err) {
            assertEquals(CommandExceptionError.NO_SERVICE_CATALOG_FOUND, err.getError());
        }
    }

    @Test
    public void coverage() {
        new AccessNoTenant().determineCurrentEndPoint();
    }

}
