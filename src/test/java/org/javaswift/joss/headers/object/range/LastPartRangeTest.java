package org.javaswift.joss.headers.object.range;

import org.javaswift.joss.headers.AbstractHeaderTest;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class LastPartRangeTest extends AbstractHeaderTest {

    @Test
    public void headerValue() {
        LastPartRange range = new LastPartRange(127);
        assertEquals("bytes=-127", range.getHeaderValue());
    }
}
