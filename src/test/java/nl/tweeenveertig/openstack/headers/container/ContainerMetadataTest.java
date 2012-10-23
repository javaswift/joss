package nl.tweeenveertig.openstack.headers.container;

import nl.tweeenveertig.openstack.headers.AbstractHeaderTest;
import org.junit.Test;

public class ContainerMetadataTest extends AbstractHeaderTest {

    @Test
    public void addHeader() {
        testHeader(new ContainerMetadata("name", "value"));
    }
}
