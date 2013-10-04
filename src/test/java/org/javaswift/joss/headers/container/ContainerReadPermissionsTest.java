package org.javaswift.joss.headers.container;

import org.javaswift.joss.headers.AbstractHeaderTest;
import org.junit.Test;

public class ContainerReadPermissionsTest extends AbstractHeaderTest {

    @Test
    public void addHeader() {
        testHeader(new ContainerReadPermissions("123"));
    }

}
