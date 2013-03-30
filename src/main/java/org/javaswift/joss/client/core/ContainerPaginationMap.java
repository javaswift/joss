package org.javaswift.joss.client.core;

import org.javaswift.joss.model.Container;
import org.javaswift.joss.model.StoredObject;

public class ContainerPaginationMap extends AbstractPaginationMap<StoredObject> {

    public ContainerPaginationMap(Container container, String prefix, Integer pageSize) {
        super(container, prefix, pageSize);
    }

}
