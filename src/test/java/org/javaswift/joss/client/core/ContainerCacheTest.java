package org.javaswift.joss.client.core;

import org.javaswift.joss.client.mock.ClientMock;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class ContainerCacheTest {

    @Test
    public void getContainerWithCacheEnabled() {
        AbstractAccount account = new ClientMock()
                .authenticate(null, null, null, null)
                .setAllowContainerCaching(true);
        ContainerCache cache = new ContainerCache();
        cache.getContainer(account, "alpha");
        cache.getContainer(account, "alpha");
        assertEquals(1, cache.size());
    }

    @Test
    public void reset() {
        AbstractAccount account = new ClientMock()
                .authenticate(null, null, null, null)
                .setAllowContainerCaching(true);
        ContainerCache cache = new ContainerCache();
        cache.getContainer(account, "alpha");
        account.getContainer("alpha");
        assertEquals(1, cache.size());
        cache.reset();
        assertEquals(0, cache.size());
    }

}
