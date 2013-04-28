package org.javaswift.joss.client.core;

import org.javaswift.joss.model.Container;

import java.util.Map;
import java.util.TreeMap;

public class ContainerCache {

    public Map<String, Container> cache = new TreeMap<String, Container>();

    public Container getContainer(AbstractAccount account, String containerName) {
        Container container = cache.get(containerName);
        if (container == null) {
            container = account.createContainer(containerName);
            cache.put(containerName, container);
        }
        return container;
    }

    public void reset() {
        this.cache.clear();
    }

    public int size() {
        return this.cache.size();
    }

}
