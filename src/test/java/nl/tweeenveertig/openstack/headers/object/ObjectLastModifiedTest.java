package nl.tweeenveertig.openstack.headers.object;

import nl.tweeenveertig.openstack.headers.HeaderTest;
import org.junit.Test;

public class ObjectLastModifiedTest extends HeaderTest {

    @Test
    public void addHeader() {
        testHeader(new ObjectLastModified("Sat, 22 Sep 2012 07:24:21 GMT"));
    }
}
