package org.javaswift.joss.client.mock;

import org.javaswift.joss.client.core.AbstractStoredObject;

public class StoredObjectMock extends AbstractStoredObject {

    public StoredObjectMock(ContainerMock container, String name) {
        super(container.getFactory().getStoredObjectCommandFactory(), container, name, ALLOW_CACHING);
    }

}
