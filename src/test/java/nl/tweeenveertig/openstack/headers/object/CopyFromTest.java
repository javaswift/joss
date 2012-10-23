package nl.tweeenveertig.openstack.headers.object;

import nl.tweeenveertig.openstack.headers.AbstractHeaderTest;
import org.junit.Test;

public class CopyFromTest extends AbstractHeaderTest {

    @Test
    public void addHeader() {
        testHeader(new CopyFrom("/container/object"));
    }
}
