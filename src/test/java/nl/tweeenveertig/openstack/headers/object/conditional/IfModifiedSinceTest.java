package nl.tweeenveertig.openstack.headers.object.conditional;

import nl.tweeenveertig.openstack.command.core.CommandException;
import nl.tweeenveertig.openstack.command.core.CommandExceptionError;
import nl.tweeenveertig.openstack.headers.HeaderTest;
import org.apache.http.impl.cookie.DateParseException;
import org.apache.http.impl.cookie.DateUtils;
import org.junit.Test;

import java.util.Date;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.fail;

public class IfModifiedSinceTest extends HeaderTest {

    public static final String olderDate          = "Tue, 1 Jan 2009 11:29:02 GMT";

    public static final String expectedDateString = "Tue, 02 Oct 2012 17:34:17 GMT";

    public static final String moreRecentDate     = "Tue, 19 Oct 2012 17:34:17 GMT";

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
    public void fromLongToDate() throws DateParseException {
        Long expectedMilliseconds = DateUtils.parseDate(expectedDateString).getTime();
        IfModifiedSince header = new IfModifiedSince(expectedMilliseconds);
        assertEquals(expectedDateString, header.getHeaderValue());
    }

    @Test
    public void addHeader() throws DateParseException {
        testHeader(new IfModifiedSince(expectedDateString));
    }

    @Test
    public void contentMustBeDifferent() throws DateParseException {
        new IfModifiedSince(expectedDateString).sinceAgainst(DateUtils.parseDate(moreRecentDate));
    }

    @Test
    public void unchangedContentIsError() throws DateParseException {
        try {
            new IfModifiedSince(expectedDateString).sinceAgainst(DateUtils.parseDate(olderDate));
            fail("should have thrown an exception");
        } catch (CommandException err) {
            assertEquals(CommandExceptionError.CONTENT_NOT_MODIFIED, err.getError());
        }
    }

    @Test
    public void supplyEmptyString() throws DateParseException {
        IfModifiedSince ifModifiedSince = new IfModifiedSince((String)null);
        assertNull(ifModifiedSince.getSinceDate());
    }

}
