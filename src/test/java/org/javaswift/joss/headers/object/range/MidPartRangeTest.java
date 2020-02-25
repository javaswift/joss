package org.javaswift.joss.headers.object.range;

import static junit.framework.Assert.assertEquals;

import org.javaswift.joss.headers.AbstractHeaderTest;
import org.junit.Test;

public class MidPartRangeTest extends AbstractHeaderTest {

    @Test
    public void headerValue() {
        MidPartRange range = new MidPartRange(16, 32);
        assertEquals("bytes=16-32", range.getHeaderValue());
    }
}
