package org.javaswift.joss.headers.object.range;

import static junit.framework.Assert.assertEquals;

import org.javaswift.joss.headers.AbstractHeaderTest;
import org.junit.Test;

public class FirstPartRangeTest extends AbstractHeaderTest {

    @Test
    public void headerValue() {
        FirstPartRange range = new FirstPartRange(8);
        assertEquals("bytes=0-8", range.getHeaderValue());
    }

    @Test
    public void addHeader() {
        testHeader(new FirstPartRange(4));
    }
}
