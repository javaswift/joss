package org.javaswift.joss.headers.website;

import org.javaswift.joss.headers.AbstractHeaderTest;
import org.junit.Test;

public class ErrorPageTest extends AbstractHeaderTest {
    
    @Test
    public void create() {
        testHeader(new ErrorPage("error.html"));
    }
}
