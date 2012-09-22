package nl.tweeenveertig.openstack.headers.object;

import nl.tweeenveertig.openstack.headers.HeaderTest;
import nl.tweeenveertig.openstack.headers.object.ObjectMetadata;
import org.junit.Test;

public class ObjectMetadataTest extends HeaderTest {

    @Test
    public void addHeader() {
        testHeader(new ObjectMetadata("name", "value"));
    }
}
