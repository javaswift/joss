package org.javaswift.joss.headers.object;

import mockit.Mocked;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.impl.cookie.DateParseException;
import org.apache.http.impl.cookie.DateUtils;
import org.javaswift.joss.headers.AbstractHeaderTest;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertNull;
import static org.javaswift.joss.command.impl.core.BaseCommandTest.prepareHeader;
import static org.javaswift.joss.headers.object.DeleteAt.X_DELETE_AT;

public class DeleteAtTest extends AbstractHeaderTest {

    @Mocked
    private HttpResponse response;

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

    @Test
    public void noLegalDeleteAtDate() {
        final List<Header> headers = new ArrayList<Header>();
        prepareHeader(response, X_DELETE_AT, null, headers);
        assertNull(DeleteAt.fromResponse(response));
    }
}
