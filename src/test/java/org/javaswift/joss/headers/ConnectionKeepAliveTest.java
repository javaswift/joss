package org.javaswift.joss.headers;

import org.junit.Test;

public class ConnectionKeepAliveTest extends AbstractHeaderTest {

    @Test
    public void testAddHeader() {
        testHeader(new ConnectionKeepAlive());
    }
}
