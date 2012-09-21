package nl.tweeenveertig.openstack.headers.range;

import nl.tweeenveertig.openstack.headers.HeaderTest;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class LastPartRangeTest extends HeaderTest {

    @Test
    public void headerValue() {
        LastPartRange range = new LastPartRange(127);
        assertEquals("bytes: -127", range.getHeaderValue());
    }
}
