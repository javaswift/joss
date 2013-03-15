package org.javaswift.joss.headers.object.range;

import org.javaswift.joss.headers.AbstractHeaderTest;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ExcludeStartRangeTest extends AbstractHeaderTest {

    @Test
    public void headerValue() {
        ExcludeStartRange range = new ExcludeStartRange(32);
        assertEquals("bytes=32-", range.getHeaderValue());
    }
}
