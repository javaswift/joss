package nl.tweeenveertig.openstack.headers.container;

import nl.tweeenveertig.openstack.headers.HeaderTest;
import nl.tweeenveertig.openstack.headers.container.ContainerRights;
import org.junit.Test;

public class ContainerRightsTest extends HeaderTest {

    @Test
    public void addHeader() {
        testHeader(new ContainerRights(true));
    }
}
