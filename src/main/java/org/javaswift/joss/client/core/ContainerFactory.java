package org.javaswift.joss.client.core;

import org.javaswift.joss.model.Container;

public interface ContainerFactory<N extends Container> {

    public N create(AbstractAccount account, String containerName);

}
