package org.javaswift.joss.headers.object;

import org.javaswift.joss.headers.AbstractHeaderTest;
import org.junit.Test;

public class ObjectContentLengthTest extends AbstractHeaderTest {

    @Test
    public void addHeader() {
        testHeader(new ObjectContentLength("123654789"));
    }
}
