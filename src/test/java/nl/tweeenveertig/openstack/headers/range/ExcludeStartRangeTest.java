package nl.tweeenveertig.openstack.headers.range;

import nl.tweeenveertig.openstack.headers.HeaderTest;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ExcludeStartRangeTest extends HeaderTest {

    @Test
    public void headerValue() {
        ExcludeStartRange range = new ExcludeStartRange(32);
        assertEquals("bytes=32-", range.getHeaderValue());
    }
}
