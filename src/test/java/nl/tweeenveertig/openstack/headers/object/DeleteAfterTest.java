package nl.tweeenveertig.openstack.headers.object;

import nl.tweeenveertig.openstack.headers.HeaderTest;
import org.junit.Test;

public class DeleteAfterTest extends HeaderTest {

    @Test
    public void testAddHeader() {
        testHeader(new DeleteAfter(30));
    }

}
