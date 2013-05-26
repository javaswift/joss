package org.javaswift.joss.command.mock.factory;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class StoredObjectCommandFactoryMockTest {

    @Test
    public void getTempUrlPrefix() {
        assertEquals("", new StoredObjectCommandFactoryMock(null).getTempUrlPrefix());
    }
}
