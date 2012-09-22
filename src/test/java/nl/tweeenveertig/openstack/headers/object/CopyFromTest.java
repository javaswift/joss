package nl.tweeenveertig.openstack.headers.object;

import nl.tweeenveertig.openstack.headers.HeaderTest;
import nl.tweeenveertig.openstack.headers.object.CopyFrom;
import org.junit.Test;

public class CopyFromTest extends HeaderTest {

    @Test
    public void addHeader() {
        testHeader(new CopyFrom("/container/object"));
    }
}
