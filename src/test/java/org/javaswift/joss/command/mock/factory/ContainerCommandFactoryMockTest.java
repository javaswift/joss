package org.javaswift.joss.command.mock.factory;

import static junit.framework.Assert.assertEquals;

import org.junit.Test;

public class ContainerCommandFactoryMockTest {

    @Test
    public void getTempUrlPrefix() {
        assertEquals("", new ContainerCommandFactoryMock(null).getTempUrlPrefix());
    }

}
