package nl.tweeenveertig.openstack.headers.container;

import nl.tweeenveertig.openstack.headers.HeaderTest;
import nl.tweeenveertig.openstack.headers.container.ContainerMetadata;
import org.junit.Test;

public class ContainerMetadataTest extends HeaderTest {

    @Test
    public void addHeader() {
        testHeader(new ContainerMetadata("name", "value"));
    }
}
