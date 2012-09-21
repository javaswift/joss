package nl.tweeenveertig.openstack.command.object.range;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ExcludeStartRangeTest {

    @Test
    public void headerValue() {
        ExcludeStartRange range = new ExcludeStartRange(32);
        assertEquals("bytes: 32-", range.generateHeaderValue());
    }
}
