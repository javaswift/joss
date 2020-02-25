package org.javaswift.joss.headers.container;

import static junit.framework.Assert.assertEquals;

import org.javaswift.joss.headers.AbstractHeaderTest;
import org.junit.Test;

public class ContainerWritePermissionsTest extends AbstractHeaderTest {

    @Test
    public void addHeader() {
        testHeader(new ContainerWritePermissions("123"));
    }

    @Test
    public void nullValue() {
        assertEquals("", new ContainerWritePermissions(null).getHeaderValue());
    }

}
