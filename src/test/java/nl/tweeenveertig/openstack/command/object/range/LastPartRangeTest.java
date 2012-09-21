package nl.tweeenveertig.openstack.command.object.range;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class LastPartRangeTest {

    @Test
    public void headerValue() {
        LastPartRange range = new LastPartRange(127);
        assertEquals("bytes: -127", range.generateHeaderValue());
    }
}
