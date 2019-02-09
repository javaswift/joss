package org.javaswift.joss.client.core;

import org.javaswift.joss.client.factory.AccountConfig;
import org.javaswift.joss.client.mock.ClientMock;
import org.javaswift.joss.client.mock.ContainerFactoryMock;
import org.javaswift.joss.model.Container;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static junit.framework.Assert.*;

public class ContainerCacheTest {

    @Test
    public void getContainerWithCacheEnabled() {
        AbstractAccount account = new ClientMock(new AccountConfig()).authenticate();
        ContainerCache cache = new ContainerCache<Container>(account, new ContainerFactoryMock());
        cache.getContainer("alpha");
        cache.getContainer("alpha");
        assertEquals(1, cache.size());
    }

    @Test
    public void getContainerConcurrentlyWithCacheEnabled() throws InterruptedException {
        AbstractAccount account = new ClientMock(new AccountConfig()).authenticate();
        final ContainerCache<Container> cache = new ContainerCache<Container>(account, new ContainerFactoryMock());
        int numThreads = 20;
        final int numContainers = 100000;

        final String[] containers = new String[numContainers];

        for (int i = 0; i < numContainers; i++) {
            containers[i] = "Container-" + i;
        }


        ExecutorService pool = Executors.newFixedThreadPool(numThreads);

        final CountDownLatch latch = new CountDownLatch(numThreads);

        final AtomicReference<Exception> exceptionAtomicReference = new AtomicReference<Exception>(null);


        for (int i = 0; i < numThreads; i++) {
            pool.execute(new Runnable(){

                @Override
                public void run() {
                    try {
                        for (int i = 0; i < numContainers; i++) {
                            int containerI = (int) (Math.random() * numContainers);
                            String randomContainer = containers[containerI];
                            cache.getContainer(randomContainer);
                            cache.getContainer(randomContainer);
                        }
                    } catch (RuntimeException e) {
                        exceptionAtomicReference.set(e);
                    } finally {
                        latch.countDown();
                    }
                }
            });
        }

        boolean terminatedNormally = latch.await(10, TimeUnit.SECONDS);

        pool.shutdown();
        pool.awaitTermination(1, TimeUnit.SECONDS);

        Exception exception = exceptionAtomicReference.get();
        if (exception != null) {
            exception.printStackTrace();
            fail("Exception thrown " + exception.getMessage());
        }

        assertTrue("Timed out - threads may be deadlocked in ContainerCache.getContainer",terminatedNormally);

    }

    @Test
    public void getContainerWithCacheDisabled() {
        AbstractAccount account = new ClientMock(new AccountConfig()).authenticate();
        ContainerCache cache = new ContainerCache<Container>(account, new ContainerFactoryMock());
        cache.setCacheEnabled(false);
        Container container1 = cache.getContainer("alpha");
        Container container2 = cache.getContainer("alpha");
        assertNotSame(container1, container2);
    }

    @Test
    public void reset() {
        AbstractAccount account = new ClientMock(new AccountConfig()).authenticate();
        ContainerCache cache = new ContainerCache<Container>(account, new ContainerFactoryMock());
        cache.getContainer("alpha");
        account.getContainer("alpha");
        assertEquals(1, cache.size());
        cache.reset();
        assertEquals(0, cache.size());
    }

}
