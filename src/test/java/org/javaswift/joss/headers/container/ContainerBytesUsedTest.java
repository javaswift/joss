package org.javaswift.joss.headers.container;

import org.javaswift.joss.headers.AbstractHeaderTest;
import org.junit.Test;

public class ContainerBytesUsedTest extends AbstractHeaderTest {

    @Test
    public void addHeader() {
        testHeader(new ContainerBytesUsed("123654789"));
    }
}
