package org.javaswift.joss.headers.website;

import org.javaswift.joss.headers.AbstractHeaderTest;
import org.junit.Test;

public class ListingTest extends AbstractHeaderTest {
    
    @Test
    public void create() {
        testHeader(new Listing(true));
    }
}
