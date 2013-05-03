package org.javaswift.joss.client.impl;

import org.javaswift.joss.client.core.AbstractAccount;
import org.javaswift.joss.client.core.ContainerFactory;
import org.javaswift.joss.model.Website;

public class WebsiteFactoryImpl implements ContainerFactory<Website> {

    @Override
    public Website create(AbstractAccount account, String containerName) {
        return new WebsiteImpl((AccountImpl)account, containerName, account.isAllowCaching());
    }

}
