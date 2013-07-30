package org.javaswift.joss.command.mock.factory;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class ContainerCommandFactoryMockTest {

    @Test
    public void getTempUrlPrefix() {
        assertEquals("", new ContainerCommandFactoryMock(null).getTempUrlPrefix());
    }

}
