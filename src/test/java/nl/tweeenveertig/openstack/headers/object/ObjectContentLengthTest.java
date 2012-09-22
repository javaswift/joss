package nl.tweeenveertig.openstack.headers.object;

import nl.tweeenveertig.openstack.headers.HeaderTest;
import nl.tweeenveertig.openstack.headers.container.ContainerBytesUsed;
import org.junit.Test;

public class ObjectContentLengthTest extends HeaderTest {

    @Test
    public void addHeader() {
        testHeader(new ObjectContentLength("123654789"));
    }
}
