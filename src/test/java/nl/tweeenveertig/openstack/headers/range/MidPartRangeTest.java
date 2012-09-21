package nl.tweeenveertig.openstack.headers.range;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class MidPartRangeTest {

    @Test
    public void headerValue() {
        MidPartRange range = new MidPartRange(16, 32);
        assertEquals("bytes: 16-32", range.getHeaderValue());
    }
}
