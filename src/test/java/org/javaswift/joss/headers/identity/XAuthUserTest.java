package org.javaswift.joss.headers.identity;

import org.javaswift.joss.headers.AbstractHeaderTest;
import org.junit.Test;

public class XAuthUserTest extends AbstractHeaderTest {

    @Test
    public void testXAuthUser() {
        testHeader(new XAuthUser("secret-user"));
    }

}
