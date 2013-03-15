package org.javaswift.joss.headers.object;

import org.javaswift.joss.headers.AbstractHeaderTest;
import org.junit.Test;

public class DeleteAfterTest extends AbstractHeaderTest {

    @Test
    public void testAddHeader() {
        testHeader(new DeleteAfter(30));
    }

}
