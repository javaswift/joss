package nl.tweeenveertig.openstack.headers.object;

import nl.tweeenveertig.openstack.headers.HeaderTest;
import org.junit.Test;

public class ObjectContentTypeTest extends HeaderTest {

    @Test
    public void addHeader() {
        testHeader(new ObjectContentType("text/plain"));
    }
}
