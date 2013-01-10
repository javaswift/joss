package nl.tweeenveertig.openstack.client.impl;

import nl.tweeenveertig.openstack.model.Container;
import nl.tweeenveertig.openstack.model.StoredObject;

public class ContainerPaginationMap extends AbstractPaginationMap<StoredObject> {

    public ContainerPaginationMap(Container container, Integer pageSize) {
        super(container, pageSize);
    }

}
