package org.javaswift.joss.client.impl;

import org.javaswift.joss.client.core.AbstractStoredObject;

public class StoredObjectImpl extends AbstractStoredObject {

    public StoredObjectImpl(ContainerImpl container, String name, boolean allowCaching) {
        super(container.getFactory().getStoredObjectCommandFactory(), container, name, allowCaching);
    }

}
