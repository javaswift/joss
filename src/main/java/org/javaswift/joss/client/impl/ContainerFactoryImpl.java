package org.javaswift.joss.client.impl;

import org.javaswift.joss.client.core.AbstractAccount;
import org.javaswift.joss.client.core.ContainerFactory;
import org.javaswift.joss.model.Container;

public class ContainerFactoryImpl implements ContainerFactory<Container> {

    @Override
    public Container create(AbstractAccount account, String containerName) {
        return new ContainerImpl((AccountImpl)account, containerName, account.isAllowCaching());
    }

}
