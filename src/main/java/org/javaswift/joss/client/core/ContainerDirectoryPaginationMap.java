package org.javaswift.joss.client.core;

import org.javaswift.joss.model.Container;
import org.javaswift.joss.model.DirectoryOrObject;
import java.util.Collection;

public class ContainerDirectoryPaginationMap extends AbstractPaginationMap<DirectoryOrObject> {

    private Character delimiter;

    private Container container;

    public ContainerDirectoryPaginationMap(Container container, Character delimiter, String prefix, Integer pageSize) {
        super(prefix, pageSize, container.getMaxPageSize());
        this.container = container;
        this.delimiter = delimiter;
    }

    @Override
    public Collection<DirectoryOrObject> list(String prefix, String marker, int blockSize) {
        return container.listDirectory(prefix, delimiter, marker, blockSize);
    }

    @Override
    public int getCount() {
        return container.getCount();
    }

}
