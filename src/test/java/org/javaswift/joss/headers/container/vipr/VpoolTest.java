package org.javaswift.joss.headers.container.vipr;

import org.javaswift.joss.headers.AbstractHeaderTest;
import org.junit.Test;

public class VpoolTest extends AbstractHeaderTest {

    @Test
    public void addHeader() {
        testHeader(new Vpool("123654789"));
    }

}
