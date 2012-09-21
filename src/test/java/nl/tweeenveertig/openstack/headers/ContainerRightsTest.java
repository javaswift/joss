package nl.tweeenveertig.openstack.headers;

import org.junit.Test;

public class ContainerRightsTest extends HeaderTest {

    @Test
    public void addHeader() {
        testHeader(new ContainerRights(true));
    }
}
