package nl.tweeenveertig.openstack.headers.object;

import nl.tweeenveertig.openstack.headers.HeaderTest;
import org.apache.http.impl.cookie.DateParseException;
import org.junit.Test;

public class ObjectLastModifiedTest extends HeaderTest {

    @Test
    public void addHeader() throws DateParseException {
        testHeader(new ObjectLastModified("Sat, 22 Sep 2012 07:24:21 GMT"));
    }
}
