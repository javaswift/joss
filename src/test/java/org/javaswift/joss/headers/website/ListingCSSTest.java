package org.javaswift.joss.headers.website;

import org.javaswift.joss.headers.AbstractHeaderTest;
import org.junit.Test;

public class ListingCSSTest extends AbstractHeaderTest {

    @Test
    public void create() {
        testHeader(new ListingCSS("styles/listing.css"));
    }
}
