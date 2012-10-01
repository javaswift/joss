package nl.tweeenveertig.openstack.headers.object.range;

import nl.tweeenveertig.openstack.headers.HeaderTest;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class MidPartRangeTest extends HeaderTest {

    @Test
    public void headerValue() {
        MidPartRange range = new MidPartRange(16, 32);
        assertEquals("bytes=16-32", range.getHeaderValue());
    }
}
