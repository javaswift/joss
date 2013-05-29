package org.javaswift.joss.headers.account;

import mockit.Mocked;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.impl.cookie.DateParseException;
import org.apache.http.impl.cookie.DateUtils;
import org.javaswift.joss.exception.CommandException;
import org.javaswift.joss.headers.AbstractHeaderTest;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.javaswift.joss.command.impl.core.BaseCommandTest.prepareHeader;

public class ServerDateTest extends AbstractHeaderTest {

    @Test
    public void testAddHeaderByString() throws DateParseException {
        testHeader(new ServerDate("Sat, 22 Sep 2012 07:24:21 GMT"));
    }

    @Test
    public void testAddHeaderByMilliseconds() throws DateParseException {
        testHeader(new ServerDate(DateUtils.parseDate("Sat, 22 Sep 2012 07:24:21 GMT").getTime()));
    }

    @Test
    public void testAddHeaderByDate() throws DateParseException {
        testHeader(new ServerDate(DateUtils.parseDate("Sat, 22 Sep 2012 07:24:21 GMT")));
    }

    @Test(expected = CommandException.class)
    public void noLegalServerDate(@Mocked HttpResponse response) {
        final List<Header> headers = new ArrayList<Header>();
        prepareHeader(response, ServerDate.DATE, "xxx", headers);
        ServerDate.fromResponse(response);
    }

}
