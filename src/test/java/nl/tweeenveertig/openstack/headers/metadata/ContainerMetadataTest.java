package nl.tweeenveertig.openstack.headers.metadata;

import nl.tweeenveertig.openstack.headers.HeaderTest;
import org.junit.Test;

public class ContainerMetadataTest extends HeaderTest {

    @Test
    public void addHeader() {
        testHeader(new ContainerMetadata("name", "value"));
    }
}
