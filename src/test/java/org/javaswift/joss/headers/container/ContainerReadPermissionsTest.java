package org.javaswift.joss.headers.container;

import org.javaswift.joss.headers.AbstractHeaderTest;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class ContainerReadPermissionsTest extends AbstractHeaderTest {

    @Test
    public void addHeader() {
        testHeader(new ContainerReadPermissions("123"));
    }

    @Test
    public void nullValue() {
        assertEquals("", new ContainerReadPermissions(null).getHeaderValue());
    }

}
