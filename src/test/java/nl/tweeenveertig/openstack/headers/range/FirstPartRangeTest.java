package nl.tweeenveertig.openstack.headers.range;

import nl.tweeenveertig.openstack.headers.HeaderTest;
import nl.tweeenveertig.openstack.headers.container.ContainerMetadata;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class FirstPartRangeTest extends HeaderTest {

    @Test
    public void headerValue() {
        FirstPartRange range = new FirstPartRange(8);
        assertEquals("bytes=0-8", range.getHeaderValue());
    }

    @Test
    public void addHeader() {
        testHeader(new FirstPartRange(4));
    }
}
