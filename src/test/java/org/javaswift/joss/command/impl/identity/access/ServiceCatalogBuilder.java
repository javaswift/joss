package org.javaswift.joss.command.impl.identity.access;

import java.util.List;

public class ServiceCatalogBuilder {

    private ServiceCatalog serviceCatalog = new ServiceCatalog();

    public ServiceCatalogBuilder setName(String name) {
        this.serviceCatalog.name = name;
        return this;
    }

    public ServiceCatalogBuilder setType(String type) {
        this.serviceCatalog.type = type;
        return this;
    }

    public ServiceCatalog getServiceCatalog() {
        return this.serviceCatalog;
    }

    public ServiceCatalogBuilder setEndPoints(List<EndPoint> endpoints) {
        this.serviceCatalog.endpoints = endpoints;
        return this;
    }

    public static ServiceCatalog createServiceCatalog(String name, String type, List<EndPoint> endpoints) {
        return new ServiceCatalogBuilder()
                .setName(name)
                .setType(type)
                .setEndPoints(endpoints)
                .getServiceCatalog();
    }
}
