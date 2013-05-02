package org.javaswift.joss.client.website;

import org.javaswift.joss.model.StoredObject;
import org.javaswift.joss.model.Website;

public class ObjectStoreFileObjects extends AbstractFileObjects {

    private final Website website;

    public ObjectStoreFileObjects(Website website) {
        this.website = website;
        for (StoredObject object : this.website.list()) {
            add(object.getName(), new ObjectStoreFileObject(object));
        }
    }

    @Override
    public ObjectStoreFileObject create(String path) {
        return new ObjectStoreFileObject(website.getObject(path));
    }

    @Override
    public void cleanup() { /* Swift has no folders, therefore no empty folders */ }

}
