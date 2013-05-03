package org.javaswift.joss.client.mock;

import org.javaswift.joss.client.core.AbstractAccount;
import org.javaswift.joss.client.core.ContainerFactory;
import org.javaswift.joss.model.Website;

public class WebsiteFactoryMock implements ContainerFactory<Website> {

    @Override
    public Website create(AbstractAccount account, String containerName) {
        return new WebsiteMock((AccountMock)account, containerName);
    }

}
