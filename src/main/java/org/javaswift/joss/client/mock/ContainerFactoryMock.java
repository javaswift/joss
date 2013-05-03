package org.javaswift.joss.client.mock;

import org.javaswift.joss.client.core.AbstractAccount;
import org.javaswift.joss.client.core.ContainerFactory;
import org.javaswift.joss.model.Container;

public class ContainerFactoryMock implements ContainerFactory<Container> {

    @Override
    public Container create(AbstractAccount account, String containerName) {
        return new ContainerMock((AccountMock)account, containerName);
    }

}
