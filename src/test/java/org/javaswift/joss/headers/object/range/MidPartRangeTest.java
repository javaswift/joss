package org.javaswift.joss.headers.object.range;

import org.javaswift.joss.headers.AbstractHeaderTest;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class MidPartRangeTest extends AbstractHeaderTest {

    @Test
    public void headerValue() {
        MidPartRange range = new MidPartRange(16, 32);
        assertEquals("bytes=16-32", range.getHeaderValue());
    }
}
