package org.javaswift.joss.client.impl;

import org.javaswift.joss.client.core.AbstractWebsite;
import org.javaswift.joss.model.StoredObject;

public class WebsiteImpl extends AbstractWebsite {

    public WebsiteImpl(AccountImpl account, String name, boolean allowCaching) {
        super(account, name, allowCaching);
    }

    @Override
    public StoredObject getObject(String objectName) {
        return new StoredObjectImpl(this, objectName, isAllowCaching());
    }
}
