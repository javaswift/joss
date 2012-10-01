package nl.tweeenveertig.openstack.headers.object.conditional;

import nl.tweeenveertig.openstack.headers.HeaderTest;
import org.junit.Test;

public class IfMatchTest extends HeaderTest{

    @Test
    public void addHeader() {
        testHeader(new IfMatch("cafebabe"));
    }

}
