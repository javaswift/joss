package org.javaswift.joss.client.mock;

import org.javaswift.joss.client.core.AbstractStoredObject;
import org.javaswift.joss.model.Container;

public class StoredObjectMock extends AbstractStoredObject {

    public StoredObjectMock(Container container, String name) {
        super(container, name, ALLOW_CACHING);
    }

}
