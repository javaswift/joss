package org.javaswift.joss.client.core;

import org.javaswift.joss.client.factory.AccountConfig;
import org.javaswift.joss.client.mock.ClientMock;
import org.javaswift.joss.client.mock.ContainerFactoryMock;
import org.javaswift.joss.model.Container;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotSame;

public class ContainerCacheTest {

    @Test
    public void getContainerWithCacheEnabled() {
        AbstractAccount account = new ClientMock(new AccountConfig())
                .authenticate(null, null, null, null, null);
        ContainerCache cache = new ContainerCache<Container>(account, new ContainerFactoryMock());
        cache.getContainer("alpha");
        cache.getContainer("alpha");
        assertEquals(1, cache.size());
    }

    @Test
    public void getContainerWithCacheDisabled() {
        AbstractAccount account = new ClientMock(new AccountConfig())
                .authenticate(null, null, null, null, null);
        ContainerCache cache = new ContainerCache<Container>(account, new ContainerFactoryMock());
        cache.setCacheEnabled(false);
        Container container1 = cache.getContainer("alpha");
        Container container2 = cache.getContainer("alpha");
        assertNotSame(container1, container2);
    }

    @Test
    public void reset() {
        AbstractAccount account = new ClientMock(new AccountConfig())
                .authenticate(null, null, null, null, null);
        ContainerCache cache = new ContainerCache<Container>(account, new ContainerFactoryMock());
        cache.getContainer("alpha");
        account.getContainer("alpha");
        assertEquals(1, cache.size());
        cache.reset();
        assertEquals(0, cache.size());
    }

}
