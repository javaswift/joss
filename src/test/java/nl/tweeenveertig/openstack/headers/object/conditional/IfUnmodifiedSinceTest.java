package nl.tweeenveertig.openstack.headers.object.conditional;

import nl.tweeenveertig.openstack.headers.HeaderTest;
import org.apache.http.impl.cookie.DateParseException;
import org.apache.http.impl.cookie.DateUtils;
import org.junit.Test;

import java.util.Date;

import static junit.framework.Assert.assertEquals;

public class IfUnmodifiedSinceTest extends HeaderTest {

    public static final String expectedDateString = "Tue, 02 Oct 2012 17:34:17 GMT";

    @Test
    public void insertDate() throws DateParseException {
        String expectedDateString = "Tue, 02 Oct 2012 17:34:17 GMT";
        Date date = DateUtils.parseDate(expectedDateString);
        IfUnmodifiedSince header = new IfUnmodifiedSince(date);
        assertEquals(expectedDateString, header.getHeaderValue());
    }

    @Test
    public void fromStringToDate() throws DateParseException {
        String expectedDateString = "Tue, 02 Oct 2012 17:34:17 GMT";
        IfUnmodifiedSince header = new IfUnmodifiedSince(expectedDateString);
        assertEquals(expectedDateString, header.getHeaderValue());
    }

    @Test
    public void addHeader() throws DateParseException {
        testHeader(new IfUnmodifiedSince(expectedDateString));
    }
}
