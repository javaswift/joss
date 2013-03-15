package org.javaswift.joss.headers.object;

import org.javaswift.joss.headers.AbstractHeaderTest;
import org.junit.Test;

public class CopyFromTest extends AbstractHeaderTest {

    @Test
    public void addHeader() {
        testHeader(new CopyFrom("/container/object"));
    }
}
