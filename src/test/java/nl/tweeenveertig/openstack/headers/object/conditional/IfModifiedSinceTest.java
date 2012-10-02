package nl.tweeenveertig.openstack.headers.object.conditional;

import nl.tweeenveertig.openstack.headers.HeaderTest;
import org.apache.http.impl.cookie.DateParseException;
import org.apache.http.impl.cookie.DateUtils;
import org.junit.Test;

import java.util.Date;

import static junit.framework.Assert.assertEquals;

public class IfModifiedSinceTest extends HeaderTest {

    public static final String expectedDateString = "Tue, 02 Oct 2012 17:34:17 GMT";

    @Test
    public void insertDate() throws DateParseException {
        Date date = DateUtils.parseDate(expectedDateString);
        IfModifiedSince header = new IfModifiedSince(date);
        assertEquals(expectedDateString, header.getHeaderValue());
    }

    @Test
    public void fromStringToDate() throws DateParseException {
        IfModifiedSince header = new IfModifiedSince(expectedDateString);
        assertEquals(expectedDateString, header.getHeaderValue());
    }

    @Test
    public void addHeader() throws DateParseException {
        testHeader(new IfModifiedSince(expectedDateString));
    }
}
