package nl.tweeenveertig.openstack.headers;

import org.junit.Test;

public class CopyFromTest extends HeaderTest {

    @Test
    public void addHeader() {
        testHeader(new CopyFrom("/container/object"));
    }
}
