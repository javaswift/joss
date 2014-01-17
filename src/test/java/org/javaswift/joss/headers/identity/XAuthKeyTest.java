package org.javaswift.joss.headers.identity;

import org.javaswift.joss.headers.AbstractHeaderTest;
import org.junit.Test;

public class XAuthKeyTest extends AbstractHeaderTest {

    @Test
    public void testXAuthKey() {
        testHeader(new XAuthKey("cafebabe"));
    }

}
