package nl.tweeenveertig.openstack.headers;

import org.junit.Test;

public class TokenTest extends HeaderTest {

    @Test
    public void testAddHeader() {
        testHeader(new Token("cafebabe"));
    }
}
