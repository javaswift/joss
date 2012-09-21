package nl.tweeenveertig.openstack.headers.metadata;

import nl.tweeenveertig.openstack.headers.HeaderTest;
import org.junit.Test;

public class ObjectMetadataTest extends HeaderTest {

    @Test
    public void addHeader() {
        testHeader(new ObjectMetadata("name", "value"));
    }
}
