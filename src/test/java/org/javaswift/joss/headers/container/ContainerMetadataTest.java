package org.javaswift.joss.headers.container;

import org.javaswift.joss.headers.AbstractHeaderTest;
import org.junit.Test;

public class ContainerMetadataTest extends AbstractHeaderTest {

    @Test
    public void addHeader() {
        testHeader(new ContainerMetadata("name", "value"));
    }
}
