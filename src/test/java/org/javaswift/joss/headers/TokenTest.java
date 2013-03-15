package org.javaswift.joss.headers;

import org.junit.Test;

public class TokenTest extends AbstractHeaderTest {

    @Test
    public void testAddHeader() {
        testHeader(new Token("cafebabe"));
    }
}
