package nl.tweeenveertig.openstack.headers.container;

import nl.tweeenveertig.openstack.headers.AbstractHeaderTest;
import org.junit.Test;

public class ContainerBytesUsedTest extends AbstractHeaderTest {

    @Test
    public void addHeader() {
        testHeader(new ContainerBytesUsed("123654789"));
    }
}
