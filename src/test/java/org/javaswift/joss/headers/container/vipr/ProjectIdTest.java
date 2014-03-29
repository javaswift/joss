package org.javaswift.joss.headers.container.vipr;

import org.javaswift.joss.headers.AbstractHeaderTest;
import org.junit.Test;

public class ProjectIdTest extends AbstractHeaderTest {

    @Test
    public void addHeader() {
        testHeader(new ProjectId("123654789"));
    }
}
