package nl.tweeenveertig.openstack.headers.object;

import nl.tweeenveertig.openstack.headers.AbstractHeaderTest;
import org.junit.Test;

public class ObjectMetadataTest extends AbstractHeaderTest {

    @Test
    public void addHeader() {
        testHeader(new ObjectMetadata("name", "value"));
    }
}
