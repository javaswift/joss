package nl.tweeenveertig.openstack.headers.object;

import nl.tweeenveertig.openstack.headers.HeaderTest;
import org.apache.http.impl.cookie.DateParseException;
import org.apache.http.impl.cookie.DateUtils;
import org.junit.Test;

public class DeleteAtTest extends HeaderTest {

    @Test
    public void testAddHeaderByString() throws DateParseException {
        testHeader(new DeleteAt("Sat, 22 Sep 2012 07:24:21 GMT"));
    }

    @Test
    public void testAddHeaderByMilliseconds() throws DateParseException {
        testHeader(new DeleteAt(DateUtils.parseDate("Sat, 22 Sep 2012 07:24:21 GMT").getTime()));
    }

    @Test
    public void testAddHeaderByDate() throws DateParseException {
        testHeader(new DeleteAt(DateUtils.parseDate("Sat, 22 Sep 2012 07:24:21 GMT")));
    }

}
