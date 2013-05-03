package org.javaswift.joss.client.core;

import org.javaswift.joss.model.Container;

import java.util.Map;
import java.util.TreeMap;

public class ContainerCache<N extends Container> {

    private Map<String, N> cache = new TreeMap<String, N>();

    private final AbstractAccount account;

    private final ContainerFactory<N> containerFactory;

    private boolean cacheEnabled;

    public ContainerCache(AbstractAccount account, ContainerFactory<N> containerFactory) {
        this.account = account;
        this.containerFactory = containerFactory;
        this.cacheEnabled = true;
    }

    public N getContainer(String containerName) {
        if (!cacheEnabled) {
            return containerFactory.create(account, containerName);
        }
        N container = cache.get(containerName);
        if (container == null) {
            container = containerFactory.create(account, containerName);
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

    public void setCacheEnabled(boolean cacheEnabled) {
        this.cacheEnabled = cacheEnabled;
    }

}
