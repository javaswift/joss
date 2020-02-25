package org.javaswift.joss.headers.object.range;

import static junit.framework.Assert.assertEquals;

import org.javaswift.joss.headers.AbstractHeaderTest;
import org.junit.Test;

public class LastPartRangeTest extends AbstractHeaderTest {

    @Test
    public void headerValue() {
        LastPartRange range = new LastPartRange(127);
        assertEquals("bytes=-127", range.getHeaderValue());
    }
}
