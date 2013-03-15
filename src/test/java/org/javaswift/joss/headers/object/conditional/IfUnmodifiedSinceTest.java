package org.javaswift.joss.headers.object.conditional;

import org.javaswift.joss.exception.ModifiedException;
import org.javaswift.joss.headers.AbstractHeaderTest;
import org.apache.http.impl.cookie.DateParseException;
import org.apache.http.impl.cookie.DateUtils;
import org.junit.Test;

import java.util.Date;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;

public class IfUnmodifiedSinceTest extends AbstractHeaderTest {

    public static final String olderDate          = "Tue, 1 Jan 2009 11:29:02 GMT";

    public static final String expectedDateString = "Tue, 02 Oct 2012 17:34:17 GMT";

    public static final String moreRecentDate     = "Tue, 19 Oct 2012 17:34:17 GMT";

    @Test
    public void insertDate() throws DateParseException {
        Date date = DateUtils.parseDate(expectedDateString);
        IfUnmodifiedSince header = new IfUnmodifiedSince(date);
        assertEquals(expectedDateString, header.getHeaderValue());
    }

    @Test
    public void fromStringToDate() throws DateParseException {
        IfUnmodifiedSince header = new IfUnmodifiedSince(expectedDateString);
        assertEquals(expectedDateString, header.getHeaderValue());
    }

    @Test
    public void fromLongToDate() throws DateParseException {
        Long expectedMilliseconds = DateUtils.parseDate(expectedDateString).getTime();
        IfUnmodifiedSince header = new IfUnmodifiedSince(expectedMilliseconds);
        assertEquals(expectedDateString, header.getHeaderValue());
    }

    @Test
    public void addHeader() throws DateParseException {
        testHeader(new IfUnmodifiedSince(expectedDateString));
    }

    @Test
    public void contentMustBeUnchanged() throws DateParseException {
        new IfUnmodifiedSince(expectedDateString).sinceAgainst(DateUtils.parseDate(olderDate));
    }

    @Test(expected = ModifiedException.class)
    public void changedContentIsError() throws DateParseException {
        new IfUnmodifiedSince(expectedDateString).sinceAgainst(DateUtils.parseDate(moreRecentDate));
    }

}

