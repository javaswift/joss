package org.javaswift.joss.client.impl;

import org.javaswift.joss.client.core.AbstractStoredObject;
import org.javaswift.joss.model.Container;

public class StoredObjectImpl extends AbstractStoredObject {

    public StoredObjectImpl(Container container, String name, boolean allowCaching) {
        super(container, name, allowCaching);
    }

}
