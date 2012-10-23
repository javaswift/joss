package nl.tweeenveertig.openstack.headers.object;

import nl.tweeenveertig.openstack.headers.AbstractHeaderTest;
import org.junit.Test;

public class DeleteAfterTest extends AbstractHeaderTest {

    @Test
    public void testAddHeader() {
        testHeader(new DeleteAfter(30));
    }

}
