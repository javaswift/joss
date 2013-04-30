package org.javaswift.joss.client.mock;

import org.javaswift.joss.client.core.AbstractWebsite;
import org.javaswift.joss.model.StoredObject;

public class WebsiteMock extends AbstractWebsite {

    public WebsiteMock(AccountMock account, String name) {
        super(account, name, ALLOW_CACHING);
    }

    @Override
    public StoredObject getObject(String objectName) {
        return new StoredObjectMock(this, objectName);
    }

}
