package nl.tweeenveertig.openstack.headers.object;

import nl.tweeenveertig.openstack.headers.AbstractHeaderTest;
import org.junit.Test;

public class ObjectContentLengthTest extends AbstractHeaderTest {

    @Test
    public void addHeader() {
        testHeader(new ObjectContentLength("123654789"));
    }
}
